package com.kickalert.app.dto.internal;

public class FeedbackInDto {
    public record ReqFeedBack(
            String userId,
            String like,
            String contents
    ) {}
}
