package com.diary.backend.aireply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiReplyCreateResponse {
    private Long id;
    private Long diaryId;
    private String reply;
    private LocalDateTime createdAt;
}
