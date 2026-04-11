package com.kota.approvalworkflowapi.exception;

public class StatusConflictException extends RuntimeException {
    public StatusConflictException(String errorMessage) {
        super(errorMessage);
    }
}
