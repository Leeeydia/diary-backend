package com.diary.backend.dto.response;

import com.diary.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<T> {

    private int status;
    private String code;
    private String message;
    private T data;

    // ── Success ──────────────────────────────

    public static <T> ResultData<T> success(T data) {
        return ResultData.<T>builder()
                .status(200)
                .code("SUCCESS")
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }

    public static ResultData<Void> success() {
        return ResultData.<Void>builder()
                .status(200)
                .code("SUCCESS")
                .message("요청이 성공적으로 처리되었습니다.")
                .build();
    }

    public static <T> ResultData<T> success(String message, T data) {
        return ResultData.<T>builder()
                .status(200)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    // ── Fail ─────────────────────────────────

    public static <T> ResultData<T> fail(ErrorCode errorCode) {
        return ResultData.<T>builder()
                .status(errorCode.getStatus())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> ResultData<T> fail(int status, String code, String message) {
        return ResultData.<T>builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }
}
