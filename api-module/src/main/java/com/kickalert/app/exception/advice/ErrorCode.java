package com.kickalert.app.exception.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST_KICK(BAD_REQUEST, "BAD_REQUEST" ,"Bad Request has been sent to Server"), //400
    WRONG_URL_KICK(BAD_REQUEST, "The url is not correct" ,"URL is not supported"),
    UNAUTHORIZED_KICK(UNAUTHORIZED,"UNAUTHORIZED" ,"Can't find Requested User Info"), //401
    NOT_FOUND_RESOURCE_SCOPE_KICK(UNAUTHORIZED,"NOT_FOUND_RESOURCE_SCOPE" ,"This user authority scope can't be found"), //401
    INVALID_HEADER_KICK(UNAUTHORIZED,"INVALID_HEADER" ,"WRONG Requested User Info"), //401
    INVALID_TOKEN_KICK(UNAUTHORIZED,"INVALID_TOKEN" ,"WRONG Requested User Info"), //401
    FORBIDDEN_KICK(FORBIDDEN,"FORBIDDEN" ,"Don't have proper Authority for Requested job"), //403
    NOT_FOUND_KICK(NOT_FOUND,"NOT_FOUND" ,"Can't find Requested Info"), //404
    NOT_EXIST_ID_KICK(NOT_FOUND,"NOT_EXIST_ID" ,"Can't find Stored ID Info"), //404
    NOT_EXIST_FCMKEY_KICK(NOT_FOUND,"NOT_EXIST_FCMKEY" ,"Can't find Stored Fcm Info"), //404
    NOT_EXIST_MULTIPART_KICK(NOT_FOUND,"NOT_EXIST_MULTIPART" ,"Doesn't Exist multipart file"), //404
    FIRST_CATEGORY_ERROR_KICK(NOT_FOUND,"FIRST_CATEGORY_ERROR_KICK" ,"Can't find the first category data"), //404

    CONFLICT_KICK(CONFLICT, "DUPLICATE_RESOURCE" ,"Requested Data already exist"), //409

    INTERNAL_SERVER_ERROR_KICK(INTERNAL_SERVER_ERROR, "INTERNAL_ERROR" ,"Service is not available temporarily. Please visit after a while"),
    FILE_UPLOAD_FAIL_KICK(INTERNAL_SERVER_ERROR,"FILE_UPLOAD_FAIL" ,"File upload failed"),
    CREATE_IDENTIFICATION_ID_ERROR_KICK(INTERNAL_SERVER_ERROR,"CREATE_IDENTIFICATION_ID_ERROR_KICK" ,"Can't create identification id"),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
