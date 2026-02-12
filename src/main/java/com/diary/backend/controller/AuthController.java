package com.diary.backend.controller;

import com.diary.backend.dto.request.LoginRequest;
import com.diary.backend.dto.request.RefreshRequest;
import com.diary.backend.dto.request.RegisterRequest;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.dto.response.TokenResponse;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResultData<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> data = authService.register(request);
        return ResultData.success("회원가입이 완료되었습니다.", data);
    }

    @PostMapping("/login")
    public ResultData<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Map<String, Object> data = authService.login(request);
        return ResultData.success("로그인 성공", data);
    }

    @PostMapping("/logout")
    public ResultData<Void> logout(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        authService.logout(memberId);
        return ResultData.success();
    }

    @GetMapping("/me")
    public ResultData<Map<String, Object>> me(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        Map<String, Object> data = authService.getCurrentUser(memberId);
        return ResultData.success(data);
    }

    @PostMapping("/refresh")
    public ResultData<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        TokenResponse tokens = authService.refresh(request.getRefreshToken());
        return ResultData.success("토큰이 갱신되었습니다.", tokens);
    }
}
