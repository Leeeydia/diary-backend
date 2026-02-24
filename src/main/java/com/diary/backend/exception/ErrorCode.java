package com.diary.backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ── Auth ─────────────────────────
    DUPLICATE_EMAIL(409, "이미 사용 중인 이메일입니다."),
    DUPLICATE_USERNAME(409, "이미 사용 중인 사용자 이름입니다."),
    INVALID_CREDENTIALS(401, "이메일 또는 비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED(401, "로그인이 필요합니다."),

    // ── Member ───────────────────────
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    INVALID_REPLY_MODE(400, "유효하지 않은 답장 모드입니다. PARENT 또는 TEACHER만 허용됩니다."),
    INVALID_USERNAME(400, "닉네임은 공백 없이 2자 이상 10자 이하로 입력해 주세요."),

    // ── Diary ────────────────────────
    DIARY_NOT_FOUND(404, "일기를 찾을 수 없습니다."),
    DIARY_ACCESS_DENIED(403, "해당 일기에 접근 권한이 없습니다."),

    // ── Board ────────────────────────
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    BOARD_ACCESS_DENIED(403, "해당 게시글에 접근 권한이 없습니다."),

    // ── AI Reply ─────────────────────
    AI_REPLY_NOT_FOUND(404, "AI 답변을 찾을 수 없습니다."),
    AI_REPLY_ALREADY_EXISTS(409, "이미 AI 답변이 존재합니다."),
    AI_SERVICE_ERROR(500, "AI 서비스 호출 중 오류가 발생했습니다."),

    // ── File ─────────────────────────
    INVALID_FILE_TYPE(400, "jpg, jpeg, png 파일만 업로드할 수 있습니다."),
    FILE_TOO_LARGE(413, "파일 크기는 5MB 이하여야 합니다."),
    FILE_UPLOAD_FAILED(500, "파일 업로드 중 오류가 발생했습니다."),

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
