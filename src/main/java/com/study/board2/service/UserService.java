package com.study.board2.service;

import com.study.board2.dto.*;
import com.study.board2.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean userEmailCheck(String userEmail, String userName) {

        User user = userMapper.findByUsername(userName);
        if(user!=null && user.getUserEmail().equals(userEmail)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void updateUserPassword(String userEmail, String newPassword) {
        User user = userMapper.findByEmail(userEmail);
        if (user != null) {
            user.setUserPw(passwordEncoder.encode(newPassword));
            userMapper.updateUserPassword(user);
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
    }

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
