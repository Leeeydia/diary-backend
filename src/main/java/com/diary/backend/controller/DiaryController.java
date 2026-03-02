package com.diary.backend.controller;

import com.diary.backend.dto.request.DiaryCreateRequest;
import com.diary.backend.dto.request.DiaryUpdateRequest;
import com.diary.backend.dto.response.DiaryResponse;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.DiaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // ==========================
    // 생성
    // ==========================
    @PostMapping
    public ResultData<DiaryResponse> create(
            @RequestBody DiaryCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId =
                (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);

        DiaryResponse diary =
                diaryService.createDiary(memberId, request);

        return ResultData.success("일기가 작성되었습니다.", diary);
    }

    // ==========================
    // 단건 조회
    // ==========================
    @GetMapping("/{id}")
    public ResultData<DiaryResponse> detail(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        Long memberId =
                (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);

        DiaryResponse diary =
                diaryService.getDiary(id, memberId);

        return ResultData.success("일기 조회 성공", diary);
    }

    // ==========================
    // 수정
    // ==========================
    @PutMapping("/{id}")
    public ResultData<DiaryResponse> update(
            @PathVariable Long id,
            @RequestBody DiaryUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId =
                (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);

        DiaryResponse diary =
                diaryService.updateDiary(id, memberId, request);

        return ResultData.success("일기가 수정되었습니다.", diary);
    }

    // ==========================
    // 삭제
    // ==========================
    @DeleteMapping("/{id}")
    public ResultData<Void> delete(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        Long memberId =
                (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);

        diaryService.deleteDiary(id, memberId);

        return ResultData.success("일기가 삭제되었습니다.", null);
    }
}