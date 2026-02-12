package com.diary.backend.mapper;

import com.diary.backend.vo.AiReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiReplyMapper {

    void insert(AiReplyVO aiReply);

    List<AiReplyVO> findByDiaryId(@Param("diaryId") Long diaryId);

    AiReplyVO findByDiaryIdAndType(@Param("diaryId") Long diaryId, @Param("replyType") String replyType);

    void deleteByDiaryId(@Param("diaryId") Long diaryId);
}
