package com.kota.approvalworkflowapi.entity;

import com.kota.approvalworkflowapi.common.ExpenseClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "expense_claims")
public class ExpenseClaimEntity {

    @Id
    private String expenseClaimId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private ExpenseClaimStatus status;

    private String title;

    private String description;

    private int amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void changeStatus(ExpenseClaimStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
