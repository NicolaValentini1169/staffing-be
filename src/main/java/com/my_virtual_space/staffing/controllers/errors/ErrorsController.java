package com.my_virtual_space.staffing.controllers.errors;

import com.my_virtual_space.staffing.constants.ErrorsConstants;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.rmi.UnexpectedException;

@ControllerAdvice
public class ErrorsController extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorsController.class);

    private void logMessageAtLevel(String message, LogLevel level, Object... args) {
        if (level == LogLevel.ERROR) {
            log.error(message, args);
        } else if (level == LogLevel.WARN) {
            log.warn(message, args);
        } else if (level == LogLevel.INFO) {
            log.info(message, args);
        } else if (level == LogLevel.DEBUG) {
            log.debug(message, args);
        } else if (level == LogLevel.TRACE) {
            log.trace(message, args);
        }
    }

    private void logExceptionStackTrace(Exception e, WebRequest request) {
        log.error("Exception occurred calling " + request.getDescription(false));
        log.error("##############################    Exception.printStackTrace()    ##############################");
        log.error("Exception details:", e);
        log.error("##############################  END Exception.printStackTrace()  ##############################");
    }

    private void logOnlyExceptionMessage(Exception e, WebRequest request) {
        logOnlyExceptionMessage(e, request, LogLevel.WARN);
    }

    private void logOnlyExceptionMessage(Exception e, WebRequest request, LogLevel level) {
        logMessageAtLevel("##############################    Exception.message()    ##############################", level);
        logMessageAtLevel("Exception occurred calling " + request.getDescription(false), level);
        logMessageAtLevel(String.format("Exception message: %s", e.getMessage()), level);
        logMessageAtLevel("##############################  END Exception.message()  ##############################", level);
    }

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<String> handleUnexpectedException(Exception e, WebRequest request) {
        logExceptionStackTrace(e, request);
        String message = e.getMessage() == null
                ? ErrorsConstants.INTERNAL_SERVER_ERROR
                : e.getMessage();

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCrendential(Exception e, WebRequest request) {
//        Viene già loggata grazie all'aspect che gira attorno al metodo nel controller, per questo qui non serve
//        logOnlyExceptionMessage(e, request);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity<String> handleClientAbortException(Exception e, WebRequest request) {
        logOnlyExceptionMessage(e, request);
        return null;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e, WebRequest request) {
        logExceptionStackTrace(e, request);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
