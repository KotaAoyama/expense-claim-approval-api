package com.kota.approvalworkflowapi.common;

public enum RequestStatus {
    DRAFT, SUBMITTED, APPROVED, REJECTED;

    public boolean isSubmittable() {
        return this == DRAFT;
    }
}
