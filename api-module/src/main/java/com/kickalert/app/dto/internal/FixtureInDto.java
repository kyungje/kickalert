package com.kickalert.app.dto.internal;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FixtureInDto {
    @Getter
    @Setter
    public static class  ResFixtureInfo{
        private Long fixtureId;
        private Long homeTeamId;
        private Long awayTeamId;
        private String homeTeamName;
        private String awayTeamName;
        private LocalDateTime matchDateTimeOriginal;
        private String matchDateTime;
        private String homeTeamLogo;
        private String awayTeamLogo;
        private Long leagueId;
        private String leagueName;
        private List<AlarmInDto.AlarmPlayerInfo> alarmPlayers = new ArrayList<>();

        public record AlarmPlayerInfo(
                Long playerId,
                String playerName,
                String playerPhotoUrl,
                String alarmType
        ) {}
    }
}
