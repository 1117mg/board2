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
        return postMapper.findByIdx(postId);
    }

    public Post getPostByParentId(int parentId){
        return postMapper.findByParentId(parentId);
    }

    public int hit(int postId) {
        return postMapper.hit(postId);
    }

    public void createPost(Post post) {
        postMapper.insertPost(post);
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