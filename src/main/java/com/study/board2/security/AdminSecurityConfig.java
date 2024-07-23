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