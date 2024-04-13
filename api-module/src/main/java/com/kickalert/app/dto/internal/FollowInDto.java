package com.kickalert.app.dto.internal;

import java.time.LocalDateTime;

public class FollowInDto {
    public record ReqFollow(
            String userId,
            String playerId
    ) {}
}
