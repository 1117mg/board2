package com.study.board2.controller;

import com.study.board2.dto.JoinForm;
import com.study.board2.dto.LoginForm;
import com.study.board2.dto.User;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/front")
@Controller
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String loginForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(authentication));
        if (authentication instanceof AnonymousAuthenticationToken){
            model.addAttribute("loginForm", new LoginForm());
            return "front/login";}
        return "front/main";
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
        model.addAttribute("joinForm",new JoinForm());
        return "front/join";
    }

    @PostMapping("/auth/join")
    public String join(JoinForm form) {
        User user = User.builder()
                .userId(form.getLoginId())
                .userPw(passwordEncoder.encode(form.getLoginPw()))
                .userName(form.getUserName())
                .userEmail(form.getUserEmail())
                .userRole("ROLE_USER")
                .build();
        userService.register(user);
        return "redirect:/front/auth/login";
    }

    @GetMapping("/main")
    public String main(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            log.info("Authenticated user: " + username);
            model.addAttribute("username", username);
        }
        return "front/main";
    }

    // 게시판 카테고리 목록
    @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = userService.getAllMembers();
        model.addAttribute("users", users);
        return "front/userList";
    }

    @GetMapping("/user/{idx}")
    public String userDetail(@PathVariable("idx") int idx, Model model){
        User user=userService.findByIdx(idx);
        model.addAttribute("user",user);
        return "front/userDetail";
    }

}