package com.diary.backend.aireply;

import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.DiaryMapper;
import com.diary.backend.mapper.MemberMapper;
import com.diary.backend.vo.DiaryVO;
import com.diary.backend.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiReplyServiceImpl implements AiReplyService {

    private final AiReplyMapper aiReplyMapper;
    private final DiaryMapper diaryMapper;
    private final MemberMapper memberMapper;
    private final AiService aiService;

    @Override
    @Transactional
    public AiReplyCreateResponse generateReply(Long diaryId, Long memberId) {

        // 1️⃣ 일기 조회
        DiaryVO diary = diaryMapper.findById(diaryId);
        if (diary == null) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }

        // 2️⃣ 작성자 검증
        if (!diary.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
        }

        // 3️⃣ 이미 답장 존재하는지 확인
        AiReplyVO existing = aiReplyMapper.findByDiaryId(diaryId);
        if (existing != null) {
            throw new CustomException(ErrorCode.AI_REPLY_ALREADY_EXISTS);
        }

        // 4️⃣ 회원 조회
        MemberVO member = memberMapper.findById(memberId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String replyMode = member.getReplyMode();
        if (!"PARENT".equals(replyMode) && !"TEACHER".equals(replyMode)) {
            throw new CustomException(ErrorCode.INVALID_REPLY_MODE);
        }

        // 5️⃣ AI 원본 답장 생성
        String replyContent = aiService.generateReply(diary.getContent(), replyMode);

        // 6️⃣ username 치환
        String finalReplyContent =
                replyContent.replace("{username}", member.getUsername());

        // 7️⃣ 저장
        AiReplyVO aiReply = AiReplyVO.builder()
                .diaryId(diaryId)
                .replyType(replyMode)
                .replyContent(finalReplyContent)
                .build();

        aiReplyMapper.insert(aiReply);

        // 🔥 8️⃣ createdAt 채워오기 (insert 후 다시 조회)
        AiReplyVO savedReply = aiReplyMapper.findByDiaryId(diaryId);

        return AiReplyCreateResponse.builder()
                .id(savedReply.getId())
                .diaryId(savedReply.getDiaryId())
                .reply(savedReply.getReplyContent())
                .createdAt(savedReply.getCreatedAt())
                .build();
    }
}