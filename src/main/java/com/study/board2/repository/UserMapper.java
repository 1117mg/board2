package com.study.board2.repository;

import com.study.board2.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUserId(String userId);
}
