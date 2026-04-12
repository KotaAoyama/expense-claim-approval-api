package com.kota.expenseclaimapprovalapi.repository;

import com.kota.expenseclaimapprovalapi.entity.ExpenseClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaExpenseClaimRepository extends JpaRepository<ExpenseClaimEntity, String> {
    List<ExpenseClaimEntity> findByUserId(String userId);
}
