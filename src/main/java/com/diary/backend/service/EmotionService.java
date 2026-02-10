package com.diary.backend.service;

import com.diary.backend.dto.response.EmotionStatsResponse;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.mapper.EmotionMapper;
import com.diary.backend.vo.DiaryVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmotionService {

    private final EmotionMapper emotionMapper;
    private final DiaryMapper diaryMapper;

    public EmotionService(EmotionMapper emotionMapper, DiaryMapper diaryMapper) {
        this.emotionMapper = emotionMapper;
        this.diaryMapper = diaryMapper;
    }

    /**
     * Get emotion statistics for a user within a date range.
     */
    public List<EmotionStatsResponse> getStats(Long userId, LocalDate startDate, LocalDate endDate) {
        return emotionMapper.getStatsByUserId(userId, startDate, endDate);
    }

    /**
     * Get calendar data: date -> primary emotion for each diary in range.
     */
    public List<Map<String, Object>> getCalendar(Long userId, LocalDate startDate, LocalDate endDate) {
        List<DiaryVO> diaries = diaryMapper.findByUserIdAndDateRange(userId, startDate, endDate);

        return diaries.stream().map(d -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("date", d.getWrittenDate());
            entry.put("emotion", d.getEmotion());
            entry.put("emotionScore", d.getEmotionScore());
            entry.put("diaryId", d.getId());
            entry.put("title", d.getTitle());
            return entry;
        }).collect(Collectors.toList());
    }
}
