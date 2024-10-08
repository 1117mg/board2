package com.study.board2.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mail {
    private String address;
    private String title;
    private String message;
    private String code;
}