package com.kickalert.app.controller;

import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.service.FootBallApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/api")
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
