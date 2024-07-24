package com.study.board2.repository;

import com.study.board2.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAllMembers();
    User findByUserId(String userId);
    User findByIdx(int idx);
    void insertUser(User user);
    void insertMember(User user);
    void insertAdmin(User user);
}
