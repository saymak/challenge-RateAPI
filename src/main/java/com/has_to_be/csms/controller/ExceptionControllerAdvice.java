package com.has_to_be.csms.controller;

import com.has_to_be.csms.dto.ApiExceptionResponseDTO;
import com.has_to_be.csms.exception.BusinessException;
import com.has_to_be.csms.util.MessageSourceUtility;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
@Log4j2
public class ExceptionControllerAdvice {


    public ExceptionControllerAdvice(@Qualifier("businessExceptionMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    private MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiExceptionResponseDTO> businessExceptionHandler(BusinessException e,
                                                                            Locale locale,
                                                                            HttpServletRequest request) {
        log.error("BusinessException", e);
        String exceptionMessage = MessageSourceUtility.getMessageByMessageKey(messageSource,
                e.getMessageKey(),
                locale);
        ApiExceptionResponseDTO apiExceptionResponse = new ApiExceptionResponseDTO(request.getMethod(),
                e.getStatusCode().value(),
                exceptionMessage,
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponse, e.getStatusCode());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e,
                                                         Locale locale,
                                                         HttpServletRequest request) {
        log.error("RuntimeException", e);
        HttpStatus httpStatus;
        String exceptionMessage;
        httpStatus = HttpStatus.BAD_REQUEST;
        exceptionMessage = MessageSourceUtility.getMessageByMessageKey(messageSource,
                BusinessException.DEFAULT_BAD_REQUEST_MESSAGE_KEY,
                locale);
        ApiExceptionResponseDTO apiExceptionResponse = new ApiExceptionResponseDTO(request.getMethod(),
                httpStatus.value(),
                exceptionMessage,
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e,
                                                  Locale locale,
                                                  HttpServletRequest request) {
        log.error("Exception", e);
        HttpStatus httpStatus;
        String exceptionMessage;
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        exceptionMessage = MessageSourceUtility.getMessageByMessageKey(messageSource,
                BusinessException.DEFAULT_INTERNAL_SERVER_ERROR_KEY,
                locale);
        ApiExceptionResponseDTO apiExceptionResponse = new ApiExceptionResponseDTO(request.getMethod(),
                httpStatus.value(),
                exceptionMessage,
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

}
