package com.kickalert.app.aop;

import com.kickalert.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 컨트롤러 호출 전, 후 파라미터 및 리턴값 출력한다.
 */
@Slf4j
@Aspect
@Component
public class ParameterLogAop {
    @Pointcut("execution(* com.kickalert.app.controller..*.*(..))")
    private void controllerCut() {}

    /**
     * 컨트롤러 호출 전 파라미터 값 출력한다.
     * @param joinPoint
     */
    @Before("controllerCut()")
    public void before(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        log.info("Method exec : {}", method.getName());

        Object[] args = joinPoint.getArgs();

        Arrays.stream(args).forEach(arg ->{
            if(!CommonUtils.isEmpty(arg)){
                log.info("type : {}, value : {}", arg.getClass().getSimpleName(), arg);
            }
        });
    }

    /**
     * 컨트롤러 호출 후
     * @param joinPoint
     * @param obj
     */
    @AfterReturning(value = "controllerCut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj){
        log.info("Return value : {}", obj);
    }
}
