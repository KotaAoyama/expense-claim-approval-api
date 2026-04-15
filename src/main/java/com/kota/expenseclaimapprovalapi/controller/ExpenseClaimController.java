package com.kota.expenseclaimapprovalapi.controller;

import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimDetail;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimInput;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimSummary;
import com.kota.expenseclaimapprovalapi.dto.request.CreateExpenseClaimRequest;
import com.kota.expenseclaimapprovalapi.dto.request.DecideExpenseClaimRequest;
import com.kota.expenseclaimapprovalapi.dto.request.EditExpenseClaimRequest;
import com.kota.expenseclaimapprovalapi.dto.response.ExpenseClaimDetailResponse;
import com.kota.expenseclaimapprovalapi.dto.response.ExpenseClaimSummaryResponse;
import com.kota.expenseclaimapprovalapi.service.ExpenseClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expense Claim", description = "Expense claim workflow APIs")
@RestController
@RequestMapping("/expense-claims")
public class ExpenseClaimController {

    private final ExpenseClaimService expenseClaimService;

    public ExpenseClaimController(ExpenseClaimService expenseClaimService) {
        this.expenseClaimService = expenseClaimService;
    }

    @Operation(summary = "Create a new expense claim")
    @PostMapping
    public ExpenseClaimDetailResponse createExpenseClaim(@RequestBody @Valid CreateExpenseClaimRequest req) {
        ExpenseClaimInput input = ExpenseClaimInput.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .amount(req.getAmount())
                .build();
        ExpenseClaimDetail savedExpenseClaim = expenseClaimService.createExpenseClaim(input);
        return ExpenseClaimDetailResponse.from(savedExpenseClaim);
    }

    @Operation(summary = "Get expense claim list")
    @GetMapping
    public List<ExpenseClaimSummaryResponse> getExpenseClaims() {
        List<ExpenseClaimSummary> expenseClaimSummaries = expenseClaimService.getExpenseClaims();
        return expenseClaimSummaries.stream().map(expenseClaimSummary -> ExpenseClaimSummaryResponse.builder()
                .expenseClaimId(expenseClaimSummary.getExpenseClaimId())
                .userName(expenseClaimSummary.getUserName())
                .status(expenseClaimSummary.getStatus())
                .title(expenseClaimSummary.getTitle())
                .amount(expenseClaimSummary.getAmount())
                .createdAt(expenseClaimSummary.getCreatedAt())
                .build()).toList();
    }

    @Operation(summary = "Get expense claim detail")
    @GetMapping("/{expenseClaimId}")
    public ExpenseClaimDetailResponse getExpenseClaimById(@PathVariable String expenseClaimId) {
        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.getExpenseClaimById(expenseClaimId);
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }

    @Operation(summary = "Edit a draft expense claim")
    @PatchMapping("{expenseClaimId}")
    public ExpenseClaimDetailResponse editExpenseClaimById(@PathVariable String expenseClaimId,
                                                           @RequestBody @Valid EditExpenseClaimRequest req) {
        ExpenseClaimInput input = ExpenseClaimInput.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .amount(req.getAmount())
                .build();
        ExpenseClaimDetail savedExpenseClaim = expenseClaimService.editExpenseClaim(expenseClaimId, input);
        return ExpenseClaimDetailResponse.from(savedExpenseClaim);
    }

    @Operation(summary = "Submit a draft expense claim")
    @PostMapping("/{expenseClaimId}/submit")
    public ExpenseClaimDetailResponse submitExpenseClaim(@PathVariable String expenseClaimId) {
        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.submitExpenseClaim(expenseClaimId);
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }

    @Operation(summary = "Approve a submitted expense claim")
    @PostMapping("/{expenseClaimId}/approve")
    public ExpenseClaimDetailResponse approveExpenseClaim(@PathVariable String expenseClaimId,
                                                          @RequestBody DecideExpenseClaimRequest req) {
        ExpenseClaimDetail expenseClaimDetail =
                expenseClaimService.approveExpenseClaim(expenseClaimId, req.getReviewerComment());
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }

    @Operation(summary = "Reject a submitted expense claim")
    @PostMapping("/{expenseClaimId}/reject")
    public ExpenseClaimDetailResponse rejectExpenseClaim(@PathVariable String expenseClaimId,
                                                         @RequestBody DecideExpenseClaimRequest req) {
        ExpenseClaimDetail expenseClaimDetail =
                expenseClaimService.rejectExpenseClaim(expenseClaimId, req.getReviewerComment());
        return ExpenseClaimDetailResponse.from(expenseClaimDetail);
    }
}
