package com.diary.backend.service;

import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.AiReplyMapper;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.vo.AiReplyVO;
import com.diary.backend.vo.DiaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiReplyService {

    private final AiReplyMapper aiReplyMapper;
    private final DiaryMapper diaryMapper;
    private final OpenAiService openAiService;

    public AiReplyService(AiReplyMapper aiReplyMapper, DiaryMapper diaryMapper, OpenAiService openAiService) {
        this.aiReplyMapper = aiReplyMapper;
        this.diaryMapper = diaryMapper;
        this.openAiService = openAiService;
    }

    @Transactional
    public AiReplyVO generateReply(Long diaryId, Long memberId, String replyType) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }

        AiReplyVO existing = aiReplyMapper.findByDiaryIdAndType(diaryId, replyType);
        if (existing != null) {
            return existing;
        }

        String replyContent = openAiService.generateReply(diary.getContent(), replyType);

        AiReplyVO aiReply = AiReplyVO.builder()
                .diaryId(diaryId)
                .replyType(replyType)
                .replyContent(replyContent)
                .build();

        aiReplyMapper.insert(aiReply);
        return aiReply;
    }

    public List<AiReplyVO> getReplies(Long diaryId, Long memberId) {
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }
        return aiReplyMapper.findByDiaryId(diaryId);
    }
}
