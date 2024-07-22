package com.study.board2.controller;

import com.study.board2.dto.Board;
import com.study.board2.dto.Post;
import com.study.board2.service.BoardService;
import com.study.board2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/front/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;

    // 게시판 카테고리 목록
    @GetMapping("/list")
    public String boardList(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "front/boardList";
    }

    // 게시판 글 목록
    @GetMapping("/{boardIdx}/posts")
    public String postList(@PathVariable("boardIdx") int boardIdx, Model model) {
        List<Post> posts = postService.getPostsByBoardId(boardIdx);
        model.addAttribute("posts", posts);
        return "front/postList";
    }

    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable("postId") int postId, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "front/postDetail";
    }

    @GetMapping("/post/write")
    public String writePostForm(Model model) {
        model.addAttribute("post", new Post());
        return "front/writePost";
    }

    @PostMapping("/post/write")
    public String writePost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/front/board/" + post.getBoardIdx() + "/posts";
    }

    @GetMapping("/post/{postId}/edit")
    public String editPostForm(@PathVariable("postId") int postId, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "front/editPost";
    }

    @PostMapping("/post/{postId}/edit")
    public String editPost(@PathVariable("postId") int postId, @ModelAttribute Post post) {
        post.setIdx(postId);
        postService.updatePost(post);
        return "redirect:/front/board/" + post.getBoardIdx() + "/posts";
    }

    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") int postId) {
        Post post = postService.getPostById(postId);
        postService.deletePost(postId);
        return "redirect:/front/board/" + post.getBoardIdx() + "/posts";
    }
}