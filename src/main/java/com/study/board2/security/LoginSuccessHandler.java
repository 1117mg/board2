package com.study.board2.security;

import com.study.board2.dto.User;
import com.study.board2.service.AdminService;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AdminService adminService;
    private final UserService userService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = userService.findByUserId(authentication.getName());

        // 로그인 기록 저장
        if ("ROLE_USER".equals(user.getUserRole())) {
            adminService.recordLogin(user.getIdx());
        }

        // AJAX 요청 여부 확인
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // AJAX 요청인 경우 JSON 응답
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": true}");
        } else {
            // 일반 요청인 경우 리다이렉션
            String targetUrl = determineTargetUrl(authentication);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }
    }

    private String determineTargetUrl(Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        if (isUser) {
            return "/front/main";
        } else if (isAdmin) {
            return "/master/main";
        } else {
            throw new IllegalStateException();
        }
    }
}