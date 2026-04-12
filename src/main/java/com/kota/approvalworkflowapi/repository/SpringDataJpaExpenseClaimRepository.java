package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.ExpenseClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaExpenseClaimRepository extends JpaRepository<ExpenseClaimEntity, String> {
    List<ExpenseClaimEntity> findByUserId(String userId);
}
