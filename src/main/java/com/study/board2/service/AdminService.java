package com.study.board2.service;

import com.study.board2.repository.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminMapper adminMapper;

    public int getRecentJoinedUsers(){
        return adminMapper.findRecentJoinedUsers();
    }
}
