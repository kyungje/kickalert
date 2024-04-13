package com.kickalert.app.service;

import com.kickalert.app.dto.internal.PlayerInDto;
import com.kickalert.app.exception.UserCustomException;
import com.kickalert.app.repository.PlayerRepository;
import com.kickalert.core.domain.Players;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.kickalert.app.exception.advice.ErrorCode.UNAUTHORIZED_KICK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Map<String, Object> followingPlayerList(Long userId, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Slice<PlayerInDto.ResFollowIngPlayerInfo> resFollowIngPlayerInfos = playerRepository.followingPlayerList(userId, pageable);
        response.put("followingPlayerList", resFollowIngPlayerInfos.getContent());
        response.put("hasNext", resFollowIngPlayerInfos.hasNext());

        return response;
    }

    public Map<String, Object> searchPlayerList(Long userId, String searchKeyword, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Slice<PlayerInDto.ResSearchPlayerInfo> resPlayerInfos = playerRepository.searchPlayerList(userId, searchKeyword, pageable);
        response.put("searchPlayerList", resPlayerInfos.getContent());
        response.put("hasNext", resPlayerInfos.hasNext());

        return response;
    }
}
