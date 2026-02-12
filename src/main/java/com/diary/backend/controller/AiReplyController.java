package com.diary.backend.controller;

import com.diary.backend.dto.request.AiReplyRequest;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.AiReplyService;
import com.diary.backend.vo.AiReplyVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries/{diaryId}/replies")
public class AiReplyController {

    private final AiReplyService aiReplyService;

    public AiReplyController(AiReplyService aiReplyService) {
        this.aiReplyService = aiReplyService;
    }

    @PostMapping
    public ResultData<AiReplyVO> generate(@PathVariable Long diaryId,
                                           @RequestBody AiReplyRequest request,
                                           HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        AiReplyVO reply = aiReplyService.generateReply(diaryId, memberId, request.getReplyType());
        return ResultData.success("AI 답변이 생성되었습니다.", reply);
    }

    @GetMapping
    public ResultData<List<AiReplyVO>> list(@PathVariable Long diaryId,
                                             HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        List<AiReplyVO> replies = aiReplyService.getReplies(diaryId, memberId);
        return ResultData.success(replies);
    }
}
