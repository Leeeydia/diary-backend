package com.diary.backend.controller;

import com.diary.backend.dto.request.UpdateReplyModeRequest;
import com.diary.backend.dto.request.UpdateNicknameRequest;
import com.diary.backend.dto.response.MyPageResponse;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping
    public ResponseEntity<ResultData<MyPageResponse>> getMyPage(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("userId");
        MyPageResponse response = myPageService.getMyPage(memberId);
        return ResponseEntity.ok(ResultData.success(response));
    }

    @PostMapping("/profile-image")
    public ResponseEntity<ResultData<MyPageResponse>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("userId");
        MyPageResponse response = myPageService.uploadProfileImage(memberId, file);
        return ResponseEntity.ok(ResultData.success("프로필 이미지가 업로드되었습니다.", response));
    }

    @PutMapping("/nickname")
    public ResponseEntity<ResultData<MyPageResponse>> updateNickname(
            @RequestBody UpdateNicknameRequest body,
            HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("userId");
        MyPageResponse response = myPageService.updateNickname(memberId, body.getNickname());
        return ResponseEntity.ok(ResultData.success("닉네임이 변경되었습니다.", response));
    }

    @PutMapping("/reply-mode")
    public ResponseEntity<ResultData<MyPageResponse>> updateReplyMode(
            @RequestBody UpdateReplyModeRequest body,
            HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("userId");
        MyPageResponse response = myPageService.updateReplyMode(memberId, body.getReplyMode());
        return ResponseEntity.ok(ResultData.success("답장 모드가 변경되었습니다.", response));
    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<ResultData<MyPageResponse>> deleteProfileImage(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("userId");
        MyPageResponse response = myPageService.deleteProfileImage(memberId);
        return ResponseEntity.ok(ResultData.success("프로필 이미지가 삭제되었습니다.", response));
    }
}
