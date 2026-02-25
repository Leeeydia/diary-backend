package com.diary.backend.service;

import com.diary.backend.dto.request.LoginRequest;
import com.diary.backend.dto.request.RegisterRequest;
import com.diary.backend.dto.response.TokenResponse;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.jwt.JwtProvider;
import com.diary.backend.mapper.MemberMapper;
import com.diary.backend.mapper.RefreshTokenMapper;
import com.diary.backend.vo.MemberVO;
import com.diary.backend.vo.RefreshTokenVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final MemberMapper memberMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(MemberMapper memberMapper,
                       RefreshTokenMapper refreshTokenMapper,
                       JwtProvider jwtProvider) {
        this.memberMapper = memberMapper;
        this.refreshTokenMapper = refreshTokenMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtProvider = jwtProvider;
    }

    // ==============================
    // 회원가입
    // ==============================
    public Map<String, Object> register(RegisterRequest request) {

        if (memberMapper.existsByEmail(request.getEmail()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (memberMapper.existsByUsername(request.getUsername()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        MemberVO member = MemberVO.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        memberMapper.insert(member);

        Map<String, Object> result = new HashMap<>();
        result.put("id", member.getId());
        result.put("email", member.getEmail());
        result.put("username", member.getUsername());

        return result;
    }

    // ==============================
    // 로그인
    // ==============================
    public Map<String, Object> login(LoginRequest request) {

        MemberVO member = memberMapper.findByUsername(request.getUsername());

        if (member == null ||
                !passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        RefreshTokenVO tokenVO = RefreshTokenVO.builder()
                .memberId(member.getId())
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenMapper.upsert(tokenVO);

        Map<String, Object> result = new HashMap<>();
        result.put("id", member.getId());
        result.put("email", member.getEmail());
        result.put("username", member.getUsername());
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

    // ==============================
    // 로그아웃
    // ==============================
    public void logout(Long memberId) {
        refreshTokenMapper.deleteByMemberId(memberId);
    }

    // ==============================
    // 토큰 재발급
    // ==============================
    public TokenResponse refresh(String refreshToken) {

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (!"refresh".equals(jwtProvider.getTokenType(refreshToken))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Long memberId = jwtProvider.getUserIdFromToken(refreshToken);

        RefreshTokenVO storedToken = refreshTokenMapper.findByMemberId(memberId);
        if (storedToken == null ||
                !refreshToken.equals(storedToken.getToken())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String newAccessToken = jwtProvider.createAccessToken(memberId);
        String newRefreshToken = jwtProvider.createRefreshToken(memberId);

        RefreshTokenVO newTokenVO = RefreshTokenVO.builder()
                .memberId(memberId)
                .token(newRefreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenMapper.upsert(newTokenVO);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    // ==============================
    // 현재 사용자 조회
    // ==============================
    public Map<String, Object> getCurrentUser(Long memberId) {

        MemberVO member = memberMapper.findById(memberId);

        if (member == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", member.getId());
        result.put("email", member.getEmail());
        result.put("username", member.getUsername());
        result.put("role", member.getRole());
        result.put("createdAt", member.getCreatedAt());

        return result;
    }
}