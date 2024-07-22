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
}
