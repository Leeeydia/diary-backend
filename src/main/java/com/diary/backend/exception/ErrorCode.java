package com.diary.backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ── Auth ─────────────────────────
    DUPLICATE_EMAIL(409, "이미 사용 중인 이메일입니다."),
    INVALID_CREDENTIALS(401, "이메일 또는 비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED(401, "로그인이 필요합니다."),

    // ── Diary ────────────────────────
    DIARY_NOT_FOUND(404, "일기를 찾을 수 없습니다."),
    DIARY_ACCESS_DENIED(403, "해당 일기에 접근 권한이 없습니다."),

    // ── Common ───────────────────────
    INVALID_INPUT(400, "입력값이 올바르지 않습니다."),
    INTERNAL_ERROR(500, "서버 내부 오류가 발생했습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
