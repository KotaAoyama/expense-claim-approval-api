package com.kota.approvalworkflowapi.common;

public enum RequestStatus {
    DRAFT, SUBMITTED, APPROVED, REJECTED;

    public boolean canSubmit() {
        return this == DRAFT;
    }

    public boolean canApprove() {
        return this == SUBMITTED;
    }

    public boolean canReject() {
        return this == SUBMITTED;
    }
}
