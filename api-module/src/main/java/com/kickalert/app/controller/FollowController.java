package com.kickalert.app.controller;

import com.kickalert.app.dto.external.FollowExDto;
import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.dto.internal.FollowInDto;
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
@RequestMapping("/v1/follow")
public class FollowController extends BaseController {
    private final FollowService followService;

    @PostMapping(value = "/following")
    public ResultExDto<Object> following(@RequestBody FollowExDto.ReqFollow reqFollow) {

        FollowInDto.ReqFollow inReqFollow
                = new FollowInDto.ReqFollow(reqFollow.userId(), reqFollow.playerId());

        return simpleResult(followService.playerFollowing(inReqFollow));
    }

    @PostMapping(value = "/unfollowing")
    public ResultExDto<Object> unfollowing(@RequestBody FollowExDto.ReqFollow reqFollow) {

        FollowInDto.ReqFollow inReqFollow
                = new FollowInDto.ReqFollow(reqFollow.userId(), reqFollow.playerId());

        return simpleResult(followService.playerUnFollowing(inReqFollow));
    }
}
