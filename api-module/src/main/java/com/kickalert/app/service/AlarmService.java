package com.kickalert.app.service;

import com.kickalert.app.dto.internal.AlarmInDto;
import com.kickalert.app.repository.AlarmHistoryRepository;
import com.kickalert.app.repository.FixtureRepository;
import com.kickalert.app.repository.MemberRepository;
import com.kickalert.app.repository.PlayerRepository;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.AlarmHistory;
import com.kickalert.core.domain.Fixtures;
import com.kickalert.core.domain.Members;
import com.kickalert.core.domain.Players;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
    private final AlarmHistoryRepository alarmHistoryRepository;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;
    private final FixtureRepository fixtureRepository;

    public Map<String, Object> alarmHistoryList(Long userId, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Slice<AlarmInDto.ResAlarmHistoryInfo> resAlarmHistoryInfos = alarmHistoryRepository.alarmHistoryList(userId, pageable);
        response.put("alarmHistoryList", resAlarmHistoryInfos.getContent());
        response.put("hasNext", resAlarmHistoryInfos.hasNext());

        return response;
    }

    @Transactional
    public Map<String, Object> alarmSetting(AlarmInDto.ReqAlarmSetting reqAlarmSetting) {
        Map<String, Object> response = new HashMap<>();

        Members member = memberRepository.findById(Long.parseLong(reqAlarmSetting.userId()))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Players player = playerRepository.findById(Long.parseLong(reqAlarmSetting.playerId()))
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        Fixtures fixture = fixtureRepository.findById(Long.parseLong(reqAlarmSetting.fixtureId()))
                .orElseThrow(() -> new IllegalArgumentException("fixture not found"));

        AlarmHistory alarmHistory = alarmHistoryRepository.findByPlayerAndFixture(player, fixture)
                .map(existingAlarmHistory -> {
                    existingAlarmHistory.changeAlarmType(reqAlarmSetting.alarmType());
                    existingAlarmHistory.changeAlarmDateTime(LocalDateTime.now());
                    return existingAlarmHistory;
                })
                .orElseGet(() -> AlarmHistory.builder()
                        .member(member)
                        .player(player)
                        .homeTeam(fixture.getHomeTeam())
                        .awayTeam(fixture.getAwayTeam())
                        .fixture(fixture)
                        .alarmType(reqAlarmSetting.alarmType())
                        .matchDateTime(fixture.getDatetime())
                        .alarmDateTime(LocalDateTime.now())
                        .build());

        Long id = alarmHistoryRepository.save(alarmHistory).getId();

        response.put("alarmHistoryId", id);

        return response;
    }
}
