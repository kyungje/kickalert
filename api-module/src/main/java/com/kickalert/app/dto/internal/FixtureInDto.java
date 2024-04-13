package com.kickalert.app.dto.internal;

import java.time.LocalDateTime;

public class FixtureInDto {
    public record ResFixtureInfo(
            Long fixtureId
            , Long homeTeamId
            , Long awayTeamId
            , String homeTeamName
            , String awayTeamName
            , LocalDateTime matchDateTime
            , String homeTeamLogo
            , String awayTeamLogo
            , Long leagueId
            , String leagueName) {}
}
