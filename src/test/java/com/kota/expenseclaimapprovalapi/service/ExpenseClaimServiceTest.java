package com.kota.expenseclaimapprovalapi.service;

import com.kota.expenseclaimapprovalapi.common.ExpenseClaimStatus;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimDetail;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimInput;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimSummary;
import com.kota.expenseclaimapprovalapi.exception.NotFoundException;
import com.kota.expenseclaimapprovalapi.exception.StatusConflictException;
import com.kota.expenseclaimapprovalapi.repository.ExpenseClaimRepository;
import com.kota.expenseclaimapprovalapi.repository.InMemoryExpenseClaimRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseClaimServiceTest {

    ExpenseClaimRepository expenseClaimRepository;
    ExpenseClaimService expenseClaimService;

    @BeforeEach
    void setup() {
        expenseClaimRepository = new InMemoryExpenseClaimRepositoryImpl();
        expenseClaimService = new ExpenseClaimService(expenseClaimRepository);
    }

    @AfterEach
    void cleanup() {
        expenseClaimRepository = null;
        expenseClaimService = null;
    }

    @Test
    void createExpenseClaim_shouldReturnDraftDetail() {

        ExpenseClaimInput input = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail result = expenseClaimService.createExpenseClaim(input);

        assertNotNull(result);
        assertNotNull(result.getExpenseClaimId());
        assertEquals("交通費精算", result.getTitle());
        assertEquals(5000, result.getAmount());
        assertEquals(ExpenseClaimStatus.DRAFT, result.getStatus());
        assertEquals("userId1234", result.getUserId());
        assertEquals("user1", result.getUserName());
        assertNull(result.getReviewerComment());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void getExpenseClaims_shouldReturnSummaryList() {

        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        expenseClaimService.createExpenseClaim(input1);
        List<ExpenseClaimSummary> result = expenseClaimService.getExpenseClaims();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("交通費精算", result.getFirst().getTitle());
        assertEquals(5000, result.getFirst().getAmount());
        assertEquals(ExpenseClaimStatus.DRAFT, result.getFirst().getStatus());
        assertEquals("user1", result.getFirst().getUserName());
    }

    @Test
    void getExpenseClaimByID_shouldReturnDetail() {

        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail expenseClaimDetail = expenseClaimService.createExpenseClaim(input1);
        ExpenseClaimDetail result = expenseClaimService.getExpenseClaimById(expenseClaimDetail.getExpenseClaimId());

        assertNotNull(result);
        assertEquals(expenseClaimDetail.getExpenseClaimId(), result.getExpenseClaimId());
        assertEquals(expenseClaimDetail.getUserName(), result.getUserName());
        assertEquals("交通費精算", result.getTitle());
        assertEquals("4月分", result.getDescription());
        assertEquals(5000, result.getAmount());
        assertEquals(ExpenseClaimStatus.DRAFT, result.getStatus());
        assertEquals("userId1234", result.getUserId());
        assertEquals("user1", result.getUserName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void submitExpenseClaim_should_change_status_to_SUBMITTED() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);
        ExpenseClaimDetail result = expenseClaimService.submitExpenseClaim(draftExpenseClaim.getExpenseClaimId());

        assertNotNull(result);
        assertEquals(draftExpenseClaim.getExpenseClaimId(), result.getExpenseClaimId());
        assertEquals(ExpenseClaimStatus.SUBMITTED, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(result.getCreatedAt(), result.getUpdatedAt());
    }

    @Test
    void approveExpenseClaim_should_change_status_to_APPROVED() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();
        String reviewerComment = "OK!";

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);
        ExpenseClaimDetail submittedExpenseClaim = expenseClaimService.submitExpenseClaim(draftExpenseClaim.getExpenseClaimId());
        LocalDateTime submittedTime = submittedExpenseClaim.getUpdatedAt();
        ExpenseClaimDetail result = expenseClaimService
                .approveExpenseClaim(submittedExpenseClaim.getExpenseClaimId(), reviewerComment);

        assertNotNull(result);
        assertEquals(draftExpenseClaim.getExpenseClaimId(), result.getExpenseClaimId());
        assertEquals(ExpenseClaimStatus.APPROVED, result.getStatus());
        assertEquals("OK!", result.getReviewerComment());
        assertNotNull(submittedTime);
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(submittedTime, result.getUpdatedAt());
    }

    @Test
    void rejectExpenseClaim_should_change_status_to_REJECTED() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();
        String reviewerComment = "NG!";

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);
        ExpenseClaimDetail submittedExpenseClaim = expenseClaimService.submitExpenseClaim(draftExpenseClaim.getExpenseClaimId());
        LocalDateTime submittedTime = submittedExpenseClaim.getUpdatedAt();
        ExpenseClaimDetail result = expenseClaimService
                .rejectExpenseClaim(submittedExpenseClaim.getExpenseClaimId(), reviewerComment);

        assertNotNull(result);
        assertEquals(draftExpenseClaim.getExpenseClaimId(), result.getExpenseClaimId());
        assertEquals(ExpenseClaimStatus.REJECTED, result.getStatus());
        assertEquals("NG!", result.getReviewerComment());
        assertNotNull(submittedTime);
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(submittedTime, result.getUpdatedAt());
    }

    @Test
    void approveExpenseClaim_should_throw_StatusConflictException_when_status_is_DRAFT() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);

        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.approveExpenseClaim(draftExpenseClaim.getExpenseClaimId(), "OK!"));
    }

    @Test
    void rejectExpenseClaim_should_throw_StatusConflictException_when_status_is_DRAFT() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);

        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.rejectExpenseClaim(draftExpenseClaim.getExpenseClaimId(), "NG!"));
    }

    @Test
    void submitExpenseClaim_should_throw_StatusConflictException_when_status_is_SUBMITTED() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);
        ExpenseClaimDetail submittedExpenseClaim = expenseClaimService.submitExpenseClaim(draftExpenseClaim.getExpenseClaimId());

        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.submitExpenseClaim(submittedExpenseClaim.getExpenseClaimId()));
    }

    @Test
    void submitExpenseClaim_should_throw_NotFoundException_when_invalid_id_given() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        expenseClaimService.createExpenseClaim(input1);
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.submitExpenseClaim("invalid_id"));
    }

    @Test
    void approveExpenseClaim_should_throw_NotFoundException_when_invalid_id_given() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);
        expenseClaimService.submitExpenseClaim(draftExpenseClaim.getExpenseClaimId());
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.approveExpenseClaim("invalid_id", "OK!"));
    }

    @Test
    void rejectExpenseClaim_should_throw_NotFoundException_when_invalid_id_given() {
        ExpenseClaimInput input1 = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        ExpenseClaimDetail draftExpenseClaim = expenseClaimService.createExpenseClaim(input1);
        expenseClaimService.submitExpenseClaim(draftExpenseClaim.getExpenseClaimId());
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.rejectExpenseClaim("invalid_id", "NG!"));
    }
}
