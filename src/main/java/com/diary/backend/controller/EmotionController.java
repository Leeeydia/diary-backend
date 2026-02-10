package com.diary.backend.controller;

import com.diary.backend.dto.response.EmotionStatsResponse;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.EmotionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emotions")
public class EmotionController {

    private final EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    /**
     * GET /api/emotions/stats?startDate=2026-01-01&endDate=2026-01-31
     */
    @GetMapping("/stats")
    public ResultData<List<EmotionStatsResponse>> stats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        List<EmotionStatsResponse> stats = emotionService.getStats(userId, startDate, endDate);
        return ResultData.success(stats);
    }

    /**
     * GET /api/emotions/calendar?startDate=2026-02-01&endDate=2026-02-28
     */
    @GetMapping("/calendar")
    public ResultData<List<Map<String, Object>>> calendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthInterceptor.SESSION_USER_ID);
        List<Map<String, Object>> calendar = emotionService.getCalendar(userId, startDate, endDate);
        return ResultData.success(calendar);
    }
}
