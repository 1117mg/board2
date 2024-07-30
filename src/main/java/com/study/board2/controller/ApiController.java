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

    @GetMapping("/check/findPw")
    public @ResponseBody Map<String, Boolean> pw_find(@RequestParam String userEmail, @RequestParam String userName) {
        Map<String, Boolean> json = new HashMap<>();
        boolean pwFindCheck = userService.userEmailCheck(userEmail, userName);

        json.put("check", pwFindCheck);
        return json;
    }

    @PostMapping("/check/findPw/sendEmail")
    public @ResponseBody void sendEmail(@RequestParam String userEmail, @RequestParam String userName) {
        Mail dto = sendEmailService.createMailAndChangePassword(userEmail, userName);
        sendEmailService.mailSend(dto);
    }
}