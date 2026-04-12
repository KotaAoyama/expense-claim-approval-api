package com.kota.approvalworkflowapi.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateExpenseClaimRequest {
    private String title;
    private String description;
    private int amount;
}
