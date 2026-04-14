package com.kota.expenseclaimapprovalapi.dto;

import com.kota.expenseclaimapprovalapi.common.ExpenseClaimStatus;
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
    private Integer amount;
    private String reviewerComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
