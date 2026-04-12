package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.ExpenseClaimStatus;
import com.kota.approvalworkflowapi.dto.ExpenseClaimDetail;
import com.kota.approvalworkflowapi.dto.ExpenseClaimInput;
import com.kota.approvalworkflowapi.dto.ExpenseClaimSummary;
import com.kota.approvalworkflowapi.entity.ExpenseClaimEntity;
import com.kota.approvalworkflowapi.exception.NotFoundException;
import com.kota.approvalworkflowapi.exception.StatusConflictException;
import com.kota.approvalworkflowapi.repository.ExpenseClaimRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ExpenseClaimService {

    // Phase1では固定値
    private static final String USER_ID = "userId1234";
    private static final String USER_NAME = "user1";

    private final ExpenseClaimRepository expenseClaimRepository;

    public ExpenseClaimService(ExpenseClaimRepository expenseClaimRepository) {
        this.expenseClaimRepository = expenseClaimRepository;
    }

    public ExpenseClaimDetail createExpenseClaim(ExpenseClaimInput input) {
        String expenseClaimId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        ExpenseClaimEntity savedEntity = expenseClaimRepository.saveExpenseClaim(
                ExpenseClaimEntity.builder()
                        .expenseClaimId(expenseClaimId)
                        .userId(USER_ID)
                        .status(ExpenseClaimStatus.DRAFT)
                        .title(input.getTitle())
                        .description(input.getDescription())
                        .amount(input.getAmount())
                        .createdAt(now)
                        .updatedAt(now)
                        .build());
        return toExpenseClaimDetail(savedEntity);
    }

    public List<ExpenseClaimSummary> getExpenseClaims() {
        List<ExpenseClaimEntity> expenseClaimEntities = expenseClaimRepository.getExpenseClaimsByUserId(USER_ID);
        return expenseClaimEntities.stream()
                .map(entity -> ExpenseClaimSummary.builder()
                        .expenseClaimId(entity.getExpenseClaimId())
                        .userName(USER_NAME)
                        .title(entity.getTitle())
                        .amount(entity.getAmount())
                        .status(entity.getStatus())
                        .createdAt(entity.getCreatedAt())
                        .build()).toList();
    }

    public ExpenseClaimDetail getExpenseClaimById(String expenseClaimId) {
        ExpenseClaimEntity expenseClaimEntity = getExpenseClaimOrThrow(expenseClaimId);
        return toExpenseClaimDetail(expenseClaimEntity);
    }

    public ExpenseClaimDetail submitExpenseClaim(String expenseClaimId) {
        ExpenseClaimEntity expenseClaimEntity = getExpenseClaimOrThrow(expenseClaimId);
        if (!expenseClaimEntity.getStatus().canSubmit()) {
            throw new StatusConflictException("Only DRAFT expense claims can be submitted");
        }
        expenseClaimEntity.changeStatus(ExpenseClaimStatus.SUBMITTED);
        ExpenseClaimEntity updatedEntity = expenseClaimRepository.saveExpenseClaim(expenseClaimEntity);
        return toExpenseClaimDetail(updatedEntity);
    }

    public ExpenseClaimDetail approveExpenseClaim(String expenseClaimId) {
        ExpenseClaimEntity expenseClaimEntity = getExpenseClaimOrThrow(expenseClaimId);
        if (!expenseClaimEntity.getStatus().canApprove()) {
            throw new StatusConflictException("Only SUBMITTED expense claims can be approved");
        }
        expenseClaimEntity.changeStatus(ExpenseClaimStatus.APPROVED);
        ExpenseClaimEntity updatedEntity = expenseClaimRepository.saveExpenseClaim(expenseClaimEntity);
        return toExpenseClaimDetail(updatedEntity);
    }

    public ExpenseClaimDetail rejectExpenseClaim(String expenseClaimId) {
        ExpenseClaimEntity expenseClaimEntity = getExpenseClaimOrThrow(expenseClaimId);
        if (!expenseClaimEntity.getStatus().canReject()) {
            throw new StatusConflictException("Only SUBMITTED expense claims can be rejected");
        }
        expenseClaimEntity.changeStatus(ExpenseClaimStatus.REJECTED);
        ExpenseClaimEntity updatedEntity = expenseClaimRepository.saveExpenseClaim(expenseClaimEntity);
        return toExpenseClaimDetail(updatedEntity);
    }

    private ExpenseClaimEntity getExpenseClaimOrThrow(String expenseClaimId) {
        try {
            return expenseClaimRepository.getExpenseClaimById(expenseClaimId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("No expense claim is found");
        }
    }

    private ExpenseClaimDetail toExpenseClaimDetail(ExpenseClaimEntity entity) {
        return ExpenseClaimDetail.builder()
                .expenseClaimId(entity.getExpenseClaimId())
                .userId(entity.getUserId())
                .userName(USER_NAME) // TODO
                .status(entity.getStatus())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
