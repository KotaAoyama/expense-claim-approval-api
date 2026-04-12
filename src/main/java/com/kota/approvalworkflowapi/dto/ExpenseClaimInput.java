package com.kota.approvalworkflowapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpenseClaimInput {
    private String title;
    private String description;
    private int amount;
}
