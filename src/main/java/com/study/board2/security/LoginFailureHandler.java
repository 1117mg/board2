package com.study.board2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.";

        if (exception.getMessage().contains("Bad credentials")) {
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        }

        response.getWriter().write("{\"error\":\"" + errorMessage + "\"}");
    }
}