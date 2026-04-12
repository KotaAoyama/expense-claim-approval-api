package com.kota.expenseclaimapprovalapi.repository;

import com.kota.expenseclaimapprovalapi.entity.ExpenseClaimEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryExpenseClaimRepositoryImpl implements ExpenseClaimRepository {

    private final List<ExpenseClaimEntity> expenseClaimEntities = new ArrayList<>();

    @Override
    public ExpenseClaimEntity saveExpenseClaim(ExpenseClaimEntity entity) {
        expenseClaimEntities.add(entity);
        return entity;
    }

    @Override
    public List<ExpenseClaimEntity> getExpenseClaimsByUserId(String userId) {
        return expenseClaimEntities.stream().filter(x -> Objects.equals(x.getUserId(), userId)).toList();
    }

    @Override
    public ExpenseClaimEntity getExpenseClaimById(String expenseClaimId) {
        return expenseClaimEntities.stream()
                .filter(x -> Objects.equals(x.getExpenseClaimId(), expenseClaimId))
                .findFirst().orElseThrow();
    }
}
