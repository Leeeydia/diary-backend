package com.diary.backend.mapper;

import com.diary.backend.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    void insert(MemberVO member);

    MemberVO findByUsername(@Param("username") String username);

    MemberVO findById(@Param("id") Long id);

    int existsByEmail(@Param("email") String email);

    int existsByUsername(@Param("username") String username);

    void updateProfileImage(@Param("id") Long id,
                            @Param("profileImageUrl") String profileImageUrl);

    void updateReplyMode(@Param("id") Long id,
                         @Param("replyMode") String replyMode);

    void updateUsername(@Param("id") Long id,
                        @Param("username") String username);
}