package com.diary.backend.mapper;

import com.diary.backend.dto.response.EmotionStatsResponse;
import com.diary.backend.vo.EmotionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmotionMapper {

    void insert(EmotionVO emotion);

    void insertBatch(@Param("list") List<EmotionVO> emotions);

    List<EmotionVO> findByDiaryId(@Param("diaryId") Long diaryId);

    void deleteByDiaryId(@Param("diaryId") Long diaryId);

    List<EmotionStatsResponse> getStatsByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
