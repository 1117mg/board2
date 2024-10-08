package com.study.board2.controller;

import com.study.board2.dto.Board;
import com.study.board2.util.LoginForm;
import com.study.board2.dto.Post;
import com.study.board2.dto.User;
import com.study.board2.service.BoardService;
import com.study.board2.service.PostService;
import com.study.board2.service.UserService;
import com.study.board2.util.MemberDetails;
import com.study.board2.util.Page;
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
    public String postList(@PathVariable("boardIdx") int boardIdx,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "searchType", required = false) String searchType,
                           Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);

        String boardType = boardService.getBoardType(boardIdx);
        int pageSize = 7;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = !(authentication instanceof AnonymousAuthenticationToken);
        Integer currentUserIdx = null;
        String userRole = null;

        if (isAuthenticated) {
            currentUserIdx = ((MemberDetails) authentication.getPrincipal()).getUser().getIdx();
            userRole = ((MemberDetails) authentication.getPrincipal()).getUser().getUserRole();
        }

        Page<Post> posts;
        if ("200".equals(boardType)) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                model.addAttribute("loginForm", new LoginForm());
                return "front/login";
            }
            posts = postService.getHPostsPageByBoardId(boardIdx, page, pageSize, keyword, searchType);
        } else {
            if (authentication instanceof AnonymousAuthenticationToken) {
                model.addAttribute("loginRequired", true);
            }
            posts = postService.getPostsPageByBoardId(boardIdx, page, pageSize, keyword, searchType);
        }

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", posts.getPageNumber());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("boardIdx", boardIdx);
        model.addAttribute("currentUserIdx", currentUserIdx);
        model.addAttribute("userRole", userRole);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);

        return "front/postList";
    }

    // 게시글 상세
    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable("postId") int postId, Model model) {
        // 로그인한 사용자 ID 가져오기
        String userId = userService.getloginUser();
        User user = userService.findByUserId(userId);

        // 로그인한 사용자 정보가 없으면 userNo를 0으로 설정
        int userNo = (user != null) ? user.getIdx() : 0;
        model.addAttribute("userNo", userNo);

        // 게시글 정보 가져오기
        Post post = postService.getPostById(postId);
        Post postInfo = postService.getHierarchy(postId);
        if (postInfo != null) {
            post.setDepth(postInfo.getDepth());
            post.setParentIdx(postInfo.getParentIdx());
        }
        post.setHits(postService.hit(postId));
        model.addAttribute("post", post);
        model.addAttribute("boardIdx", post.getBoardIdx());

        // 게시판 타입 정보 가져오기
        String boardType = boardService.getBoardType(post.getBoardIdx());
        model.addAttribute("boardType", boardType);

        // 게시글 작성자 정보 가져오기
        User writer = userService.findUserByIdx(post.getUserNo());
        User writerDetail = null;
        boolean hasEditPermission = false;
        boolean canReply = false;

        if (writer != null) {
            // 작성자의 역할에 따른 세부 정보 가져오기
            if ("ROLE_USER".equals(writer.getUserRole())) {
                writerDetail = userService.findMemberByIdx(writer.getIdx());
            } else {
                writerDetail = userService.findAdminByIdx(writer.getIdx());
            }
            model.addAttribute("writer", writerDetail);

            // 로그인 사용자와 작성자의 권한 비교
            if (user != null) {
                hasEditPermission = (writer.getIdx() == userNo || "ROLE_ADMIN".equals(user.getUserRole()));
            }
        } else {
            // writer가 null인 경우
            model.addAttribute("writer", null);
        }

        // 답글 작성 권한 체크
        canReply = (userNo != 0 && "200".equals(boardType) && !post.isNoticeYn());

        model.addAttribute("hasEditPermission", hasEditPermission);
        model.addAttribute("canReply", canReply);

        // 이전글 및 다음글 가져오기
        Integer prevPostId = postService.findPrevIdx(post.getBoardIdx(), postId);
        Integer nextPostId = postService.findNextIdx(post.getBoardIdx(), postId);
        Integer replyPostId = postService.findReplyIdx(post.getIdx());
        Integer parentPostId = null;
        if (post.getParentIdx() != null) {
            parentPostId = postService.findParentIdx(post.getParentIdx());
        }
        model.addAttribute("prevPostId", prevPostId);
        model.addAttribute("nextPostId", nextPostId);
        model.addAttribute("replyPostId", replyPostId);
        model.addAttribute("parentPostId", parentPostId);

        return "front/postDetail";
    }

    // 게시글 등록 폼
    @GetMapping("/{boardIdx}/post/write")
    public String writePostForm(@PathVariable("boardIdx") int boardIdx, Model model) {
        String userId= userService.getloginUser();
        User user=userService.findByUserId(userId);
        model.addAttribute("user", user);
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
    @ResponseBody
    public ResponseEntity<Map<String, String>> replyPost(@PathVariable("boardIdx") int boardIdx,
                                                         @PathVariable("postId") int postId,
                                                         @ModelAttribute Post post) {
        Map<String, String> response = new HashMap<>();

        // 유효성 검사
        if (post.getTitle() == null || post.getTitle().trim().isEmpty() ||
                post.getContent() == null || post.getContent().trim().isEmpty() ||
                post.getUploadDate() == null) {
            response.put("error", "제목, 내용, 등록일자는 필수 입력 항목입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 게시글 설정
            post.setBoardIdx(boardIdx);
            post.setParentIdx(postId);

            // 부모 게시글의 존재 여부 확인
            Post parentPost = postService.getPostByParentId(post.getParentIdx());
            if (parentPost == null) {
                response.put("error", "부모 게시글이 존재하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 부모 게시글의 깊이 가져오기
            post.setDepth(parentPost.getDepth() + 1);

            // 답글 저장
            postService.replyPost(post);

            response.put("message", "답글 작성 완료!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "작성 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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