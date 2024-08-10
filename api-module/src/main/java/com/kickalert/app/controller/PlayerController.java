package com.kickalert.app.controller;

import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.service.PlayerService;
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
@RequestMapping("/api/v1/player")
public class PlayerController extends BaseController{
    private final PlayerService playerService;

//    @GetMapping(value = "/player")
//    public ResultExDto<Object> findPlayerBy(@RequestParam Long playerId) {
//        return simpleResult(playerService.findPlayerBy(playerId));
//    }

    @GetMapping(value = "/followingPlayerList")
    public ResultExDto<Object> followingPlayerList(@RequestParam String userId, Pageable pageable) {
        if(CommonUtils.isEmpty(userId)) simpleResult("");
        return simpleResult(playerService.followingPlayerList(Long.parseLong(userId), pageable));
    }

    @GetMapping(value = "/searchPlayerList")
    public ResultExDto<Object> searchPlayerList(@RequestParam String userId, @RequestParam String keyword, Pageable pageable) {
        if(CommonUtils.isEmpty(userId)) simpleResult("");
        return simpleResult(playerService.searchPlayerList(Long.parseLong(userId), keyword, pageable));
    }
}
