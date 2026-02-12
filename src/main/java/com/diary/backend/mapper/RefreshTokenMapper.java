package com.diary.backend.mapper;

import com.diary.backend.vo.RefreshTokenVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RefreshTokenMapper {

    RefreshTokenVO findByMemberId(@Param("memberId") Long memberId);

    RefreshTokenVO findByToken(@Param("token") String token);

    void upsert(RefreshTokenVO refreshToken);

    void deleteByMemberId(@Param("memberId") Long memberId);

    void deleteExpiredTokens();
}
