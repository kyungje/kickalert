package com.kickalert.app.dto.external;

public class MemberExDto {
    public record ReqInitUser(
            String fcmToken
    ) {}
}
