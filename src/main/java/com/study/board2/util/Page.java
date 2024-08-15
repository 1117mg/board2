package com.study.board2.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Page<T> {
    private List<T> content;     // 현재 페이지의 데이터 목록
    private int pageNumber;      // 현재 페이지 번호
    private int pageSize;        // 페이지당 항목 수
    private long totalElements;  // 총 항목 수
    private int totalPages;      // 총 페이지 수

    public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }
}
