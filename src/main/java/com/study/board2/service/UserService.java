package com.study.board2.service;

import com.study.board2.dto.JoinForm;
import com.study.board2.dto.User;
import com.study.board2.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public User findByUserId(String userName){
        return userMapper.findByUserId(userName);
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

}
