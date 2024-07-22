package com.study.board2.repository;

import com.study.board2.dto.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> findByBoardIdx(@Param("boardIdx") int boardIdx);
    Post findByIdx(@Param("idx") int idx);
    void insertPost(Post post);
    void updatePost(Post post);
    void deletePost(@Param("idx") int idx);
}