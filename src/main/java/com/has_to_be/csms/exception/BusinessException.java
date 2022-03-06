package com.has_to_be.csms.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    public static final String DEFAULT_BAD_REQUEST_MESSAGE_KEY = "backend.exception.data.format.is.invalid";

    public static final String DEFAULT_INTERNAL_SERVER_ERROR_KEY = "backend.exception.static.internal.server.error";

    protected BusinessException() {
    }

    protected BusinessException(Exception e) {
        super(e);
    }

    public abstract HttpStatus getStatusCode();

    public final String getMessageKey() {
        return this.getClass().getCanonicalName();
    }

}
