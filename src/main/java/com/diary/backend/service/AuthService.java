package com.diary.backend.service;

import com.diary.backend.dto.request.LoginRequest;
import com.diary.backend.dto.request.RegisterRequest;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.UserMapper;
import com.diary.backend.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Register a new user.
     * Returns a simple map with user id + nickname (no password).
     */
    public Map<String, Object> register(RegisterRequest request) {
        // duplicate check
        if (userMapper.existsByEmail(request.getEmail()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        UserVO user = UserVO.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userMapper.insert(user);

        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("email", user.getEmail());
        result.put("nickname", user.getNickname());
        return result;
    }

    /**
     * Authenticate user.
     * Returns userId on success for session storage.
     */
    public Long login(LoginRequest request) {
        UserVO user = userMapper.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
        return user.getId();
    }

    /**
     * Get current user info (no password).
     */
    public Map<String, Object> getCurrentUser(Long userId) {
        UserVO user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("email", user.getEmail());
        result.put("nickname", user.getNickname());
        result.put("createdAt", user.getCreatedAt());
        return result;
    }
}
