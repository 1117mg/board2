package com.study.board2.dto;

import lombok.Data;

@Data
public class CtgAuth {
    private int idx;
    private int boardId;
    private boolean read;
    private boolean write;
    private boolean download;
}
