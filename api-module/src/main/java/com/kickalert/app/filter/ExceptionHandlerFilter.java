package com.kickalert.app.filter;

import com.kickalert.app.exception.UserCustomException;
import com.kickalert.app.exception.advice.ErrorCode;
import com.kickalert.app.exception.advice.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter에서 Exception 발생시 처리하는 로직
 */
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UserCustomException e){
            log.error("UserCustomException exception handler filter: {} " , e.getMessage(), e);
            setErrorResponse(e.getErrorCode(), response, e);
        } catch (RuntimeException e){
            log.error("RuntimeException exception handler filter: {} " , e.getMessage(), e);
            setErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR_KICK, response, e);
        }
    }

    private void setErrorResponse(ErrorCode errorCode, HttpServletResponse response, Throwable ex){
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");

        try {
            String jsonResponseBody = ErrorResponse.toJsonString(errorCode, ex.getMessage());
            log.info("jsonResponseBody : {}" , jsonResponseBody);
            response.getWriter().write(jsonResponseBody);
        } catch (IOException e) {
            log.error("setErrorResponse method: IOException : {} " , e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
