package com.diary.backend.aireply;

public interface AiReplyService {

    AiReplyCreateResponse generateReply(Long diaryId, Long memberId);

    AiReplyCreateResponse getReply(Long diaryId, Long memberId);
}
