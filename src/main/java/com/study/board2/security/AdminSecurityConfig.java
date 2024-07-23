package com.study.board2.security;

import com.study.board2.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(0)  // 높은 우선순위
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/master/**")
                .and()
                .formLogin()
                .loginPage("/master/auth/login")
                .loginProcessingUrl("/master/auth/login")
                .usernameParameter("loginId")
                .passwordParameter("loginPw")
                .defaultSuccessUrl("/master/main")
                /*.and()
                .rememberMe() // rememberMe 기능 작동함
                .rememberMeParameter("remember") // default: remember-me, checkbox 등의 이름과 맞춰야함
                .tokenValiditySeconds(3600) // 쿠키의 만료시간 설정(초), default: 14일
                .alwaysRemember(false) // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
                .userDetailsService(userDetailsService) // 기능을 사용할 때 사용자 정보가 필요함. 반드시 이 설정*/
                .and()
                .logout()
                .logoutUrl("/master/auth/logout")
                .logoutSuccessUrl("/master/main")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    if(session!=null){
                        session.invalidate();
                    }
                })
                .logoutSuccessHandler((request, response, authentication) ->
                        response.sendRedirect("/login"))
                .deleteCookies("remember-me")
                .and()
                .csrf().disable();
    }
}