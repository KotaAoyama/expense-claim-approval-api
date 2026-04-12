package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.ExpenseClaimEntity;

import java.util.List;

public interface ExpenseClaimRepository {
    ExpenseClaimEntity saveExpenseClaim(ExpenseClaimEntity entity);

    List<ExpenseClaimEntity> getExpenseClaimsByUserId(String userId);

    ExpenseClaimEntity getExpenseClaimById(String expenseClaimId);
}
