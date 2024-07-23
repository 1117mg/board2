package com.study.board2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    // User 테이블에 저장될 기본정보
    private int idx;
    private String userId;
    private String userPw;
    private LocalDateTime regDate;
    private String userRole;

    // Member(ROLE_USER) 또는 Admin(ROLE_ADMIN) 테이블에 저장될 세부정보
    private String userName;
    private String userEmail;
}
