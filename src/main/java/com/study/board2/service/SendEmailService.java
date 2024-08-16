package com.study.board2.service;

import com.study.board2.util.Mail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    private static final String FROM_ADDRESS = "boardproject11@gmail.com";

    public Mail createMailAndChangePassword(String userEmail, String userName) {
        String code = getCode();
        Mail mail = new Mail();
        mail.setAddress(userEmail);
        mail.setTitle("[Board] " + userName + "님의 비밀번호 재설정 안내 이메일 입니다.");
        mail.setCode(code);
        mail.setMessage("안녕하세요. [" + userName + "]님의 비밀번호 재설정 인증번호는 " + code + " 입니다. 3분 안에 비밀번호 재설정을 완료해주세요.");

        return mail;
    }

    public String getCode() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            str.append(charSet[idx]);
        }
        return str.toString();
    }

    public void mailSend(Mail mailDto) {
        log.info("이메일 전송 시작: {}", mailDto.getAddress());
        long count = getEmailRequestCount(mailDto.getAddress());
        if (count >= 5) {
            throw new RuntimeException("이메일 인증 요청 5번 초과로 24시간 동안 이메일 인증 요청을 할 수 없습니다.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        mailSender.send(message);
        saveVerificationCode(mailDto.getAddress(), String.valueOf(mailDto.getCode()));

        increaseEmailRequestCount(mailDto.getAddress());
        log.info("이메일 전송 완료!");
    }

    //redis에서 인증코드 가져오기
    public String getVerificationCode(String userEmail) {
        return redisTemplate.opsForValue().get(userEmail);
    }
    //redis에 인증코드 저장
    public void saveVerificationCode(String userEmail, String code) {
        redisTemplate.opsForValue().set(userEmail, code, 3, TimeUnit.MINUTES); //3분 타임아웃
    }

    //이메일 요청 카운트 증가
    public void increaseEmailRequestCount(String userEmail) {
        String key = "email_request_count:" + userEmail;
        long count = redisTemplate.opsForValue().increment(key);

        if (count == 5) {
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }
    //이메일 요청 카운트 가져오기
    public long getEmailRequestCount(String userEmail) {
        String key = "email_request_count:" + userEmail;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }
}