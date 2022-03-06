package com.has_to_be.csms.controller;

import com.has_to_be.csms.dto.ApiExceptionResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static com.has_to_be.csms.util.MessageSourceUtility.MESSAGE_MISSING_IN_BUNDLE;
import static com.has_to_be.csms.util.MessageSourceUtility.getMessage;

@ControllerAdvice
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionControllerAdvice {

    private final MessageSource messageSource;

    public ValidationExceptionControllerAdvice(@Qualifier("validationMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponseDTO> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException e, Locale locale, HttpServletRequest request) {
        log.error("validation exception", e);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String messageKey = errors.isEmpty() ? MESSAGE_MISSING_IN_BUNDLE : errors.get(0).getDefaultMessage();
        ApiExceptionResponseDTO apiExceptionResponse =
                new ApiExceptionResponseDTO(request.getMethod(), httpStatus.value(), request.getRequestURI());
        apiExceptionResponse.setMessage(getMessage(messageSource, messageKey, locale));
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

}
