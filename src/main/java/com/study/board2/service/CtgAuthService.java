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

        // 권한을 저장할 맵을 생성
        Map<Integer, CtgAuth> authMap = new HashMap<>();

        // 2. 새 권한 삽입 또는 업데이트
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            try {
                // 키를 언더스코어(_) 기준으로 분할
                String[] keyParts = key.split("_");
                if (keyParts.length != 2) {
                    log.warn("유효하지 않은 형식의 key: " + key);
                    continue;
                }

                int boardId = Integer.parseInt(keyParts[0]);
                String permissionType = keyParts[1];

                // 권한이 체크된 경우만 처리
                if ("true".equals(value)) {
                    CtgAuth ctgAuth = authMap.computeIfAbsent(boardId, id -> new CtgAuth());
                    ctgAuth.setUserId(userId);
                    ctgAuth.setBoardId(boardId);

                    switch (permissionType) {
                        case "read":
                            ctgAuth.setCanRead(true);
                            break;
                        case "write":
                            ctgAuth.setCanWrite(true);
                            break;
                        case "download":
                            ctgAuth.setCanDownload(true);
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                log.error("유효하지 않은 형식의 boardId: " + entry.getKey(), e);
            }
        }

        // 3. 권한 삽입 또는 업데이트
        for (CtgAuth ctgAuth : authMap.values()) {
            // 존재하는 경우 업데이트, 그렇지 않으면 삽입
            boolean exists = ctgAuthMapper.existsByUserIdAndBoardId(ctgAuth.getUserId(), ctgAuth.getBoardId());
            if (exists) {
                ctgAuthMapper.updateCtgAuth(ctgAuth);
            } else {
                ctgAuthMapper.insertCtgAuth(ctgAuth);
            }
        }
    }
}
