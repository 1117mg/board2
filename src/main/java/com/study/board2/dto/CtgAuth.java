package com.study.board2.dto;

import lombok.Data;

@Data
public class CtgAuth {
    private int userId;
    private int boardId;
    private boolean canRead;
    private boolean canWrite;
    private boolean canDownload;
}
