package com.diary.backend.service;

import com.diary.backend.dto.request.BoardCreateRequest;
import com.diary.backend.dto.request.BoardUpdateRequest;
import com.diary.backend.dto.response.BoardResponse;
import com.diary.backend.exception.CustomException;
import com.diary.backend.exception.ErrorCode;
import com.diary.backend.mapper.BoardMapper;
import com.diary.backend.vo.BoardVO;
import com.diary.backend.vo.Emotion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Transactional
    public BoardResponse createBoard(Long memberId, BoardCreateRequest request) {
        BoardVO board = BoardVO.builder()
                .memberId(memberId)
                .title(request.getTitle())
                .content(request.getContent())
                .emotion(request.getEmotion())
                .build();

        boardMapper.insert(board);
        return toResponse(boardMapper.findById(board.getId()));
    }

    public List<BoardResponse> getBoardList(Emotion emotion, int page, int size) {
        int offset = page * size;
        return boardMapper.findAllWithEmotion(emotion, size, offset).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BoardResponse getBoard(Long boardId) {
        BoardVO board = boardMapper.findById(boardId);
        if (board == null) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }
        return toResponse(board);
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, Long memberId, BoardUpdateRequest request) {
        BoardVO board = boardMapper.findById(boardId);
        if (board == null) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }
        if (!board.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.BOARD_ACCESS_DENIED);
        }

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setEmotion(request.getEmotion());

        boardMapper.update(board);
        return toResponse(boardMapper.findById(boardId));
    }

    @Transactional
    public void deleteBoard(Long boardId, Long memberId) {
        BoardVO board = boardMapper.findById(boardId);
        if (board == null) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }
        if (!board.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.BOARD_ACCESS_DENIED);
        }
        boardMapper.softDeleteById(boardId);
    }

    private BoardResponse toResponse(BoardVO board) {
        return BoardResponse.builder()
                .id(board.getId())
                .memberId(board.getMemberId())
                .title(board.getTitle())
                .content(board.getContent())
                .emotion(board.getEmotion())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
