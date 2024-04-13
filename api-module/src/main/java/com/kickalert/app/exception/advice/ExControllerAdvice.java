package com.kickalert.app.exception.advice;

import com.kickalert.app.exception.UserCustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.kickalert.app.exception.advice.ErrorCode.*;

@Slf4j
@RestControllerAdvice
//@EnableWebMvc
public class ExControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentExHandler(IllegalArgumentException e, HttpServletRequest request){
        log.error("[exHandler] request info : {}, {}", request.getRequestURI(), request.getMethod());
        log.error("[illegalArgumentExHandler] ex: ", e);
        return ErrorResponse.toResponseEntity(BAD_REQUEST_KICK, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentExHandler(MethodArgumentNotValidException e, HttpServletRequest request){
        log.error("[exHandler] request info : {}, {}", request.getRequestURI(), request.getMethod());
        log.error("[methodArgumentExHandler] ex: ", e);
        return ErrorResponse.toResponseEntity(BAD_REQUEST_KICK, e.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> typeMismatchExHandler(TypeMismatchException e, HttpServletRequest request){
        log.error("[exHandler] request info : {}, {}", request.getRequestURI(), request.getMethod());
        log.error("[typeMismatchExHandler] ex: ", e);
        return ErrorResponse.toResponseEntity(BAD_REQUEST_KICK, e.getMessage());
    }

    @ExceptionHandler(UserCustomException.class)
    public ResponseEntity<ErrorResponse> userCustomExHandler(UserCustomException e, HttpServletRequest request){
        log.error("[exHandler] request info : {}, {}", request.getRequestURI(), request.getMethod());
        log.error("[userCustomExHandler] ex: ", e);
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getMessage());
    }

    /**
     * 잘못된 url 접근시 처리 exception
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> noHandlerFoundExHandler(NoHandlerFoundException e, HttpServletRequest request){
        log.error("[exHandler] request info : {}, {}", request.getRequestURI(), request.getMethod());
        log.error("[noHandlerFoundExHandler] ex: ", e);
        return ErrorResponse.toResponseEntity(NOT_FOUND_KICK, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exHandler(Exception e, HttpServletRequest request){
        log.error("[exHandler] request info : {}, {}", request.getRequestURI(), request.getMethod());
        log.error("[exHandler] ex: ", e);
        return ErrorResponse.toResponseEntity(INTERNAL_SERVER_ERROR_KICK, e.getMessage());
    }
}
