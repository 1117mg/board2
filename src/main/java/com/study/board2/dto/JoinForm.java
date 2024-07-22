package com.study.board2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinForm {
    private String loginId;
    private String loginPw;
    private String loginPwCheck;

    private String userName;
    private String userEmail;
}
