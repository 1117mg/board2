package com.study.board2.repository;

import com.study.board2.dto.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    List<Post> findPosts(@Param("boardIdx") int boardIdx,
                         @Param("keyword") String keyword,
                         @Param("searchType") String searchType,
                         @Param("offset") int offset,
                         @Param("pageSize") int pageSize);

    int countPosts(@Param("boardIdx") int boardIdx,
                   @Param("keyword") String keyword,
                   @Param("searchType") String searchType);

    List<Post> findHPosts(@Param("boardIdx") int boardIdx,
                          @Param("keyword") String keyword,
                          @Param("searchType") String searchType,
                          @Param("offset") int offset,
                          @Param("pageSize") int pageSize);
    Post findPostByIdx(@Param("idx") int idx);
    Post findHierarchy(int postId);
    Integer findPrevIdx(int boardIdx, int postId);
    Integer findNextIdx(int boardIdx, int postId);
    Post findByParentId(@Param("parentIdx") int parentIdx);
    Integer findParentIdx(int parentIdx);
    Integer findReplyIdx(int idx);
    void insertPost(Post post);
    void replyPost(Post post);
    void updatePost(Post post);
    void deletePost(@Param("idx") int idx);
    int hit(@Param("idx") int idx);
}