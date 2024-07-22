package com.study.board2.controller;

import com.study.board2.dto.JoinForm;
import com.study.board2.dto.LoginForm;
import com.study.board2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/front")
@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "front/login";
    }

    @GetMapping("/auth/join")
    public String joinForm(Model model){
        model.addAttribute("joinForm",new JoinForm());
        return "front/join";
    }

    @PostMapping("/auth/join")
    public String join(JoinForm form){
        userService.register(form);
        return "redirect:/front/auth/login";
    }

    @GetMapping("/main")
    public String main(){
        return "front/main";
    }

}
