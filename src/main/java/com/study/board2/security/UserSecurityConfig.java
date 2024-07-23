package com.study.board2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(1)  // 낮은 우선순위
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/front/**")
                .and()
                .formLogin()
                .loginPage("/front/auth/login")
                .loginProcessingUrl("/front/auth/login")
                .usernameParameter("loginId")
                .passwordParameter("loginPw")
                .defaultSuccessUrl("/front/main")
                .and()
                .logout()
                .logoutUrl("/front/auth/logout")
                .logoutSuccessUrl("/front/main")  // 로그아웃 성공 후 리다이렉트 설정
                .deleteCookies("remember-me")
                .and()
                .csrf().disable()
                .rememberMe();
    }
}