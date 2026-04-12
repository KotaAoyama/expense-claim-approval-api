package com.kota.expenseclaimapprovalapi.exception;

public class StatusConflictException extends RuntimeException {
    public StatusConflictException(String errorMessage) {
        super(errorMessage);
    }
}
