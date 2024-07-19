package com.study.board2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/master")
@Controller
@Slf4j
public class AdminController {

    @GetMapping("/auth/login")
    public String loginForm(){
        return "master/login";
    }

    @PostMapping("/auth/login")
    public String login(){
        return "master/main";
    }

    @GetMapping("/main")
    public String main(){
        return "master/main";
    }
}
