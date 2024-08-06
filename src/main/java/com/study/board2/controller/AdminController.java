package com.study.board2.controller;

import com.study.board2.dto.*;
import com.study.board2.service.AdminService;
import com.study.board2.service.BoardService;
import com.study.board2.service.CtgAuthService;
import com.study.board2.service.UserService;
import com.study.board2.util.JoinForm;
import com.study.board2.util.LoginForm;
import com.study.board2.util.Page;
import com.study.board2.util.UpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/master")
@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final CtgAuthService ctgAuthService;
    private final BoardService boardService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String loginForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(authentication));
        if (authentication instanceof AnonymousAuthenticationToken){
            model.addAttribute("loginForm", new LoginForm());
            return "master/login";}
        return "redirect:/master/main";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/master/main";
    }

    @GetMapping("/auth/join")
    public String joinForm(Model model){
        model.addAttribute("joinForm", new JoinForm());
        return "master/join";
    }

    @PostMapping("/auth/join")
    public String join(JoinForm form,Model model){
        if(userService.findByUserId(form.getLoginId())!=null){
            model.addAttribute("IdDuplicate",true);
        }
        User user = User.builder()
                .userId(form.getLoginId())
                .userPw(passwordEncoder.encode(form.getLoginPw()))
                .userName(form.getUserName())
                .userEmail(form.getUserEmail())
                .userRole("ROLE_ADMIN")
                .build();
        userService.register(user);
        return "redirect:/master/auth/login";
    }

    @GetMapping("/main")
    public String main(Model model){
        // 최근 7일동안 가입한 회원 수
        int recentJoins = adminService.getRecentJoinedUsers();
        // 오늘 로그인한 회원 수
        int todayLogins = adminService.getTodayLogin();
        model.addAttribute("weekJoin",recentJoins);
        model.addAttribute("todayLogin", todayLogins);
        return "master/main";
    }

    @GetMapping("/users")
    public String adminList(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 7;
        Page<User> users = userService.getAllAdmins(page, pageSize);
        model.addAttribute("users", users.getContent());
        model.addAttribute("currentPage", users.getPageNumber());
        model.addAttribute("totalPages", users.getTotalPages());
        return "master/adminList";
    }

    @PostMapping("/user/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody UpdateStatusRequest request) {
        try {
            adminService.updateAdminStatus(request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{idx}")
    public String adminDetail(@PathVariable("idx") int idx, Model model) {
        User user = userService.findAdminByIdx(idx);
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);

        List<CtgAuth> ctgAuthList = ctgAuthService.getCtgAuthForUser(idx);

        Map<Integer, CtgAuth> readMap = new HashMap<>();

        for (CtgAuth auth : ctgAuthList) {
            readMap.put(auth.getBoardId(), auth);
        }

        for (Board board : boards) {
            if (!readMap.containsKey(board.getIdx())) {
                // 권한이 없으면 기본값으로 CtgAuth 객체를 생성
                CtgAuth defaultAuth = new CtgAuth();
                defaultAuth.setUserId(idx);
                defaultAuth.setBoardId(board.getIdx());
                defaultAuth.setCanRead(false);  // 기본값으로 false 설정
                defaultAuth.setCanWrite(false); // 기본값으로 false 설정
                defaultAuth.setCanDownload(false); // 기본값으로 false 설정

                // 기본값으로 설정된 CtgAuth를 맵에 추가
                readMap.put(board.getIdx(), defaultAuth);
            }
        }

        model.addAttribute("readMap", readMap);
        model.addAttribute("user", user);
        return "master/adminDetail";
    }

    @PostMapping("/user/{idx}")
    public String adminUpdate(@PathVariable("idx") int idx, @ModelAttribute("user") User user, @RequestParam Map<String, String> params){
        userService.updateAdmin(user);
        // 권한 업데이트
        ctgAuthService.updatePermissions(idx, params);
        return "redirect:/master/user/"+user.getIdx();
    }

    @GetMapping("/user/new")
    public String adminNewForm(Model model) {
        model.addAttribute("joinForm", new JoinForm());
        return "master/adminNew";
    }

    @PostMapping("/user/new")
    public ResponseEntity<Map<String, String>> adminNew(@Valid JoinForm form, BindingResult result) {
        if (result.hasErrors()) {
            // 폼 입력 값에 문제가 있을 경우
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "입력 값이 유효하지 않습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            User user = User.builder()
                    .userId(form.getLoginId())
                    .userPw(passwordEncoder.encode(form.getLoginPw()))
                    .userName(form.getUserName())
                    .userEmail(form.getUserEmail())
                    .userRole("ROLE_ADMIN")
                    .build();
            userService.register(user);

            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "/master/user/" + user.getIdx());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 사용자 등록 중 예외 발생 시 처리
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "사용자 등록 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "서버 오류가 발생했습니다: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
