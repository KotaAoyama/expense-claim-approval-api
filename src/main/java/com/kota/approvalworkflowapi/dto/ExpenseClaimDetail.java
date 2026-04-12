package com.kota.approvalworkflowapi.dto;

import com.kota.approvalworkflowapi.common.ExpenseClaimStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExpenseClaimDetail {
    private String expenseClaimId;
    private String userId;
    private String userName;
    private ExpenseClaimStatus status;
    private String title;
    private String description;
    private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
