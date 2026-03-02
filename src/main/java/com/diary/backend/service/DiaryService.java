package com.diary.backend.service;

import com.diary.backend.dto.request.DiaryCreateRequest;
import com.diary.backend.dto.request.DiaryUpdateRequest;
import com.diary.backend.dto.response.DiaryResponse;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.mapper.MemberMapper;
import com.diary.backend.vo.Emotion;
import com.diary.backend.vo.MemberVO;
import com.diary.backend.vo.DiaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final DiaryMapper diaryMapper;
    private final MemberMapper memberMapper;

    public DiaryService(DiaryMapper diaryMapper, MemberMapper memberMapper) {
        this.diaryMapper = diaryMapper;
        this.memberMapper = memberMapper;
    }

    // ==========================
    // 생성
    // ==========================
    @Transactional
    public DiaryResponse createDiary(Long memberId, DiaryCreateRequest request) {

        DiaryVO diary = DiaryVO.builder()
                .memberId(memberId)
                .content(request.getContent())
                .emotion(request.getEmotion())
                .build();

        diaryMapper.insert(diary);

        DiaryVO saved = diaryMapper.findById(diary.getId());

        return toResponse(saved);
    }

    // ==========================
    // 회원별 목록 조회 (VO 그대로 필요할 때)
    // ==========================
    public List<DiaryVO> getDiariesByMember(Long memberId) {
        return diaryMapper.findByMemberId(memberId);
    }

    // ==========================
    // 회원별 목록 조회 (Response 변환)
    // ==========================
    public List<DiaryResponse> getDiaryList(Long memberId, Emotion emotion, int page, int size) {

        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        int offset = page * size;

        return diaryMapper
                .findByMemberIdWithEmotion(member.getId(), emotion, size, offset)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ==========================
    // 단건 조회
    // ==========================
    public DiaryResponse getDiary(Long diaryId, Long memberId) {

        DiaryVO diary = diaryMapper.findById(diaryId);

        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }

        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }

        return toResponse(diary);
    }

    // ==========================
    // 수정
    // ==========================
    @Transactional
    public DiaryResponse updateDiary(Long diaryId, Long memberId, DiaryUpdateRequest request) {

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

        DiaryVO updated = diaryMapper.findById(diaryId);

        return toResponse(updated);
    }

    // ==========================
    // 삭제
    // ==========================
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

    // ==========================
    // 공통 변환 메서드 (⭐ 핵심)
    // ==========================
    private DiaryResponse toResponse(DiaryVO diary) {
        return DiaryResponse.builder()
                .id(diary.getId())
                .content(diary.getContent())
                .emotion(diary.getEmotion())
                .createdAt(diary.getCreatedAt())
                .nickname(diary.getNickname())   // ✅ 여기 중요
                .build();
    }
}