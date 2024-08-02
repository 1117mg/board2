package com.study.board2.repository;

import com.study.board2.dto.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> findByBoardIdx(@Param("boardIdx") int boardIdx,
                              @Param("offset") int offset,
                              @Param("pageSize") int pageSize);
    List<Post> findHByBoardIdx(@Param("boardIdx") int boardIdx,
                               @Param("offset") int offset,
                               @Param("pageSize") int pageSize);
    int countPostsByBoardId(@Param("boardIdx") int boardIdx);
    Post findByIdx(@Param("idx") int idx);
    Post findByParentId(@Param("parentIdx") int parentIdx);
    void insertPost(Post post);
    void replyPost(Post post);
    void updatePost(Post post);
    void deletePost(@Param("idx") int idx);
    int hit(@Param("idx") int idx);
}