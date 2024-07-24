package com.study.board2.service;

import com.study.board2.dto.Post;
import com.study.board2.repository.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    public List<Post> getPostsByBoardId(int boardIdx) {
        return postMapper.findByBoardIdx(boardIdx);
    }

    public List<Post> getQPostsByBoardId(int boardIdx) {
        return postMapper.findQByBoardIdx(boardIdx);
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