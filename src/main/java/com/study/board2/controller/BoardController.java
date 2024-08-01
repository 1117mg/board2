package com.study.board2.controller;

import com.study.board2.dto.LoginForm;
import com.study.board2.dto.Post;
import com.study.board2.dto.User;
import com.study.board2.service.BoardService;
import com.study.board2.service.PostService;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/front/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;
    private final UserService userService;

    // 게시판 글 목록
    @GetMapping("/{boardIdx}/posts")
    public String postList(@PathVariable("boardIdx") int boardIdx, Model model, HttpServletRequest request) {
        String boardType = boardService.getBoardType(boardIdx);

        // board_type이 200이면 계층형 게시판
        if ("200".equals(boardType)) {
            // 1:1 게시판은 비로그인 시 접근 불가
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                model.addAttribute("loginForm", new LoginForm());
                model.addAttribute("loginRequired", true);
                return "front/postList";
            }
            List<Post> hposts = postService.getHPostsByBoardId(boardIdx);
            model.addAttribute("posts", hposts);
        } else {
            // 공지사항 게시판(board_type=="100")
            List<Post> posts = postService.getPostsByBoardId(boardIdx);
            model.addAttribute("posts", posts);
        }

        return "front/postList";
    }

    // 게시글 상세
    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable("postId") int postId, Model model) {
        String userId = userService.getloginUser();
        User user = userService.findByUserId(userId);

        int userNo = (user != null) ? user.getIdx() : 0;
        model.addAttribute("userNo", userNo);

        Post post = postService.getPostById(postId);
        post.setHits(postService.hit(postId));
        model.addAttribute("post", post);
        model.addAttribute("boardIdx", post.getBoardIdx());

        // 권한 체크
        boolean hasEditPermission = (post.getUserNo() == userNo);
        model.addAttribute("hasEditPermission", hasEditPermission);

        // 답글 작성 권한 체크
        boolean canReply = (userNo != 0);
        model.addAttribute("canReply", canReply);

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
    @ResponseBody
    public ResponseEntity<Map<String, Object>> writePost(
            @PathVariable("boardIdx") int boardIdx,
            @ModelAttribute Post post) {

        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Received Post: " + post);

            post.setBoardIdx(boardIdx);
            postService.createPost(post);

            response.put("status", "success");
            response.put("message", "게시글이 성공적으로 작성되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", "게시글 작성에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 답글 등록 폼
    @GetMapping("/{boardIdx}/post/{postId}/reply")
    public String replyPostForm(@PathVariable("boardIdx") int boardIdx, @PathVariable("postId") int postId, Model model){
        String userId= userService.getloginUser();
        User user=userService.findByUserId(userId);
        model.addAttribute("userId",userId);
        Post post = new Post();
        post.setBoardIdx(boardIdx);
        post.setParentIdx(postId);
        if(user!=null){
            post.setUserNo(user.getIdx());
        }
        model.addAttribute("post", post);
        return "front/replyPost";
    }

    // 답글 등록
    @PostMapping("/{boardIdx}/post/{postId}/reply")
    public String replyPost(@PathVariable("boardIdx") int boardIdx, @PathVariable("postId") int postId, @ModelAttribute Post post) {
        post.setBoardIdx(boardIdx);
        post.setParentIdx(postId);
        Post parentPost=postService.getPostByParentId(post.getParentIdx());
        post.setDepth(parentPost.getDepth()+1);
        postService.replyPost(post);
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
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editPost(
            @PathVariable("boardIdx") int boardIdx,
            @PathVariable("postId") int postId,
            @ModelAttribute Post post) {

        Map<String, Object> response = new HashMap<>();

        try {
            post.setIdx(postId);
            post.setBoardIdx(boardIdx);
            postService.updatePost(post);

            response.put("status", "success");
            response.put("message", "게시글이 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", "게시글 수정에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
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