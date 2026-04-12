package com.kota.expenseclaimapprovalapi.dto.response;

import com.kota.expenseclaimapprovalapi.common.ExpenseClaimStatus;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExpenseClaimDetailResponse {
    private String expenseClaimId;
    private String userId;
    private String userName;
    private ExpenseClaimStatus status;
    private String title;
    private String description;
    private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ExpenseClaimDetailResponse from(ExpenseClaimDetail expenseClaimDetail) {
        return ExpenseClaimDetailResponse.builder()
                .expenseClaimId(expenseClaimDetail.getExpenseClaimId())
                .userId(expenseClaimDetail.getUserId())
                .userName(expenseClaimDetail.getUserName())
                .status(expenseClaimDetail.getStatus())
                .title(expenseClaimDetail.getTitle())
                .description(expenseClaimDetail.getDescription())
                .amount(expenseClaimDetail.getAmount())
                .createdAt(expenseClaimDetail.getCreatedAt())
                .updatedAt(expenseClaimDetail.getUpdatedAt())
                .build();
    }
}
