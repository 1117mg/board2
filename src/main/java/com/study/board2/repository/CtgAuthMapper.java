package com.study.board2.repository;

import com.study.board2.dto.CtgAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CtgAuthMapper {
    List<CtgAuth> selectCtgAuthByUserId(int userId);
    void deleteAllByUserId(int userId);
    void insertCtgAuth(CtgAuth ctgAuth);
    void updateCtgAuth(CtgAuth ctgAuth);
    boolean existsByUserIdAndBoardId(@Param("userId") int userId, @Param("boardId") int boardId);
}
