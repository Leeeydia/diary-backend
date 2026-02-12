package com.diary.backend.service;

import com.diary.backend.dto.request.DiaryCreateRequest;
import com.diary.backend.dto.request.DiaryUpdateRequest;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.vo.DiaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiaryService {

    private final DiaryMapper diaryMapper;

    public DiaryService(DiaryMapper diaryMapper) {
        this.diaryMapper = diaryMapper;
    }

    @Transactional
    public DiaryVO createDiary(Long memberId, DiaryCreateRequest request) {
        DiaryVO diary = DiaryVO.builder()
                .memberId(memberId)
                .content(request.getContent())
                .emotion(request.getEmotion())
                .build();

        diaryMapper.insert(diary);
        return diaryMapper.findById(diary.getId());
    }

    public List<DiaryVO> getDiariesByMember(Long memberId) {
        return diaryMapper.findByMemberId(memberId);
    }

    public DiaryVO getDiary(Long diaryId, Long memberId) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }
        return diary;
    }

    @Transactional
    public DiaryVO updateDiary(Long diaryId, Long memberId, DiaryUpdateRequest request) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }

        diary.setContent(request.getContent());
        diary.setEmotion(request.getEmotion());

        diaryMapper.update(diary);
        return diaryMapper.findById(diaryId);
    }

    @Transactional
    public void deleteDiary(Long diaryId, Long memberId) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }
        diaryMapper.softDeleteById(diaryId);
    }
}
