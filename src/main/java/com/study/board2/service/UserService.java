package com.study.board2.service;

import com.study.board2.dto.JoinForm;
import com.study.board2.dto.User;
import com.study.board2.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void register(User user) {
        // User 테이블에 데이터 삽입
        userMapper.insertUser(user);

        // 삽입된 User의 ID 가져오기
        int userId = user.getIdx();

        // UserRole에 따라 Member 또는 Admin 테이블에 데이터 삽입
        if ("ROLE_USER".equals(user.getUserRole())) {
            user.setIdx(userId);
            userMapper.insertMember(user);
        } else if ("ROLE_ADMIN".equals(user.getUserRole())) {
            user.setIdx(userId);
            userMapper.insertAdmin(user);
        }
    }

}
