package com.diary.backend.service;

import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final WebClient openAiWebClient;
    private final String model;
    private final int maxTokens;

    public OpenAiService(
            WebClient openAiWebClient,
            @Value("${openai.model}") String model,
            @Value("${openai.max-tokens}") int maxTokens) {
        this.openAiWebClient = openAiWebClient;
        this.model = model;
        this.maxTokens = maxTokens;
    }

    @SuppressWarnings("unchecked")
    public String generateReply(String diaryContent, String replyType) {
        String systemPrompt = buildSystemPrompt(replyType);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", maxTokens,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", diaryContent)
                )
        );

        try {
            Map<String, Object> response = openAiWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_SERVICE_ERROR);
        }
    }

    private String buildSystemPrompt(String replyType) {
        return switch (replyType) {
            case "TEACHER" -> "당신은 학생의 일기를 읽고 따뜻하고 교육적인 조언을 해주는 선생님입니다. " +
                    "학생의 감정을 공감하면서도 성장에 도움이 되는 피드백을 한국어로 제공해주세요.";
            case "PARENT" -> "당신은 자녀의 일기를 읽고 사랑과 걱정을 담아 답장하는 부모님입니다. " +
                    "자녀의 감정을 이해하고 위로와 응원의 메시지를 한국어로 전해주세요.";
            case "FRIEND" -> "당신은 친구의 일기를 읽고 친근하게 반응하는 친구입니다. " +
                    "편한 말투로 공감하고 함께 기뻐하거나 위로해주는 메시지를 한국어로 보내주세요.";
            default -> throw new CustomException(ErrorCode.INVALID_REPLY_TYPE);
        };
    }
}
