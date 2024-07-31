package com.study.board2.service;


import com.study.board2.dto.Mail;
import com.study.board2.dto.User;
import com.study.board2.repository.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SendEmailService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private static final String FROM_ADDRESS = "boardproject11@gmail.com";

    public Mail createMailAndChangePassword(String userEmail, String userName) {
        String tempPassword = getTempPassword();
        Mail mail = new Mail();
        mail.setAddress(userEmail);
        mail.setTitle("[Board] " + userName + "님의 임시비밀번호 안내 이메일 입니다.");
        mail.setMessage("안녕하세요. Board 임시비밀번호 발급 안내 이메일 입니다. [" + userName + "]님의 임시 비밀번호는 " + tempPassword + " 입니다.");

        User user = userMapper.findByUsername(userName);
        if (user != null) {
            user.setUserPw(passwordEncoder.encode(tempPassword));
            userMapper.updateUserPassword(user);
        }
        return mail;
    }

    public String getTempPassword() {
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
        log.info("이메일 전송 완료!");
    }
}