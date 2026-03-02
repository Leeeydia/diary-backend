package com.diary.backend.dto.response;

import com.diary.backend.vo.Emotion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DiaryResponse {
    private Long id;
    private String content;
    private Emotion emotion;
    private LocalDateTime createdAt;
    private String nickname; // ✅ 추가
}
