package com.study.board2.service;

import com.study.board2.dto.*;
import com.study.board2.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public List<User> getAllMembers() {
        return userMapper.findAllMembers();
    }

    public List<User> getAllAdmins() {
        return userMapper.findAllAdmins();
    }

    public User findByUserId(String userName){
        return userMapper.findByUserId(userName);
    }

    public User findByUsername(String userName){
        return userMapper.findByUsername(userName);
    }

    public User findMemberByIdx(int idx){
        return userMapper.findMemberByIdx(idx);
    }

    public User findAdminByIdx(int idx){
        return userMapper.findAdminByIdx(idx);
    }

    @Transactional
    public void register(User user) {
        userMapper.insertUser(user);

        int userId = user.getIdx();

        if ("ROLE_USER".equals(user.getUserRole())) {
            user.setIdx(userId);
            userMapper.insertMember(user);
        } else if ("ROLE_ADMIN".equals(user.getUserRole())) {
            user.setIdx(userId);
            userMapper.insertAdmin(user);
        }
    }

    public String getloginUser(){
        try {
            Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();
            return loginUser.getName();
        }catch (NullPointerException e){
            return "익명 사용자";
        }
    }

    public void updateMember(User user){
        userMapper.updateMember(user);
    }

    public void updateAdmin(User user){
        userMapper.updateAdmin(user);
    }
}
