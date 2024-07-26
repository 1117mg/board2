package com.study.board2.service;

import com.study.board2.dto.CtgAuth;
import com.study.board2.dto.PermissionType;
import com.study.board2.repository.CtgAuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CtgAuthService {

    private final CtgAuthMapper ctgAuthMapper;

    public List<CtgAuth> getCtgAuthForUser(int userId) {
        return ctgAuthMapper.selectCtgAuthByUserId(userId);
    }

    public void updatePermissions(int userId, Map<String, String> params) {
        // 1. 모든 기존 권한 삭제
        ctgAuthMapper.deleteAllByUserId(userId);
        List<CtgAuth> auths=ctgAuthMapper.selectCtgAuthByUserId(userId);
        log.info(String.valueOf(auths));

        // 2. 새 권한 삽입
        for (String key : params.keySet()) {
            try {
                // 키를 언더스코어(_) 기준으로 분할
                String[] keyParts = key.split("_");
                int boardId = Integer.parseInt(keyParts[0]);
                String permissionType = keyParts[1];

                // 권한이 체크된 경우만 삽입
                if ("true".equals(params.get(key))) {
                    CtgAuth ctgAuth = new CtgAuth();
                    ctgAuth.setIdx(userId);
                    ctgAuth.setBoardId(boardId);

                    switch (permissionType) {
                        case "read":
                            ctgAuth.setRead(true);
                            break;
                        case "write":
                            ctgAuth.setWrite(true);
                            break;
                        case "download":
                            ctgAuth.setDownload(true);
                            break;
                    }

                    // 새 권한을 데이터베이스에 삽입
                    ctgAuthMapper.insertCtgAuth(ctgAuth);
                }
            } catch (NumberFormatException e) {
                System.err.println("유효하지 않은 형식의 boardId: " + key);
            }
        }
    }
}
