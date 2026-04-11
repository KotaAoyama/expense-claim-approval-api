package com.kota.approvalworkflowapi.common.exception;

import lombok.Getter;

@Getter
public class StatusConflictException extends RuntimeException {
    private final int statusCode = 409;

    public StatusConflictException(String errorMessage) {
        super(errorMessage);
    }
}
