package com.diary.backend.dto.request;

import lombok.Data;

@Data
public class DiaryUpdateRequest {
    private String content;
    private String emotion;
}
