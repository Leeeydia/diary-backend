package com.diary.backend.service;

import com.diary.backend.dto.response.MyPageResponse;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.MemberMapper;
import com.diary.backend.vo.MemberVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class MyPageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024L; // 5MB

    private final MemberMapper memberMapper;
    private final String profileUploadDir;

    public MyPageService(MemberMapper memberMapper,
                         @Value("${file.upload.profile-dir}") String profileUploadDir) {
        this.memberMapper = memberMapper;
        this.profileUploadDir = profileUploadDir;
    }

    public MyPageResponse getMyPage(Long memberId) {
        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return toResponse(member);
    }

    public MyPageResponse uploadProfileImage(Long memberId, MultipartFile file) {
        validateFile(file);

        String extension = extractExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;
        String imageUrl = "/uploads/profile/" + filename;

        Path uploadPath = Paths.get(profileUploadDir);
        try {
            Files.createDirectories(uploadPath);
            file.transferTo(uploadPath.resolve(filename).toFile());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        // 기존 이미지 파일 삭제
        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        deleteOldProfileImage(member.getProfileImageUrl());

        memberMapper.updateProfileImage(memberId, imageUrl);
        member.setProfileImageUrl(imageUrl);
        return toResponse(member);
    }

    public MyPageResponse updateNickname(Long memberId, String nickname) {
        if (nickname == null || nickname.contains(" ")
                || nickname.length() < 2 || nickname.length() > 10) {
            throw new CustomException(ErrorCode.INVALID_USERNAME);
        }

        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        memberMapper.updateNickname(memberId, nickname);
        member.setNickname(nickname);
        return toResponse(member);
    }

    public MyPageResponse updateReplyMode(Long memberId, String replyMode) {
        if (replyMode == null || (!replyMode.equals("PARENT") && !replyMode.equals("TEACHER"))) {
            throw new CustomException(ErrorCode.INVALID_REPLY_MODE);
        }

        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        memberMapper.updateReplyMode(memberId, replyMode);
        member.setReplyMode(replyMode);
        return toResponse(member);
    }

    public MyPageResponse deleteProfileImage(Long memberId) {
        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        deleteOldProfileImage(member.getProfileImageUrl());
        memberMapper.updateProfileImage(memberId, null);
        member.setProfileImageUrl(null);
        return toResponse(member);
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(ErrorCode.FILE_TOO_LARGE);
        }
        String ext = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }

    private void deleteOldProfileImage(String existingUrl) {
        if (existingUrl == null || existingUrl.isBlank()) return;
        // "/uploads/profile/xxx.jpg" → "uploads/profile/xxx.jpg"
        String relativePath = existingUrl.startsWith("/") ? existingUrl.substring(1) : existingUrl;
        Path oldFile = Paths.get(relativePath);
        try {
            Files.deleteIfExists(oldFile);
        } catch (IOException ignored) {
            // 기존 파일 삭제 실패 시 업로드는 계속 진행
        }
    }

    private MyPageResponse toResponse(MemberVO member) {
        return MyPageResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .replyMode(member.getReplyMode())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
