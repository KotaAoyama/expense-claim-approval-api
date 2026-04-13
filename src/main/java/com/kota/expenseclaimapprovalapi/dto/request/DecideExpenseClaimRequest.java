package com.kota.expenseclaimapprovalapi.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DecideExpenseClaimRequest {
    private String reviewerComment;
}
