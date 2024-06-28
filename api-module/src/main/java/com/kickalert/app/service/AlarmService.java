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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Map<String, Object> activeAlarmList(Long userId, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> activeAlarmList = new ArrayList<>();
        List<Map<String, Object>> alarmPlayers = new ArrayList<>();

        Map<String, Object> activeAlarm1 = new HashMap<>();
        Map<String, Object> alarmPlayer1 = new HashMap<>();

        alarmPlayer1.put("playerId", 1);
        alarmPlayer1.put("playerPhotoUrl", "https://apiv3.apifootball.com/badges/players/9898_k-benzema.jpg");
        alarmPlayer1.put("playerName", "Son");
        alarmPlayer1.put("alarmType", "1");

        alarmPlayers.add(alarmPlayer1);

        Map<String, Object> alarmPlayer2 = new HashMap<>();

        alarmPlayer2.put("playerId", 2);
        alarmPlayer2.put("playerPhotoUrl", "https://apiv3.apifootball.com/badges/players/9898_k-benzema.jpg");
        alarmPlayer2.put("playerName", "Son");
        alarmPlayer2.put("alarmType", "2");

        alarmPlayers.add(alarmPlayer2);

        Map<String, Object> alarmPlayer3 = new HashMap<>();

        alarmPlayer3.put("playerId", 3);
        alarmPlayer3.put("playerPhotoUrl", "https://apiv3.apifootball.com/badges/players/9898_k-benzema.jpg");
        alarmPlayer3.put("playerName", "Son");
        alarmPlayer3.put("alarmType", "3");

        alarmPlayers.add(alarmPlayer3);

        activeAlarm1.put("fixtureId", 1);
        activeAlarm1.put("homeTeamId", 4);
        activeAlarm1.put("awayTeamId", 4);
        activeAlarm1.put("homeTeamName", "Inter");
        activeAlarm1.put("awayTeamName", "Arsenal");
        activeAlarm1.put("matchDateTime", "2024-06-25T14:30:00");
        activeAlarm1.put("homeLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
        activeAlarm1.put("awayLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
        activeAlarm1.put("alarmPlayers", alarmPlayers);

        activeAlarmList.add(activeAlarm1);

        Map<String, Object> activeAlarm2 = new HashMap<>();

        activeAlarm2.put("fixtureId", 1);
        activeAlarm2.put("homeTeamId", 4);
        activeAlarm2.put("awayTeamId", 4);
        activeAlarm2.put("homeTeamName", "Inter");
        activeAlarm2.put("awayTeamName", "Arsenal");
        activeAlarm2.put("matchDateTime", "2024-06-25T14:30:00");
        activeAlarm2.put("homeLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
        activeAlarm2.put("awayLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
        activeAlarm2.put("alarmPlayers", alarmPlayers);

        activeAlarmList.add(activeAlarm2);

        Map<String, Object> activeAlarm3 = new HashMap<>();

        activeAlarm3.put("fixtureId", 1);
        activeAlarm3.put("homeTeamId", 4);
        activeAlarm3.put("awayTeamId", 4);
        activeAlarm3.put("homeTeamName", "Inter");
        activeAlarm3.put("awayTeamName", "Arsenal");
        activeAlarm3.put("matchDateTime", "2024-06-25T14:30:00");
        activeAlarm3.put("homeLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
        activeAlarm3.put("awayLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
        activeAlarm3.put("alarmPlayers", alarmPlayers);

        activeAlarmList.add(activeAlarm3);

        response.put("activeAlarmList", activeAlarmList);
        response.put("hasNext", true);

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

        AlarmHistory alarmHistory = alarmHistoryRepository.findByPlayerAndFixtureAndMember(player, fixture, member)
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
