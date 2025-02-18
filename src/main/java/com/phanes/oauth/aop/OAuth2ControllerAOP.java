package com.phanes.oauth.aop;

import com.phanes.oauth.domain.enums.SocialType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Aspect
@Component
public class OAuth2ControllerAOP {

    @Around("execution(* com.phanes.oauth.controller.OAuth2Controller.socialLoginCallback(..))")
    public Object validateSocialType(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String socialType = (String) args[2];

        try {
            Enum.valueOf(SocialType.class, socialType.toUpperCase(Locale.KOREA));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid social type");
        }

        return joinPoint.proceed();
    }
}