package com.diary.backend.mapper;

import com.diary.backend.vo.DiaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DiaryMapper {

    void insert(DiaryVO diary);

    DiaryVO findById(@Param("id") Long id);

    List<DiaryVO> findByUserId(@Param("userId") Long userId);

    List<DiaryVO> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    void update(DiaryVO diary);

    void deleteById(@Param("id") Long id);
}
