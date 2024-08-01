package com.study.board2.security;

import com.study.board2.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(0)  // 높은 우선순위
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;
    private final MyUserDetailsService userDetailsService;
    private final PersistentTokenRepository tokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers("/css/**","/master/**")
                .and()
                .formLogin()
                .loginPage("/master/auth/login")
                .loginProcessingUrl("/master/auth/login")
                .usernameParameter("loginId")
                .passwordParameter("loginPw")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl("/master/auth/logout")
                .logoutSuccessUrl("/master/main")
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
}