package com.study.board2.controller;

import com.study.board2.dto.JoinForm;
import com.study.board2.dto.LoginForm;
import com.study.board2.dto.User;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/master")
@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "master/login";
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
    public String join(JoinForm form){
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            String username= authentication.getName();
            log.info(username);
            model.addAttribute("username", username);
        }
        return "master/main";
    }
}
