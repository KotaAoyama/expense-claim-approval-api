package com.kota.expenseclaimapprovalapi.repository;

import com.kota.expenseclaimapprovalapi.entity.ExpenseClaimEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class JpaExpenseClaimRepositoryImpl implements ExpenseClaimRepository {

    private final SpringDataJpaExpenseClaimRepository springDataJpaExpenseClaimRepository;

    public JpaExpenseClaimRepositoryImpl(SpringDataJpaExpenseClaimRepository springDataJpaExpenseClaimRepository) {
        this.springDataJpaExpenseClaimRepository = springDataJpaExpenseClaimRepository;
    }

    @Override
    public ExpenseClaimEntity saveExpenseClaim(ExpenseClaimEntity entity) {
        return springDataJpaExpenseClaimRepository.save(entity);
    }

    @Override
    public List<ExpenseClaimEntity> getExpenseClaimsByUserId(String userId) {
        return springDataJpaExpenseClaimRepository.findByUserId(userId);
    }

    @Override
    public ExpenseClaimEntity getExpenseClaimById(String expenseClaimId) {
        return springDataJpaExpenseClaimRepository.findById(expenseClaimId).orElseThrow();
    }
}
