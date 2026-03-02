package com.diary.backend.controller;

import com.diary.backend.dto.response.MyPageResponse;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.AuthService;
import com.diary.backend.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final AuthService authService;
    private final MyPageService myPageService;

    public MemberController(AuthService authService, MyPageService myPageService) {
        this.authService = authService;
        this.myPageService = myPageService;
    }

    @GetMapping("/me")
    public ResultData<Map<String, Object>> me(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        Map<String, Object> data = authService.getCurrentUser(memberId);
        return ResultData.success(data);
    }

    @PostMapping("/profile/image")
    public ResultData<MyPageResponse> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        MyPageResponse response = myPageService.uploadProfileImage(memberId, file);
        return ResultData.success("프로필 이미지가 업로드되었습니다.", response);
    }
}
