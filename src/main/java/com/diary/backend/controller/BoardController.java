package com.diary.backend.controller;

import com.diary.backend.dto.request.BoardCreateRequest;
import com.diary.backend.dto.request.BoardUpdateRequest;
import com.diary.backend.dto.response.BoardResponse;
import com.diary.backend.dto.response.ResultData;
import com.diary.backend.interceptor.AuthInterceptor;
import com.diary.backend.service.BoardService;
import com.diary.backend.vo.Emotion;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResultData<BoardResponse> create(@RequestBody BoardCreateRequest request,
                                            HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        BoardResponse board = boardService.createBoard(memberId, request);
        return ResultData.success("게시글이 작성되었습니다.", board);
    }

    @GetMapping
    public ResultData<List<BoardResponse>> list(
            @RequestParam(required = false) Emotion emotion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<BoardResponse> boards = boardService.getBoardList(emotion, page, size);
        return ResultData.success(boards);
    }

    @GetMapping("/me")
    public ResultData<List<BoardResponse>> myBoards(HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        List<BoardResponse> boards = boardService.getMyBoards(memberId);
        return ResultData.success(boards);
    }

    @GetMapping("/{id}")
    public ResultData<BoardResponse> detail(@PathVariable Long id) {
        BoardResponse board = boardService.getBoard(id);
        return ResultData.success(board);
    }

    @PutMapping("/{id}")
    public ResultData<BoardResponse> update(@PathVariable Long id,
                                            @RequestBody BoardUpdateRequest request,
                                            HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        BoardResponse board = boardService.updateBoard(id, memberId, request);
        return ResultData.success("게시글이 수정되었습니다.", board);
    }

    @DeleteMapping("/{id}")
    public ResultData<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long memberId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_ATTRIBUTE);
        boardService.deleteBoard(id, memberId);
        return ResultData.success("게시글이 삭제되었습니다.", null);
    }
}
