package com.study.board2.service;

import com.study.board2.dto.JoinForm;
import com.study.board2.dto.User;
import com.study.board2.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(JoinForm form){
        User user=new User();
        user.setUserId(form.getLoginId());
        user.setUserPw(passwordEncoder.encode(form.getLoginPw()));
        user.setUserName(form.getUserName());
        user.setUserEmail(form.getUserEmail());
        user.setUserRole("ROLE_USER");
        userMapper.insertUser(user);
    }

}
