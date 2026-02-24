package com.diary.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponse {
    private Long id;
    private String username;
    private String email;
    private String replyMode;
    private String profileImageUrl;
}
