package com.diary.backend.aireply;

import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class AiReplyController {

    private final AiReplyService aiReplyService;

    @PostMapping("/{id}/reply")
    public ResultData<AiReplyCreateResponse> generateReply(@PathVariable Long id,
                                                           HttpServletRequest httpRequest) {

        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);

        AiReplyCreateResponse response =
                aiReplyService.generateReply(id, memberId);

        return ResultData.success("AI 답변이 생성되었습니다.", response);
    }

    @GetMapping("/{id}/reply")
    public ResultData<AiReplyCreateResponse> getReply(@PathVariable Long id,
                                                      HttpServletRequest httpRequest) {

        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        AiReplyCreateResponse response = aiReplyService.getReply(id, memberId);
        return ResultData.success(response);
    }
}