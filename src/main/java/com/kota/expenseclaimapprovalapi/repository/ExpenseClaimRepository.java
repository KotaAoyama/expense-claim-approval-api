package com.kota.expenseclaimapprovalapi.repository;

import com.kota.expenseclaimapprovalapi.entity.ExpenseClaimEntity;

import java.util.List;

public interface ExpenseClaimRepository {
    ExpenseClaimEntity saveExpenseClaim(ExpenseClaimEntity entity);

    List<ExpenseClaimEntity> getExpenseClaimsByUserId(String userId);

    ExpenseClaimEntity getExpenseClaimById(String expenseClaimId);
}
