package com.has_to_be.csms.exception;

import org.springframework.http.HttpStatus;

public class MismatchedStartAndEndMeterException extends BusinessException {

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

}
