package com.diary.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenVO {
    private Long id;
    private Long memberId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
