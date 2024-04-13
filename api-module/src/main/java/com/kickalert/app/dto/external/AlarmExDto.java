package com.kickalert.app.dto.external;

import java.time.LocalDateTime;

public class AlarmExDto {
    public record ReqAlarmSetting(
            String userId,
            String fixtureId,
            String playerId,
            String alarmType
    ) {}
}
