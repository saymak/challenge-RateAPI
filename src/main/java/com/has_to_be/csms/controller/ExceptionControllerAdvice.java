package com.has_to_be.csms.controller;

import com.has_to_be.csms.dto.ApiExceptionResponseDTO;
import com.has_to_be.csms.exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

import static com.has_to_be.csms.exception.BusinessException.DEFAULT_INTERNAL_SERVER_ERROR_KEY;
import static com.has_to_be.csms.util.MessageSourceUtility.getMessage;

@ControllerAdvice
@Log4j2
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    public ExceptionControllerAdvice(@Qualifier("businessExceptionMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiExceptionResponseDTO> businessExceptionHandler(BusinessException e,
                                                                            Locale locale,
                                                                            HttpServletRequest request) {
        log.error("BusinessException", e);
        String exceptionMessage = getMessage(messageSource, e.getMessageKey(), locale);
        ApiExceptionResponseDTO apiExceptionResponse = new ApiExceptionResponseDTO(request.getMethod(),
                e.getStatusCode().value(),
                exceptionMessage,
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponse, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unhandledException(Exception e,
                                                     Locale locale,
                                                     HttpServletRequest request) {
        HttpStatus httpStatus;
        String message;
        if (e instanceof ServletException) {
            message = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;
            log.error("Exception", e);
        } else {
            UUID incidentUUID = UUID.randomUUID();
            log.error(String.format("unknown exception %s", incidentUUID), e);
            message = getMessage(messageSource, DEFAULT_INTERNAL_SERVER_ERROR_KEY, locale, incidentUUID);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ApiExceptionResponseDTO apiExceptionResponse = new ApiExceptionResponseDTO(request.getMethod(),
                httpStatus.value(),
                message,
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

}
