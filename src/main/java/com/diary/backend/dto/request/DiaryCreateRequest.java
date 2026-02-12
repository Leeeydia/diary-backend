package com.diary.backend.dto.request;

import lombok.Data;

@Data
public class DiaryCreateRequest {
    private String content;
    private String emotion;
}
