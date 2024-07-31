package com.study.board2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SessionListener httpSessionListener(){
        return new SessionListener();
    }
}
