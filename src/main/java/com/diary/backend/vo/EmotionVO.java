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
public class EmotionVO {
    private Long id;
    private Long diaryId;
    private String emotionTag;
    private Integer score;
    private LocalDateTime createdAt;
}
