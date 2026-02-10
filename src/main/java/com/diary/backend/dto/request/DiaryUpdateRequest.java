package com.diary.backend.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryUpdateRequest {
    private String title;
    private String content;
    private String voiceUrl;
    private String emotion;
    private Integer emotionScore;
    private LocalDate writtenDate;
}
