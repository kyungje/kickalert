package com.kickalert.app.dto.external;

public class FeedbackExDto {
    public record ReqFeedBack(
            String userId,
            String like,
            String feedbackContent
    ) {}
}
