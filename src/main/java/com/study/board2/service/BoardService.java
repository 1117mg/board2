package com.study.board2.service;

import com.study.board2.dto.Board;
import com.study.board2.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public List<Board> getAllBoards() {
        return boardMapper.findAll();
    }
}