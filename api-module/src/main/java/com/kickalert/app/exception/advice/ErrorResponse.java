package com.kickalert.app.exception.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String message;
    private final String operationCode;
    private final String data;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode dataCode, String dataDetail){
        return ResponseEntity
                .status(dataCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .operationCode(dataCode.getCode())
                        .message(dataCode.getMessage())
                        .data(dataDetail)
                        .build());
    }

    public static String toJsonString(ErrorCode dataCode, String dataDetail) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(ErrorResponse.builder()
                .operationCode(dataCode.getCode())
                .message(dataCode.getMessage())
                .data(dataDetail)
                .build());
    }
}
