package com.diary.backend.mapper;

import com.diary.backend.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void insert(UserVO user);

    UserVO findByEmail(@Param("email") String email);

    UserVO findById(@Param("id") Long id);

    int existsByEmail(@Param("email") String email);
}
