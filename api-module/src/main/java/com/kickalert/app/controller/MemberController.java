package com.kickalert.app.controller;

import com.kickalert.app.dto.external.MemberExDto;
import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController extends BaseController {
    private final MemberService memberService;
    @PostMapping(value = "/initUser")
    public ResultExDto<Object> initUser(@RequestBody MemberExDto.ReqInitUser reqInitUser) {
        return simpleResult(memberService.initUser(reqInitUser.fcmToken()));
    }
}
