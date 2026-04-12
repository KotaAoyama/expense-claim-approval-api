package com.kota.expenseclaimapprovalapi.dto;

import com.kota.expenseclaimapprovalapi.common.ExpenseClaimStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExpenseClaimSummary {
    private String expenseClaimId;
    private String userName;
    private ExpenseClaimStatus status;
    private String title;
    private int amount;
    private LocalDateTime createdAt;
}
