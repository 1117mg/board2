package com.study.board2.security;

import com.study.board2.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(0)  // 높은 우선순위
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(604800)
                .and()
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
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID")
                .and()
                .csrf().disable();
    }

    @Qualifier("dataSource")
    private final DataSource dataSource;

    private final MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false); // 테이블을 초기화하지 않음
        return jdbcTokenRepository;
    }
}