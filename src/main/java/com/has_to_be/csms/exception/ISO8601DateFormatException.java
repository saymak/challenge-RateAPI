package com.has_to_be.csms.exception;

import org.springframework.http.HttpStatus;

public class ISO8601DateFormatException extends BusinessException {

    public ISO8601DateFormatException(Exception e) {
        super(e);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
