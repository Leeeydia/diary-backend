package com.diary.backend.dto.request;

import com.diary.backend.vo.Emotion;
import lombok.Data;

@Data
public class BoardCreateRequest {
    private String title;
    private String content;
    private Emotion emotion;
}
