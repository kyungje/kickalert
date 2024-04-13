package com.kickalert.app.dto.internal;

import java.time.LocalDateTime;

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
}
