package com.study.board2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateStatusRequest {
    private List<Integer> ids;
    private boolean useYn;
}