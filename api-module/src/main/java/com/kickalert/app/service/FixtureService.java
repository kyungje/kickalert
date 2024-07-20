package com.kickalert.app.service;

import com.kickalert.app.dto.internal.FixtureInDto;
import com.kickalert.app.dto.internal.PlayerInDto;
import com.kickalert.app.repository.FixtureRepository;
import com.kickalert.app.repository.PlayerRepository;
import com.kickalert.core.domain.Fixtures;
import com.kickalert.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FixtureService {
    private final FixtureRepository fixtureRepository;
    private final PlayerRepository playerRepository;

    public Map<String, Object> nextFixtureList(Long userId, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Slice<FixtureInDto.ResFixtureInfo> resFixtureInfos = fixtureRepository.nextFixtureList(userId, pageable);
        response.put("nextMatchList", resFixtureInfos.getContent());
        response.put("hasNext", resFixtureInfos.hasNext());

        return response;
    }

    public Map<String, Object> fixtureInfo(Long fixtureId) {
        Map<String, Object> response = new HashMap<>();

        FixtureInDto.ResFixtureInfo fixtureInfo = fixtureRepository.findFixtureInfo(fixtureId);

        if(!CommonUtils.isEmpty(fixtureInfo)) {
            fixtureInfo.setMatchDateTime(CommonUtils.toUTCStringFromDateTime(fixtureInfo.getMatchDateTimeOriginal()));
        }

        response.put("matchInfo", fixtureInfo);

        return response;
    }

    public Map<String, Object> fixturePlayerList(Long fixtureId, Long userId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Fixtures> fixture = fixtureRepository.findById(fixtureId);
        if(fixture.isPresent()) {
            response.put("fixtureId", fixture.get().getId());
            response.put("matchDateTime", fixture.get().getDatetime());
            if(!CommonUtils.isEmpty(fixture.get().getHomeTeam())){
                List<PlayerInDto.ResMatchPlayerInfo> resMatchPlayerInfos = playerRepository.matchPlayerList(fixture.get().getHomeTeam().getId(), userId, fixtureId);
                response.put("homePlayerList", resMatchPlayerInfos);
            }

            if(!CommonUtils.isEmpty(fixture.get().getAwayTeam())){
                List<PlayerInDto.ResMatchPlayerInfo> resMatchPlayerInfos = playerRepository.matchPlayerList(fixture.get().getAwayTeam().getId(), userId, fixtureId);
                response.put("awayPlayerList", resMatchPlayerInfos);
            }
        }

        return response;
    }
}
