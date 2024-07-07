package com.kickalert.app.controller;

import com.kickalert.app.dto.external.AlarmExDto;
import com.kickalert.app.dto.external.ResultExDto;
import com.kickalert.app.dto.internal.AlarmInDto;
import com.kickalert.app.service.AlarmService;
import com.kickalert.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/alarm")
public class AlarmController extends BaseController {
    private final AlarmService alarmService;

    @GetMapping(value = "/alarmHistoryList")
    public ResultExDto<Object> alarmHistoryList(@RequestParam String userId, Pageable pageable) {
        if(CommonUtils.isEmpty(userId)) simpleResult("");
        return simpleResult(alarmService.alarmHistoryList(Long.parseLong(userId), pageable));
    }

    @PostMapping(value = "/alarmSetting")
    public ResultExDto<Object> alarmSetting(@RequestBody AlarmExDto.ReqAlarmSetting reqAlarmSetting) {
        AlarmInDto.ReqAlarmSetting inReqAlarmSetting
                = new AlarmInDto.ReqAlarmSetting(reqAlarmSetting.userId()
                                        , reqAlarmSetting.fixtureId()
                                        , reqAlarmSetting.playerId()
                                        , reqAlarmSetting.alarmType());
        return simpleResult(alarmService.alarmSetting(inReqAlarmSetting));
    }

    @GetMapping(value = "/activeAlarmList")
    public ResultExDto<Object> activeAlarmList(@RequestParam String userId, Pageable pageable) {
        if(CommonUtils.isEmpty(userId)) simpleResult("");
        return simpleResult(alarmService.activeAlarmList(Long.parseLong(userId), pageable));
    }

    @PostMapping(value = "/alarmTest")
    public ResultExDto<Object> alarmTest() {
        return simpleResult(alarmService.alarmTest());
    }
}
