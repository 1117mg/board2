package com.study.board2.repository;

import com.study.board2.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAllMembers(@Param("offset") int offset,
                              @Param("pageSize") int pageSize);
    List<User> findAllAdmins(@Param("offset") int offset,
                             @Param("pageSize") int pageSize);
    int countMembers();
    int countAdmins();
    User findByUserId(String userId);
    User findByUsername(String userName);
    User findByEmail(String userEmail);
    User findMemberByIdx(int idx);
    User findAdminByIdx(int idx);
    void insertUser(User user);
    void insertMember(User user);
    void insertAdmin(User user);
    void updateMember(User user);
    void updateAdmin(User user);
    void updateUserPassword(User user);
}
