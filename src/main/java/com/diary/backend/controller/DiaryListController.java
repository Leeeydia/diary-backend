package com.diary.backend.controller;

import com.diary.backend.dto.response.DiaryResponse;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.DiaryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryListController {

    private final DiaryService diaryService;

    @GetMapping
    public ResultData<List<DiaryResponse>> list(HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        List<DiaryResponse> result = diaryService.getDiaryList(memberId);
        return ResultData.success(result);
    }
}
