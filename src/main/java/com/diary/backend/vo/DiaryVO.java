package com.diary.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryVO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String voiceUrl;
    private String emotion;
    private Integer emotionScore;
    private LocalDate writtenDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Joined emotion details (optional, populated in some queries) */
    private List<EmotionVO> emotions;
}
