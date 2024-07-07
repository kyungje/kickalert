package com.kickalert.app.dto.internal;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmInDto {
    public record ResAlarmHistoryInfo(
            Long homeTeamId
            , Long awayTeamId
            , String homeTeamName
            , String awayTeamName
            , LocalDateTime matchDateTime
            , String homeLogo
            , String awayLogo
            , Long leagueId
            , String leagueName
            , Long playerId
            , String playerName
            , String alarmType
            , LocalDateTime alarmDateTime
    ) {}

    public record ReqAlarmSetting(
            String userId,
            String fixtureId,
            String playerId,
            String alarmType
    ) {}

    @Getter
    @Setter
    public static class ResActiveAlarmInfo {
        private Long fixtureId;
        private Long homeTeamId;
        private Long awayTeamId;
        private String homeTeamName;
        private String awayTeamName;
        private LocalDateTime matchDateTimeOriginal;
        private String homeLogo;
        private String awayLogo;
        private String matchDateTime;
        private List<AlarmPlayerInfo> alarmPlayers = new ArrayList<>();
    }

    public record AlarmPlayerInfo(
            Long playerId,
            String playerName,
            String playerPhotoUrl,
            String alarmType
    ) {}
}
