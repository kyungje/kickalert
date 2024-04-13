package com.kickalert.app.service;

import com.kickalert.app.dto.internal.FixtureInDto;
import com.kickalert.app.repository.FixtureRepository;
import com.kickalert.core.domain.Fixtures;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FixtureService {
    private final FixtureRepository fixtureRepository;

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

        response.put("matchInfo", fixtureInfo);

        return response;
    }

    public Map<String, Object> fixturePlayerList(Long fixtureId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Fixtures> fixture = fixtureRepository.findById(fixtureId);
        if(fixture.isPresent()) {
            response.put("fixtureId", fixture.get().getId());
            response.put("matchDateTime", fixture.get().getDatetime());
            response.put("homePlayerList", fixture.get().getHomeTeamLineup());
            response.put("awayPlayerList", fixture.get().getAwayTeamLineup());
        }

        return response;
    }
}
