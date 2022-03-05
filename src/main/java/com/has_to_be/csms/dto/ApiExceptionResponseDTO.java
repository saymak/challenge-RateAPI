package com.has_to_be.csms.dto;

import lombok.Data;


@Data
public class ApiExceptionResponseDTO {

    public ApiExceptionResponseDTO(String method, int status, String path) {
        this.method = method;
        this.status = status;
        this.path = path;
    }

    private String method;

    private int status;

    private String message;

    private String path;

}
