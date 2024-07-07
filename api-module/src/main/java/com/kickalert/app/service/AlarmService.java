package com.kickalert.app.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kickalert.app.dto.internal.AlarmInDto;
import com.kickalert.app.repository.AlarmHistoryRepository;
import com.kickalert.app.repository.FixtureRepository;
import com.kickalert.app.repository.MemberRepository;
import com.kickalert.app.repository.PlayerRepository;
import com.kickalert.core.domain.AlarmHistory;
import com.kickalert.core.domain.Fixtures;
import com.kickalert.core.domain.Members;
import com.kickalert.core.domain.Players;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
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

//    public Map<String, Object> activeAlarmList(Long userId, Pageable pageable) {
//        Map<String, Object> response = new HashMap<>();
//
//        List<Map<String, Object>> activeAlarmList = new ArrayList<>();
//        List<Map<String, Object>> alarmPlayers = new ArrayList<>();
//
//        Map<String, Object> activeAlarm1 = new HashMap<>();
//        Map<String, Object> alarmPlayer1 = new HashMap<>();
//
//        alarmPlayer1.put("playerId", 1);
//        alarmPlayer1.put("playerPhotoUrl", "https://apiv3.apifootball.com/badges/players/9898_k-benzema.jpg");
//        alarmPlayer1.put("playerName", "Son");
//        alarmPlayer1.put("alarmType", "1");
//
//        alarmPlayers.add(alarmPlayer1);
//
//        Map<String, Object> alarmPlayer2 = new HashMap<>();
//
//        alarmPlayer2.put("playerId", 2);
//        alarmPlayer2.put("playerPhotoUrl", "https://apiv3.apifootball.com/badges/players/9898_k-benzema.jpg");
//        alarmPlayer2.put("playerName", "Son");
//        alarmPlayer2.put("alarmType", "2");
//
//        alarmPlayers.add(alarmPlayer2);
//
//        Map<String, Object> alarmPlayer3 = new HashMap<>();
//
//        alarmPlayer3.put("playerId", 3);
//        alarmPlayer3.put("playerPhotoUrl", "https://apiv3.apifootball.com/badges/players/9898_k-benzema.jpg");
//        alarmPlayer3.put("playerName", "Son");
//        alarmPlayer3.put("alarmType", "3");
//
//        alarmPlayers.add(alarmPlayer3);
//
//        activeAlarm1.put("fixtureId", 1);
//        activeAlarm1.put("homeTeamId", 4);
//        activeAlarm1.put("awayTeamId", 4);
//        activeAlarm1.put("homeTeamName", "Inter");
//        activeAlarm1.put("awayTeamName", "Arsenal");
//        activeAlarm1.put("matchDateTime", "2024-06-25T14:30:00");
//        activeAlarm1.put("homeLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
//        activeAlarm1.put("awayLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
//        activeAlarm1.put("alarmPlayers", alarmPlayers);
//
//        activeAlarmList.add(activeAlarm1);
//
//        Map<String, Object> activeAlarm2 = new HashMap<>();
//
//        activeAlarm2.put("fixtureId", 1);
//        activeAlarm2.put("homeTeamId", 4);
//        activeAlarm2.put("awayTeamId", 4);
//        activeAlarm2.put("homeTeamName", "Inter");
//        activeAlarm2.put("awayTeamName", "Arsenal");
//        activeAlarm2.put("matchDateTime", "2024-06-25T14:30:00");
//        activeAlarm2.put("homeLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
//        activeAlarm2.put("awayLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
//        activeAlarm2.put("alarmPlayers", alarmPlayers);
//
//        activeAlarmList.add(activeAlarm2);
//
//        Map<String, Object> activeAlarm3 = new HashMap<>();
//
//        activeAlarm3.put("fixtureId", 1);
//        activeAlarm3.put("homeTeamId", 4);
//        activeAlarm3.put("awayTeamId", 4);
//        activeAlarm3.put("homeTeamName", "Inter");
//        activeAlarm3.put("awayTeamName", "Arsenal");
//        activeAlarm3.put("matchDateTime", "2024-06-25T14:30:00");
//        activeAlarm3.put("homeLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
//        activeAlarm3.put("awayLogo", "https://apiv3.apifootball.com/badges/141_arsenal.jpg");
//        activeAlarm3.put("alarmPlayers", alarmPlayers);
//
//        activeAlarmList.add(activeAlarm3);
//
//        response.put("activeAlarmList", activeAlarmList);
//        response.put("hasNext", true);
//
//        return response;
//    }

    @Transactional
    public Map<String, Object> activeAlarmList(Long userId, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();

        Slice<AlarmInDto.ResActiveAlarmInfo> resActiveAlarmInfos = alarmHistoryRepository.activeAlarmList(userId, pageable);
        response.put("activeAlarmList", resActiveAlarmInfos.getContent());
        response.put("hasNext", resActiveAlarmInfos.hasNext());

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

    @Transactional
    public Map<String, Object> alarmTest() {
        Map<String, Object> response = new HashMap<>();
        String firebaseMessage = "";

        //String deviceToken = "dIp9KQu-ZkSvqUZlWFpGhD:APA91bGa_NZSCToNWC9MH_zGIIbxY5HrD9sljAMAnq2NfD1beRQ42u9TsY0jg7fhBaq4vrPP78kC6Eb07854L6HxysmlajFuGAN0pqKYDwRLMOj3kim_eNhSnCTLK4Vm9CNANesb2ecZ";

        String deviceToken = "dJe2IvtAQCC6s37OsMMHlQ:APA91bGk53MEbkcragJYY1GhwmDE5g975WZSWDnV8Dg8lR2oKMaqEpJOO7N5xnWYzmN2UZO1IzLu3czCRcK-9gv433V-H1T9gDvQuSWqPyx70xj2lbO4n9-iQh2KrNBP29tKMoAUQE5K";

        try {
            ClassPathResource classPathResource = new ClassPathResource("firebase/kick-alert-app-dev-firebase-adminsdk.json");
            // Firebase 서비스 계정 JSON 파일 경로
            InputStream serviceAccount = classPathResource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // FirebaseApp 초기화
            FirebaseApp.initializeApp(options);

            // 전송할 메시지 구성
            Message message = Message.builder()
                    .setToken(deviceToken)  // 수신자의 디바이스 토큰
                    .setNotification(Notification.builder()
                            .setTitle("알림 제목")
                            .setBody("알림 내용")
                            .build())
                    .putData("nested_data", """
                            {
                                'matchInfo': {
                                    "fixtureId": 1,
                                    "homeTeamId": 1,
                                    "awayTeamId": 2,
                                    "homeTeamName": "team_name1",
                                    "awayTeamName": "team_name2",
                                    "matchDateTime": "2024-04-07T14:30:00Z",
                                    "homeTeamLogo": "https://cdn.sportmonks.com/images/soccer/teams/16/496.png",
                                    "awayTeamLogo": "https://cdn.sportmonks.com/images/soccer/teams/30/62.png",
                                    "leagueId": 1,
                                    "leagueName": "league_name1"
                                },
                                'playerList': [
                                    {
                                        "playerId": 33,
                                        "playerName": "James Tavernier",
                                        "playerPhotoUrl": "https://cdn.sportmonks.com/images/soccer/players/22/758.png",
                                        "alarmType": "3",
                                        "teamId": 5,
                                    },
                                    {
                                        "playerId": 33,
                                        "playerName": "James Tavernier",
                                        "playerPhotoUrl": "https://cdn.sportmonks.com/images/soccer/players/22/758.png",
                                        "alarmType": "3",
                                        "teamId": 5,
                                    },
                                    {
                                        "playerId": 33,
                                        "playerName": "James Tavernier",
                                        "playerPhotoUrl": "https://cdn.sportmonks.com/images/soccer/players/22/758.png",
                                        "alarmType": "3",
                                        "teamId": 5,
                                    }
                                ],
                            }
                            """)
                    .build();

            // 메시지 전송
            firebaseMessage = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + firebaseMessage);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        response.put("firebaseMessage", firebaseMessage);

        return response;
    }
}
