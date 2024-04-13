package com.kickalert.app.controller;

import com.kickalert.app.dto.external.FeedbackExDto;
import com.kickalert.app.dto.external.FollowExDto;
import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.dto.internal.FeedbackInDto;
import com.kickalert.app.dto.internal.FollowInDto;
import com.kickalert.app.service.FeedbackService;
import com.kickalert.app.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FeedbackController extends BaseController {
    private final FeedbackService feedbackService;

    @PostMapping(value = "/feedback")
    public ResultExDto<Object> following(@RequestBody FeedbackExDto.ReqFeedBack reqFeedback) {

        FeedbackInDto.ReqFeedBack reqFeedBack
                = new FeedbackInDto.ReqFeedBack(reqFeedback.userId(), reqFeedback.like(), reqFeedback.feedbackContent());

        return simpleResult(feedbackService.feedback(reqFeedBack));
    }
}
