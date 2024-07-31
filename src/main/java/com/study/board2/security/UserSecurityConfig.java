package com.study.board2.security;

import com.study.board2.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Order(1)  // 낮은 우선순위
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers("/css/**","/front/**")
                .and()
                .formLogin()
                .loginPage("/front/auth/login")
                .loginProcessingUrl("/front/auth/login")
                .usernameParameter("loginId")
                .passwordParameter("loginPw")
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl("/front/auth/logout")
                .logoutSuccessUrl("/front/main")  // 로그아웃 성공 후 리다이렉트 설정
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID")
                .and()
                .rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository)
                .tokenValiditySeconds(604800)
                .and()
                .csrf().disable();
    }

    public final LoginSuccessHandler successHandler;

    public final MyUserDetailsService userDetailsService;

    public final PersistentTokenRepository tokenRepository;
}