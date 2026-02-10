package com.diary.backend.controller;

import com.diary.backend.dto.request.LoginRequest;
import com.diary.backend.dto.request.RegisterRequest;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResultData<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> data = authService.register(request);
        return ResultData.success("회원가입이 완료되었습니다.", data);
    }

    /**
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResultData<Map<String, Object>> login(@RequestBody LoginRequest request, HttpSession session) {
        Long userId = authService.login(request);
        session.setAttribute(AuthInterceptor.SESSION_USER_ID, userId);

        Map<String, Object> data = authService.getCurrentUser(userId);
        return ResultData.success("로그인 성공", data);
    }

    /**
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResultData<Void> logout(HttpSession session) {
        session.invalidate();
        return ResultData.success();
    }

    /**
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResultData<Map<String, Object>> me(HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        Map<String, Object> data = authService.getCurrentUser(userId);
        return ResultData.success(data);
    }
}
