package com.kota.expenseclaimapprovalapi.entity;

import com.kota.expenseclaimapprovalapi.common.ExpenseClaimStatus;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimInput;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

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

    private Integer amount;

    private String reviewerComment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void changeStatus(ExpenseClaimStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void addReviewerComment(String reviewerComment) {
        this.reviewerComment = reviewerComment;
        this.updatedAt = LocalDateTime.now();
    }

    public void edit(ExpenseClaimInput input) {
        boolean isUpdated = false;
        if (input.getTitle() != null && !Objects.equals(this.title, input.getTitle())) {
            this.title = input.getTitle();
            isUpdated = true;
        }
        if (input.getDescription() != null && !Objects.equals(this.description, input.getDescription())) {
            this.description = input.getDescription();
            isUpdated = true;
        }
        if (input.getAmount() != null && !Objects.equals(this.amount, input.getAmount())) {
            this.amount = input.getAmount();
            isUpdated = true;
        }
        if (isUpdated) this.updatedAt = LocalDateTime.now();
    }

    public boolean validateWhenSubmit() {
        if (this.title == null || this.title.isBlank()) return false;
        if (this.amount == null) return false;
        return true;
    }
}
