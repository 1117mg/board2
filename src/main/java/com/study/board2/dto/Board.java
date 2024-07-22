package com.study.board2.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private int idx;
    private String boardId;
    private String boardType;
    private String boardTitle;
    private LocalDateTime regDate;
    private boolean deleteYn;
}
