package com.my_virtual_space.staffing.contexts;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Classe di utilità per la gestione dei log
 */

@Aspect
@Component
public class AspectLogging {
    private static final Logger log = LoggerFactory.getLogger(AspectLogging.class);

    @Pointcut("within(com.my_virtual_space.staffing.controllers..*) ||" +
            "within(com.my_virtual_space.staffing.repositories..*) ||" +
            "within(com.my_virtual_space.staffing.services..*)")
    public void loggingOperation() {
    }

    @Around("loggingOperation()")
    public Object logAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.trace("The method " + proceedingJoinPoint.getSignature() + " begins.");

        Object result;

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception occurred while executing method " + proceedingJoinPoint.getSignature());
            log.error("##############################    Exception.printStackTrace()    ##############################");
            log.error("Exception detail:", e);
            log.error("##############################  END Exception.printStackTrace()  ##############################");

            throw e;
        }

        log.trace("The method " + proceedingJoinPoint.getSignature() + " ends.");
        return result;
    }

}
