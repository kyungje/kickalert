package com.kickalert.app.controller;

import com.kickalert.app.dto.external.ResultExDto;

import java.util.Map;

public class BaseController {
    ResultExDto<Object> simpleResult(Object data) {
        return ResultExDto.builder()
                .message("Success")
                .operationCode("OK")
                .data(data)
                .build();
    }

    ResultExDto<Object> ResultWithOperation(String message, String operationCode, Object data) {
        return ResultExDto.builder()
                .message(message)
                .operationCode(operationCode)
                .data(data)
                .build();
    }
}
