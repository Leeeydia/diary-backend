package com.diary.backend.controller;

import com.diary.backend.dto.request.DiaryCreateRequest;
import com.diary.backend.dto.request.DiaryUpdateRequest;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.DiaryService;
import com.diary.backend.vo.DiaryVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    /**
     * POST /api/diaries
     */
    @PostMapping
    public ResultData<DiaryVO> create(@RequestBody DiaryCreateRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        DiaryVO diary = diaryService.createDiary(userId, request);
        return ResultData.success("일기가 작성되었습니다.", diary);
    }

    /**
     * GET /api/diaries
     */
    @GetMapping
    public ResultData<List<DiaryVO>> list(HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        List<DiaryVO> diaries = diaryService.getDiariesByUser(userId);
        return ResultData.success(diaries);
    }

    /**
     * GET /api/diaries/{id}
     */
    @GetMapping("/{id}")
    public ResultData<DiaryVO> detail(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        DiaryVO diary = diaryService.getDiary(id, userId);
        return ResultData.success(diary);
    }

    /**
     * PUT /api/diaries/{id}
     */
    @PutMapping("/{id}")
    public ResultData<DiaryVO> update(@PathVariable Long id,
                                      @RequestBody DiaryUpdateRequest request,
                                      HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        DiaryVO diary = diaryService.updateDiary(id, userId, request);
        return ResultData.success("일기가 수정되었습니다.", diary);
    }

    /**
     * DELETE /api/diaries/{id}
     */
    @DeleteMapping("/{id}")
    public ResultData<Void> delete(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        diaryService.deleteDiary(id, userId);
        return ResultData.success();
    }
}
