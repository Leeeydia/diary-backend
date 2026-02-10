package com.diary.backend.service;

import com.diary.backend.dto.request.DiaryCreateRequest;
import com.diary.backend.dto.request.DiaryUpdateRequest;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.mapper.EmotionMapper;
import com.diary.backend.vo.DiaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiaryService {

    private final DiaryMapper diaryMapper;
    private final EmotionMapper emotionMapper;

    public DiaryService(DiaryMapper diaryMapper, EmotionMapper emotionMapper) {
        this.diaryMapper = diaryMapper;
        this.emotionMapper = emotionMapper;
    }

    @Transactional
    public DiaryVO createDiary(Long userId, DiaryCreateRequest request) {
        DiaryVO diary = DiaryVO.builder()
                .userId(userId)
                .title(request.getTitle())
                .content(request.getContent())
                .voiceUrl(request.getVoiceUrl())
                .emotion(request.getEmotion())
                .emotionScore(request.getEmotionScore())
                .writtenDate(request.getWrittenDate())
                .build();

        diaryMapper.insert(diary);
        return diaryMapper.findById(diary.getId());
    }

    public List<DiaryVO> getDiariesByUser(Long userId) {
        return diaryMapper.findByUserId(userId);
    }

    public DiaryVO getDiary(Long diaryId, Long userId) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }
        // attach emotion details
        diary.setEmotions(emotionMapper.findByDiaryId(diaryId));
        return diary;
    }

    @Transactional
    public DiaryVO updateDiary(Long diaryId, Long userId, DiaryUpdateRequest request) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }

        diary.setTitle(request.getTitle());
        diary.setContent(request.getContent());
        diary.setVoiceUrl(request.getVoiceUrl());
        diary.setEmotion(request.getEmotion());
        diary.setEmotionScore(request.getEmotionScore());
        diary.setWrittenDate(request.getWrittenDate());

        diaryMapper.update(diary);
        return diaryMapper.findById(diaryId);
    }

    @Transactional
    public void deleteDiary(Long diaryId, Long userId) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }
        diaryMapper.deleteById(diaryId);
    }
}
