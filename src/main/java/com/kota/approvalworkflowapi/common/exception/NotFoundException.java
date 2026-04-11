package com.kota.approvalworkflowapi.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final int statusCode = 404;

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
