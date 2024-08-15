package com.study.board2.service;

import com.study.board2.dto.Post;
import com.study.board2.repository.PostMapper;
import com.study.board2.util.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final BoardService boardService;

    public Page<Post> getPostsPageByBoardId(int boardIdx, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        List<Post> posts = postMapper.findByBoardIdx(boardIdx, offset, pageSize);
        int totalElements = postMapper.countPostsByBoardId(boardIdx);

        return new Page<>(posts, pageNumber, pageSize, totalElements);
    }

    public Page<Post> getHPostsPageByBoardId(int boardIdx, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        List<Post> posts = postMapper.findHByBoardIdx(boardIdx, offset, pageSize);
        int totalElements = postMapper.countPostsByBoardId(boardIdx);

        return new Page<>(posts, pageNumber, pageSize, totalElements);
    }

    public Post getPostById(int postId) {
        return postMapper.findPostByIdx(postId);
    }

    public Post getHierarchy(int postId){
        return postMapper.findHierarchy(postId);
    }

    public Integer findPrevIdx(int boardIdx, int postId) {
        return postMapper.findPrevIdx(boardIdx, postId);
    }

    public Integer findNextIdx(int boardIdx, int postId) {
        return postMapper.findNextIdx(boardIdx, postId);
    }

    public Post getPostByParentId(int parentId){
        return postMapper.findByParentId(parentId);
    }

    public Integer findParentIdx(int parentIdx){
        return postMapper.findParentIdx(parentIdx);
    }

    public Integer findReplyIdx(int idx){
        return postMapper.findReplyIdx(idx);
    }

    public int hit(int postId) {
        return postMapper.hit(postId);
    }

    public void createPost(Post post) {
        postMapper.insertPost(post);

        // 계층형 게시판의 경우, 최상위 부모 글로 추가
        if ("200".equals(boardService.getBoardType(post.getBoardIdx()))) {
            post.setParentIdx(null);
            post.setDepth(0);
            postMapper.replyPost(post);  // 계층형 게시판 테이블에 삽입
        }
    }

    public void replyPost(Post post) {
        postMapper.insertPost(post);
        postMapper.replyPost(post);
    }

    public void updatePost(Post post) {
        postMapper.updatePost(post);
    }

    public void deletePost(int postId) {
        postMapper.deletePost(postId);
    }
}