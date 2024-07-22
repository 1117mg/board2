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
    private String userId;
    private String userPw;
    private String userName;
    private LocalDateTime regDate;
    private String userEmail;
    private String userRole;
}
