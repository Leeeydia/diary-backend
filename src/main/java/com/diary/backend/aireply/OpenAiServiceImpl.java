package com.diary.backend.aireply;

import org.springframework.stereotype.Service;

@Service
public class OpenAiServiceImpl implements AiService {

    @Override
    public String generateReply(String diaryContent) {
        // TODO: OpenAI API 연동 예정
        return "AI 답변이 준비 중입니다.";
    }
}
