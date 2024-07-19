package com.study.board2.controller;

import com.study.board2.dto.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/front")
@Controller
@Slf4j
public class UserController {

    @RequestMapping("/test")
    public String test(Model model){
        String name="이름";
        model.addAttribute("name", name);
        log.info(name);
        return "test";
    }

    @GetMapping("/auth/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "front/login";
    }

    @PostMapping("/auth/login")
    public String login(@ModelAttribute("loginForm") LoginForm form){
        return "front/main";
    }

    @GetMapping("/main")
    public String main(){
        return "front/main";
    }

}
