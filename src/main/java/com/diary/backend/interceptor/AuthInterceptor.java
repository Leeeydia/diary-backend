package com.diary.backend.interceptor;

import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String USER_ID_ATTRIBUTE = "userId";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    public AuthInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String token = header.substring(BEARER_PREFIX.length());
        if (!jwtProvider.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (!"access".equals(jwtProvider.getTokenType(token))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Long userId = jwtProvider.getUserIdFromToken(token);
        request.setAttribute(USER_ID_ATTRIBUTE, userId);
        return true;
    }
}
