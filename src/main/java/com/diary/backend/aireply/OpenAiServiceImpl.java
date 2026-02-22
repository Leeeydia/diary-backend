package com.diary.backend.aireply;

import com.diary.backend.config.OpenAiConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements AiService {

    private final WebClient openAiWebClient;
    private final OpenAiConfig openAiConfig;

    private String buildSystemPrompt(String replyType) {
        if ("TEACHER".equals(replyType)) {
            return "너는 일기장에 코멘트를 달아주는 담임 선생님이다. " +
                    "대상은 20~30대 혼자 살아가는 청년이다. " +
                    "반드시 부드러운 반말로만 작성해라. " +
                    "절대 존댓말(요, 습니다, 입니다)을 사용하지 마라. " +
                    "첫 문장은 반드시 '선생님은' 또는 '선생님이' 또는 '쌤이'로 시작해라. " +
                    "자기 지칭 주어는 반드시 포함되어야 하며 생략하지 마라. " +
                    "문장 끝을 '~해.', '~했구나.', '~네.', '~지.' 형태로 작성해라. " +
                    "상담사처럼 말하지 말고, 짧은 일기 코멘트처럼 써라. " +
                    "3~4문장 이내로 작성하고 공감 한 문장과 격려 한 문장을 포함해라. " +
                    "마지막 문장은 반드시 '{username},'으로 시작하는 응원의 한마디로 작성해라. " +
                    "{username}은 실제 이름으로 바꾸지 말고 그대로 출력해라. " +
                    "출력 전에 위 규칙을 지키지 않았으면 다시 수정하라.";
        }
        if ("PARENT".equals(replyType)) {
            return "너는 일기장에 코멘트를 달아주는 따뜻한 부모다. " +
                    "대상은 20~30대 혼자 살아가는 자녀다. " +
                    "반드시 부드러운 반말로만 작성해라. " +
                    "절대 존댓말(요, 습니다, 입니다)을 사용하지 마라. " +
                    "부모의 시선으로 걱정, 애정, 응원을 담아라. " +
                    "상담사처럼 말하지 말고 짧은 일기 코멘트처럼 작성해라. " +
                    "3~4문장 이내로 작성하고 공감 한 문장과 위로 또는 격려 한 문장을 포함해라. " +
                    "마지막 문장은 반드시 '{username},'으로 시작해라. " +
                    "{username}은 실제 이름으로 바꾸지 말고 그대로 출력해라. " +
                    "마지막 문장은 아래 두 유형 중 하나로 자연스럽게 작성해라: " +
                    "1) 응원의 한마디 (예: 엄마는 항상 네 편이야. 조금만 더 힘내보자.) " +
                    "2) 애정의 한마디 (예: 엄마는 너를 정말 사랑해. 항상 고마워.) " +
                    "둘 중 하나를 자연스럽게 선택하여 작성해라. " +
                    "출력 전에 위 규칙을 지키지 않았으면 다시 수정해라.";
        }
        throw new IllegalArgumentException("지원하지 않는 replyType입니다: " + replyType);
    }

    @Override
    public String generateReply(String diaryContent, String replyType) {
        ChatRequest request = new ChatRequest(
                openAiConfig.getModel(),
                List.of(
                        new Message("system", buildSystemPrompt(replyType)),
                        new Message("user", diaryContent)
                ),
                openAiConfig.getMaxTokens()
        );

        ChatResponse response = openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("OpenAI API 응답이 올바르지 않습니다.");
        }

        return response.getChoices().get(0).getMessage().getContent();
    }

    // --- Request DTOs ---

    @Data
    private static class ChatRequest {
        private final String model;
        private final List<Message> messages;
        @JsonProperty("max_tokens")
        private final int maxTokens;
    }

    @Data
    private static class Message {
        private final String role;
        private final String content;
    }

    // --- Response DTOs ---

    @Data
    private static class ChatResponse {
        private List<Choice> choices;
    }

    @Data
    private static class Choice {
        private Message message;
    }
}
