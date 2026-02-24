package com.diary.backend.mapper;

import com.diary.backend.vo.BoardVO;
import com.diary.backend.vo.Emotion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    void insert(BoardVO board);

    BoardVO findById(@Param("id") Long id);

    List<BoardVO> findAllWithEmotion(@Param("emotion") Emotion emotion,
                                     @Param("size") int size,
                                     @Param("offset") int offset);

    void update(BoardVO board);

    void softDeleteById(@Param("id") Long id);
}
