package com.kickalert.app.dto.internal;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PlayerInDto {
    @Getter
    @Setter
    public static class ResFollowIngPlayerInfo{
        private String playerPhotoUrl;
        private String playerName;
        private Long playerId;
        private Long teamId;
        private String teamName;
        private String teamLogo;
        private Long countryId;
        private String country;
        private String countryLogo;
        private LocalDateTime nextMatchDateTimeOriginal;
        private String nextMatchDateTime;
    }

    public record ResSearchPlayerInfo(
            Long playerId,
            String playerName,
            String playerPhotoUrl,
            String followYn
    ) {}

    public record ResMatchPlayerInfo(
            Long playerId,
            String playerName,
            String playerPhotoUrl,
            String alarmType,
            String followYn
    ) {}
}
