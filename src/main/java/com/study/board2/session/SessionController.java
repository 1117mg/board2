package com.study.board2.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private final SessionListener sessionListener;

    public SessionController(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    @GetMapping("/active-sessions")
    public int getActiveSessions() {
        return sessionListener.getActiveSessions();
    }
}