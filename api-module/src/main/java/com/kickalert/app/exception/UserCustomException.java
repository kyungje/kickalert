package com.kickalert.app.exception;

import com.kickalert.app.exception.advice.ErrorCode;
import lombok.Getter;

@Getter
public class UserCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserCustomException(ErrorCode errorCode , String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserCustomException(ErrorCode errorCode , String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
