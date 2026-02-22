package com.diary.backend.aireply;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiReplyMapper {

    void insert(AiReplyVO aiReply);

    AiReplyVO findByDiaryId(@Param("diaryId") Long diaryId);

    void deleteByDiaryId(@Param("diaryId") Long diaryId);
}
