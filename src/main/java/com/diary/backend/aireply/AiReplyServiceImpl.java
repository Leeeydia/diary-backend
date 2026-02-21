package com.diary.backend.aireply;

import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.vo.DiaryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiReplyServiceImpl implements AiReplyService {

    private final AiReplyMapper aiReplyMapper;
    private final DiaryMapper diaryMapper;
    private final AiService aiService;

    @Override
    @Transactional
    public AiReplyCreateResponse generateReply(Long diaryId, Long memberId) {

        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }

        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }

        AiReplyVO existing = aiReplyMapper.findByDiaryId(diaryId);
        if (existing != null) {
            throw new CustomException(ErrorCode.AI_REPLY_ALREADY_EXISTS);
        }

        String replyContent = aiService.generateReply(diary.getContent());

        AiReplyVO aiReply = AiReplyVO.builder()
                .diaryId(diaryId)
                .replyType("PARENT")   // 일단 고정값 (나중에 모드 선택 기능 추가 가능)
                .replyContent(replyContent)
                .build();

        aiReplyMapper.insert(aiReply);

        return AiReplyCreateResponse.builder()
                .id(aiReply.getId())
                .diaryId(aiReply.getDiaryId())
                .reply(aiReply.getReplyContent())
                .createdAt(aiReply.getCreatedAt())
                .build();
    }
}
