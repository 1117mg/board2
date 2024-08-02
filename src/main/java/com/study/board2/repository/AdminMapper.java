package com.study.board2.repository;

import com.study.board2.dto.UpdateStatusRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    int countRecentJoin();
    int countTodayLogins();
    void insertLoginHistory(int userIdx);
    void updateUseYn(UpdateStatusRequest request);
}
