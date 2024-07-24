package com.study.board2.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private int idx;
    private int boardIdx;
    private int boardGroupIdx;
    private int userNo;
    private String title;
    private String content;
    private int hits;
    private boolean noticeYn;
    private boolean secretYn;
    private boolean deleteYn;
    private LocalDateTime regDate;

    // 계층 구조 관련 필드
    private Integer parentIdx;  // 부모 글의 ID
    private int depth;          // 게시글 깊이
    private int sorts;       // 같은 깊이 내에서의 순서
}
