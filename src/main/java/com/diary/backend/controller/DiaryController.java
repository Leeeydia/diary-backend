package com.diary.backend.controller;

import com.diary.backend.dto.request.DiaryCreateRequest;
import com.diary.backend.dto.request.DiaryUpdateRequest;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.DiaryService;
import com.diary.backend.vo.DiaryVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public ResultData<DiaryVO> create(@RequestBody DiaryCreateRequest request, HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        DiaryVO diary = diaryService.createDiary(memberId, request);
        return ResultData.success("일기가 작성되었습니다.", diary);
    }

    @GetMapping
    public ResultData<List<DiaryVO>> list(HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        List<DiaryVO> diaries = diaryService.getDiariesByMember(memberId);
        return ResultData.success(diaries);
    }

    @GetMapping("/{id}")
    public ResultData<DiaryVO> detail(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        DiaryVO diary = diaryService.getDiary(id, memberId);
        return ResultData.success(diary);
    }

    @PutMapping("/{id}")
    public ResultData<DiaryVO> update(@PathVariable Long id,
                                      @RequestBody DiaryUpdateRequest request,
                                      HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        DiaryVO diary = diaryService.updateDiary(id, memberId, request);
        return ResultData.success("일기가 수정되었습니다.", diary);
    }

    @DeleteMapping("/{id}")
    public ResultData<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        diaryService.deleteDiary(id, memberId);
        return ResultData.success();
    }
}
