package com.diary.backend.controller;

import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResultData<Map<String, Object>> me(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        Map<String, Object> data = authService.getCurrentUser(memberId);
        return ResultData.success(data);
    }
}
