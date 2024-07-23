package com.study.board2.controller;

import com.study.board2.dto.Board;
import com.study.board2.dto.Post;
import com.study.board2.dto.User;
import com.study.board2.service.BoardService;
import com.study.board2.service.PostService;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/front/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;
    private final UserService userService;

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
        if(boardIdx==1){    // 공지사항 게시판
            List<Post> posts = postService.getPostsByBoardId(boardIdx);
            model.addAttribute("posts", posts);
        }else{              // 1:1 문의 게시판
            List<Post> qposts = postService.getQPostsByBoardId(boardIdx);
            model.addAttribute("posts", qposts);
        }

        return "front/postList";
    }

    // 게시글 상세
    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable("postId") int postId, Model model) {
        String userId= userService.getloginUser();
        User user=userService.findByUserId(userId);
        if(user!=null){
            model.addAttribute("userNo", user.getIdx());
        }else{
            model.addAttribute("userNo", 0);
        }
        Post post = postService.getPostById(postId);
        post.setHits(postService.hit(postId));
        model.addAttribute("post", post);
        model.addAttribute("boardIdx", post.getBoardIdx());
        return "front/postDetail";
    }

    // 게시글 등록 폼
    @GetMapping("/{boardIdx}/post/write")
    public String writePostForm(@PathVariable("boardIdx") int boardIdx, Model model) {
        String userId= userService.getloginUser();
        User user=userService.findByUserId(userId);
        model.addAttribute("userId",userId);
        Post post = new Post();
        post.setBoardIdx(boardIdx);
        if(user!=null){
            post.setUserNo(user.getIdx());
        }
        model.addAttribute("post", post);
        return "front/writePost";
    }

    // 게시글 등록
    @PostMapping("/{boardIdx}/post/write")
    public String writePost(@PathVariable("boardIdx") int boardIdx, @ModelAttribute Post post) {
        post.setBoardIdx(boardIdx);
        postService.createPost(post);
        return "redirect:/front/board/" + boardIdx + "/posts";
    }

    // 게시글 수정 폼
    @GetMapping("/{boardIdx}/post/{postId}/edit")
    public String editPostForm(@PathVariable("boardIdx") int boardIdx, @PathVariable("postId") int postId, Model model) {
        String userId= userService.getloginUser();
        model.addAttribute("userId",userId);
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        model.addAttribute("boardIdx", boardIdx);
        return "front/editPost";
    }

    // 게시글 수정
    @PostMapping("/{boardIdx}/post/{postId}/edit")
    public String editPost(@PathVariable("boardIdx") int boardIdx, @PathVariable("postId") int postId, @ModelAttribute Post post) {
        post.setIdx(postId);
        post.setBoardIdx(boardIdx);
        postService.updatePost(post);
        return "redirect:/front/board/" + boardIdx + "/posts";
    }

    // 게시글 삭제
    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") int postId) {
        Post post = postService.getPostById(postId);
        int boardIdx = post.getBoardIdx();
        postService.deletePost(postId);
        return "redirect:/front/board/" + boardIdx + "/posts";
    }
}