package com.study.board2.repository;

import com.study.board2.util.UpdateStatusRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    int countRecentJoin();
    int countTodayLogins();
    void insertLoginHistory(int userIdx);
    void updateUseYn(UpdateStatusRequest request);
}
