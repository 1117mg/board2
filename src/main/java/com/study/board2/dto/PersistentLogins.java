package com.study.board2.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PersistentLogins {
    private String series;
    private String username;
    private String token;
    private LocalDateTime lastUsed;
}