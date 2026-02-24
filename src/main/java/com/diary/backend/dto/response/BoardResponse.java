package com.diary.backend.dto.response;

import com.diary.backend.vo.Emotion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponse {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Emotion emotion;
    private LocalDateTime createdAt;
}
