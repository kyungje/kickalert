package com.kickalert.app.dto.external;

public class AlarmExDto {
    public record ReqAlarmSetting(
            String userId,
            String fixtureId,
            String playerId,
            String alarmType
    ) {}

    public record ReqAlarmMultiTest(
        String[] fcmTokens
    ) {}
}
