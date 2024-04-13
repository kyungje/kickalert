package com.kickalert.app.dto.internal;

import java.time.LocalDateTime;

public class PlayerInDto {
    public record ResFollowIngPlayerInfo(
            String playerPhotoUrl,
            String playerName,
            Long playerId,
            Long teamId,
            String teamName,
            String teamLogo,
            Long leagueId,
            String leagueName,
            Long countryId,
            String country,
            String countryLogo,
            LocalDateTime nextMatchDateTime
    ) {}

    public record ResSearchPlayerInfo(
            Long playerId,
            String playerName,
            String playerPhotoUrl,
            String followYn
    ) {}
}
