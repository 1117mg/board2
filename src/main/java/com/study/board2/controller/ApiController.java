package com.study.board2.controller;

import com.study.board2.dto.Mail;
import com.study.board2.service.SendEmailService;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final SendEmailService sendEmailService;

    // 회원가입 아이디 중복확인
    @GetMapping("/checkUserId")
    public Map<String, Object> checkUserId(@RequestParam("userId") String userId) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = userService.findByUserId(userId) != null;
        response.put("result", exists ? 1 : 0); // 1: 중복, 0: 사용 가능
        return response;
    }

    // 회원가입 이름 중복확인
    @GetMapping("/checkUsername")
    public Map<String, Object> checkUsername(@RequestParam("username") String username) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = userService.findByUsername(username) != null;
        response.put("result", exists ? 1 : 0); // 1: 중복, 0: 사용 가능
        return response;
    }

    // 비밀번호 찾기
    @GetMapping("/findPassword")
    public @ResponseBody Map<String, Boolean> pw_find(@RequestParam String userEmail, @RequestParam String userName) {
        Map<String, Boolean> json = new HashMap<>();
        boolean pwFindCheck = userService.userEmailCheck(userEmail, userName);

        json.put("check", pwFindCheck);
        return json;
    }

    // 임시 비밀번호 이메일 전송
    @PostMapping("/findPassword/email")
    public @ResponseBody void sendEmail(@RequestParam String userEmail, @RequestParam String userName) {
        Mail dto = sendEmailService.createMailAndChangePassword(userEmail, userName);
        sendEmailService.mailSend(dto);
    }
}