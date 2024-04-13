package com.kickalert.app.dto.external;

public class FollowExDto {
    public record ReqFollow(
            String userId,
            String playerId
    ) {}
}
