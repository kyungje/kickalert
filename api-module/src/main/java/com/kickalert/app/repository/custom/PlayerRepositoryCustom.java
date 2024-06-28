package com.kickalert.app.repository.custom;

import com.kickalert.app.dto.internal.PlayerInDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PlayerRepositoryCustom {
    Slice<PlayerInDto.ResFollowIngPlayerInfo> followingPlayerList(Long memberId, Pageable pageable);

    Slice<PlayerInDto.ResSearchPlayerInfo> searchPlayerList(Long memberId, String searchKeyword, Pageable pageable);

    List<PlayerInDto.ResMatchPlayerInfo> matchPlayerList(Long teamId, Long memberId, Long fixtureId);
}
