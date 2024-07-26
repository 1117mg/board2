package com.study.board2.controller;

import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;

    @GetMapping("/checkUserId")
    public Map<String, Object> checkUserId(@RequestParam("userId") String userId) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = userService.findByUserId(userId) != null;
        response.put("result", exists ? 1 : 0); // 1: 중복, 0: 사용 가능
        return response;
    }

    @GetMapping("/checkUsername")
    public Map<String, Object> checkUsername(@RequestParam("username") String username) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = userService.findByUsername(username) != null;
        response.put("result", exists ? 1 : 0); // 1: 중복, 0: 사용 가능
        return response;
    }
}