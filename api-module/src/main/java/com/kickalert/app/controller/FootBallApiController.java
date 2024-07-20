package com.kickalert.app.controller;

import com.kickalert.app.dto.external.FollowExDto;
import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.dto.internal.FollowInDto;
import com.kickalert.app.service.FootBallApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class FootBallApiController extends BaseController{
    private final FootBallApiService footBallApiService;

    @GetMapping(value = "/fixture")
    public ResultExDto<Object> apiFixtureTest() {
        return simpleResult(footBallApiService.fetchFixtureData());
    }

    @GetMapping(value = "/country")
    public ResultExDto<Object> apiCountryTest() {
        return simpleResult(footBallApiService.fetchCountryData());
    }
}
