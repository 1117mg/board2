package com.study.board2.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    int findRecentJoinedUsers();
}
