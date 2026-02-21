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

    private static final String SYSTEM_PROMPT =
            "당신은 사용자의 일기를 읽고 공감하며 따뜻하게 답변해주는 AI 상담사입니다. " +
            "사용자의 감정을 존중하고, 위로와 격려의 말을 전해주세요. " +
            "답변은 2~3문장으로 간결하게 작성해주세요.";

    private final WebClient openAiWebClient;
    private final OpenAiConfig openAiConfig;

    @Override
    public String generateReply(String diaryContent) {
        ChatRequest request = new ChatRequest(
                openAiConfig.getModel(),
                List.of(
                        new Message("system", SYSTEM_PROMPT),
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
