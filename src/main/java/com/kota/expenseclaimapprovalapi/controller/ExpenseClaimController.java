package com.kota.expenseclaimapprovalapi.controller;

import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimDetail;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimInput;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimSummary;
import com.kota.expenseclaimapprovalapi.dto.request.CreateExpenseClaimRequest;
import com.kota.expenseclaimapprovalapi.dto.response.ExpenseClaimDetailResponse;
import com.kota.expenseclaimapprovalapi.dto.response.ExpenseClaimSummaryResponse;
import com.kota.expenseclaimapprovalapi.service.ExpenseClaimService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense-claims")
public class ExpenseClaimController {

    private final ExpenseClaimService expenseClaimService;

    public ExpenseClaimController(ExpenseClaimService expenseClaimService) {
        this.expenseClaimService = expenseClaimService;
    }

    @PostMapping
    public ExpenseClaimDetailResponse createExpenseClaim(@RequestBody CreateExpenseClaimRequest req) {
        ExpenseClaimInput input = ExpenseClaimInput.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .amount(req.getAmount())
                .build();
        ExpenseClaimDetail savedExpenseClaim = expenseClaimService.createExpenseClaim(input);
        return ExpenseClaimDetailResponse.from(savedExpenseClaim);
    }

    @GetMapping
    public List<ExpenseClaimSummaryResponse> getExpenseClaims() {
        List<ExpenseClaimSummary> expenseClaimSummaries = expenseClaimService.getExpenseClaims();
        return expenseClaimSummaries.stream().map(expenseClaimSummary -> ExpenseClaimSummaryResponse.builder()
                .expenseClaimId(expenseClaimSummary.getExpenseClaimId())
                .userName(expenseClaimSummary.getUserName())
                .status(expenseClaimSummary.getStatus())
                .title(expenseClaimSummary.getTitle())
                .createdAt(expenseClaimSummary.getCreatedAt())
                .build()).toList();
    }

    @GetMapping("/{expenseClaimId}")
    public ExpenseClaimDetailResponse getExpenseClaimById(@PathVariable String expenseClaimId) {
        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.getExpenseClaimById(expenseClaimId);
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }

    @PostMapping("/{expenseClaimId}/submit")
    public ExpenseClaimDetailResponse submitExpenseClaim(@PathVariable String expenseClaimId) {
        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.submitExpenseClaim(expenseClaimId);
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }

    @PostMapping("/{expenseClaimId}/approve")
    public ExpenseClaimDetailResponse approveExpenseClaim(@PathVariable String expenseClaimId) {
        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.approveExpenseClaim(expenseClaimId);
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }

    @PostMapping("/{expenseClaimId}/reject")
    public ExpenseClaimDetailResponse rejectExpenseClaim(@PathVariable String expenseClaimId) {
        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.rejectExpenseClaim(expenseClaimId);
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }
}
