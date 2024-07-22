package com.study.board2.service;

import com.study.board2.dto.MemberDetails;
import com.study.board2.dto.User;
import com.study.board2.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user =mapper.findByUserId(userId);

        if (user == null) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + userId);
        }

        return new MemberDetails(user);
    }
}
