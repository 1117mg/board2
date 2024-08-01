package com.study.board2.global;

import com.study.board2.session.SessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SessionListener httpSessionListener(){
        return new SessionListener();
    }
}
