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

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static com.has_to_be.csms.exception.BusinessException.DEFAULT_BAD_REQUEST_MESSAGE_KEY;
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e,
                                                         Locale locale,
                                                         HttpServletRequest request) {
        log.error("RuntimeException", e);
        HttpStatus httpStatus;
        httpStatus = HttpStatus.BAD_REQUEST;
        ApiExceptionResponseDTO apiExceptionResponseDTO = new ApiExceptionResponseDTO(request.getMethod(),
                httpStatus.value(),
                getMessage(messageSource, DEFAULT_BAD_REQUEST_MESSAGE_KEY, locale),
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponseDTO, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e,
                                                  Locale locale,
                                                  HttpServletRequest request) {
        log.error("Exception", e);
        HttpStatus httpStatus;
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiExceptionResponseDTO apiExceptionResponseDTO = new ApiExceptionResponseDTO(request.getMethod(),
                httpStatus.value(),
                getMessage(messageSource, DEFAULT_INTERNAL_SERVER_ERROR_KEY, locale),
                request.getRequestURI());
        return new ResponseEntity<>(apiExceptionResponseDTO, httpStatus);
    }

}
