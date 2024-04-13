package com.kickalert.app.controller;

import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.service.FixtureService;
import com.kickalert.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/match")
public class FixtureController extends BaseController{
    private final FixtureService fixtureService;

    @GetMapping(value = "/nextMatchList")
    public ResultExDto<Object> nextFixtureList(@RequestParam String userId, Pageable pageable) {
        if(CommonUtils.isEmpty(userId)) simpleResult("");
        return simpleResult(fixtureService.nextFixtureList(Long.parseLong(userId), pageable));
    }

    @GetMapping(value = "/matchInfo")
    public ResultExDto<Object> fixtureInfo(@RequestParam String fixtureId) {
        if(CommonUtils.isEmpty(fixtureId)) simpleResult("");
        return simpleResult(fixtureService.fixtureInfo(Long.parseLong(fixtureId)));
    }

    @GetMapping(value = "/matchPlayerList")
    public ResultExDto<Object> fixturePlayerList(@RequestParam String fixtureId) {
        if(CommonUtils.isEmpty(fixtureId)) simpleResult("");
        return simpleResult(fixtureService.fixturePlayerList(Long.parseLong(fixtureId)));
    }

}
