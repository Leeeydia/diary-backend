package com.diary.backend.mapper;

import com.diary.backend.vo.DiaryVO;
import com.diary.backend.vo.Emotion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiaryMapper {

    void insert(DiaryVO diary);

    DiaryVO findById(@Param("id") Long id);

    List<DiaryVO> findByMemberId(@Param("memberId") Long memberId);

    List<DiaryVO> findByMemberIdWithEmotion(@Param("memberId") Long memberId, @Param("emotion") Emotion emotion);

    void update(DiaryVO diary);

    void softDeleteById(@Param("id") Long id);
}
