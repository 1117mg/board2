package com.study.board2.service;

import com.study.board2.util.UpdateStatusRequest;
import com.study.board2.repository.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminMapper adminMapper;

    // 7일 내 가입한 회원 수 조회
    public int getRecentJoinedUsers(){
        return adminMapper.countRecentJoin();
    }

    // 오늘 로그인한 회원 수 조회
    public int getTodayLogin(){
        return adminMapper.countTodayLogins();
    }

    // 로그인 기록 저장
    public void recordLogin(int userIdx){
        adminMapper.insertLoginHistory(userIdx);
    }

    @Transactional
    public void updateAdminStatus(UpdateStatusRequest request) {
        adminMapper.updateUseYn(request);
    }
}
