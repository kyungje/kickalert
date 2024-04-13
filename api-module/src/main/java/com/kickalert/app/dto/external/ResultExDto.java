package com.kickalert.app.dto.external;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResultExDto<T> {
    private final String message;
    private final String operationCode;
    private final T data;

    @Builder
    public ResultExDto(String message, String operationCode, T data) {
        this.message = message;
        this.operationCode = operationCode;
        this.data = data;
    }
}
