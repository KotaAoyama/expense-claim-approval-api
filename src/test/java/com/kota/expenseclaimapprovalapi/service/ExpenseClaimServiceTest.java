package com.kota.expenseclaimapprovalapi.service;

import com.kota.expenseclaimapprovalapi.common.ExpenseClaimStatus;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimDetail;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimInput;
import com.kota.expenseclaimapprovalapi.dto.ExpenseClaimSummary;
import com.kota.expenseclaimapprovalapi.exception.NotFoundException;
import com.kota.expenseclaimapprovalapi.exception.StatusConflictException;
import com.kota.expenseclaimapprovalapi.exception.ValidationException;
import com.kota.expenseclaimapprovalapi.repository.ExpenseClaimRepository;
import com.kota.expenseclaimapprovalapi.repository.InMemoryExpenseClaimRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseClaimServiceTest {

    private ExpenseClaimRepository expenseClaimRepository;
    private ExpenseClaimService expenseClaimService;

    @BeforeEach
    void setUp() {
        expenseClaimRepository = new InMemoryExpenseClaimRepositoryImpl();
        expenseClaimService = new ExpenseClaimService(expenseClaimRepository);
    }

    // =========================
    // create
    // =========================

    @Test
    void createExpenseClaim_whenInputIsValid_thenReturnDraftDetail() {
        // Arrange
        ExpenseClaimInput input = validInput();

        // Act
        ExpenseClaimDetail result = expenseClaimService.createExpenseClaim(input);

        // Assert
        assertNotNull(result);
        assertEquals(ExpenseClaimStatus.DRAFT, result.getStatus());
        assertEquals(input.getTitle(), result.getTitle());
        assertEquals(input.getDescription(), result.getDescription());
        assertEquals(input.getAmount(), result.getAmount());
        assertNotNull(result.getExpenseClaimId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void createExpenseClaim_whenTitleIsMissing_thenThrowMethodArgumentNotValidException() {
        // Arrange
        ExpenseClaimInput input = ExpenseClaimInput.builder()
                .description("4月分")
                .amount(5000)
                .build();

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> expenseClaimService.createExpenseClaim(input));
    }

    // =========================
    // get
    // =========================

    @Test
    void getExpenseClaims_whenExpenseClaimsExist_thenReturnSummaryList() {
        // Arrange
        ExpenseClaimInput input = validInput();
        expenseClaimService.createExpenseClaim(input);

        // Act
        List<ExpenseClaimSummary> result = expenseClaimService.getExpenseClaims();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getExpenseClaimById_whenExpenseClaimExists_thenReturnDetail() {
        // Arrange
        ExpenseClaimDetail created = expenseClaimService.createExpenseClaim(validInput());

        // Act
        ExpenseClaimDetail result = expenseClaimService.getExpenseClaimById(created.getExpenseClaimId());

        // Assert
        assertNotNull(result);
        assertEquals(created.getExpenseClaimId(), result.getExpenseClaimId());
    }

    @Test
    void getExpenseClaimById_whenIdIsInvalid_thenThrowNotFoundException() {
        // Arrange
        String invalidId = "invalid_id";

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.getExpenseClaimById(invalidId));
    }

    // =========================
    // submit
    // =========================

    @Test
    void submitExpenseClaim_whenStatusIsDraftAndRequiredFieldsArePresent_thenReturnSubmittedDetail() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());

        // Act
        ExpenseClaimDetail result = expenseClaimService.submitExpenseClaim(draft.getExpenseClaimId());

        // Assert
        assertEquals(ExpenseClaimStatus.SUBMITTED, result.getStatus());
        assertNotEquals(result.getCreatedAt(), result.getUpdatedAt());
    }

    @Test
    void submitExpenseClaim_whenStatusIsSubmitted_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());
        ExpenseClaimDetail submitted = expenseClaimService.submitExpenseClaim(draft.getExpenseClaimId());

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.submitExpenseClaim(submitted.getExpenseClaimId()));
    }

    @Test
    void submitExpenseClaim_whenStatusIsApproved_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail approved = createApprovedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.submitExpenseClaim(approved.getExpenseClaimId()));
    }

    @Test
    void submitExpenseClaim_whenStatusIsRejected_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail rejected = createRejectedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.submitExpenseClaim(rejected.getExpenseClaimId()));
    }

    @Test
    void submitExpenseClaim_whenIdIsInvalid_thenThrowNotFoundException() {
        // Arrange
        String invalidId = "invalid_id";

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.submitExpenseClaim(invalidId));
    }

    @Test
    void submitExpenseClaim_whenAmountIsMissing_thenThrowValidationException() {
        // Arrange
        ExpenseClaimInput input = ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(input);

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> expenseClaimService.submitExpenseClaim(draft.getExpenseClaimId()));
    }

    @Test
    void submitExpenseClaim_whenStatusIsSubmitted_thenStateShouldNotBeChanged() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());
        ExpenseClaimDetail submitted = expenseClaimService.submitExpenseClaim(draft.getExpenseClaimId());
        LocalDateTime updatedAtBefore = submitted.getUpdatedAt();

        // Act
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.submitExpenseClaim(submitted.getExpenseClaimId()));

        ExpenseClaimDetail result = expenseClaimService.getExpenseClaimById(submitted.getExpenseClaimId());

        // Assert
        assertEquals(ExpenseClaimStatus.SUBMITTED, result.getStatus());
        assertEquals(updatedAtBefore, result.getUpdatedAt());
    }

    // =========================
    // approve
    // =========================

    @Test
    void approveExpenseClaim_whenStatusIsSubmitted_thenReturnApprovedDetail() {
        // Arrange
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();

        // Act
        ExpenseClaimDetail result =
                expenseClaimService.approveExpenseClaim(submitted.getExpenseClaimId(), "OK!");

        // Assert
        assertEquals(ExpenseClaimStatus.APPROVED, result.getStatus());
    }

    @Test
    void approveExpenseClaim_whenStatusIsSubmitted_thenReviewerCommentShouldBeSaved() {
        // Arrange
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();

        // Act
        ExpenseClaimDetail result =
                expenseClaimService.approveExpenseClaim(submitted.getExpenseClaimId(), "OK!");

        // Assert
        assertEquals("OK!", result.getReviewerComment());
    }

    @Test
    void approveExpenseClaim_whenStatusIsDraft_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.approveExpenseClaim(draft.getExpenseClaimId(), "OK!"));
    }

    @Test
    void approveExpenseClaim_whenStatusIsApproved_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail approved = createApprovedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.approveExpenseClaim(approved.getExpenseClaimId(), "OK!"));
    }

    @Test
    void approveExpenseClaim_whenStatusIsRejected_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail rejected = createRejectedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.approveExpenseClaim(rejected.getExpenseClaimId(), "OK!"));
    }

    @Test
    void approveExpenseClaim_whenIdIsInvalid_thenThrowNotFoundException() {
        // Arrange
        String invalidId = "invalid_id";

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.approveExpenseClaim(invalidId, "OK!"));
    }

    @Test
    void approveExpenseClaim_whenStatusIsDraft_thenStateShouldNotBeChanged() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());
        LocalDateTime updatedAtBefore = draft.getUpdatedAt();

        // Act
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.approveExpenseClaim(draft.getExpenseClaimId(), "OK!"));

        ExpenseClaimDetail result = expenseClaimService.getExpenseClaimById(draft.getExpenseClaimId());

        // Assert
        assertEquals(ExpenseClaimStatus.DRAFT, result.getStatus());
        assertEquals(updatedAtBefore, result.getUpdatedAt());
        assertNull(result.getReviewerComment());
    }

    // =========================
    // reject
    // =========================

    @Test
    void rejectExpenseClaim_whenStatusIsSubmitted_thenReturnRejectedDetail() {
        // Arrange
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();

        // Act
        ExpenseClaimDetail result =
                expenseClaimService.rejectExpenseClaim(submitted.getExpenseClaimId(), "NG!");

        // Assert
        assertEquals(ExpenseClaimStatus.REJECTED, result.getStatus());
    }

    @Test
    void rejectExpenseClaim_whenStatusIsSubmitted_thenReviewerCommentShouldBeSaved() {
        // Arrange
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();

        // Act
        ExpenseClaimDetail result =
                expenseClaimService.rejectExpenseClaim(submitted.getExpenseClaimId(), "NG!");

        // Assert
        assertEquals("NG!", result.getReviewerComment());
    }

    @Test
    void rejectExpenseClaim_whenStatusIsDraft_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.rejectExpenseClaim(draft.getExpenseClaimId(), "NG!"));
    }

    @Test
    void rejectExpenseClaim_whenStatusIsApproved_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail approved = createApprovedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.rejectExpenseClaim(approved.getExpenseClaimId(), "NG!"));
    }

    @Test
    void rejectExpenseClaim_whenStatusIsRejected_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail rejected = createRejectedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.rejectExpenseClaim(rejected.getExpenseClaimId(), "NG!"));
    }

    @Test
    void rejectExpenseClaim_whenIdIsInvalid_thenThrowNotFoundException() {
        // Arrange
        String invalidId = "invalid_id";

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.rejectExpenseClaim(invalidId, "NG!"));
    }

    // =========================
    // edit
    // =========================

    @Test
    void editExpenseClaim_whenStatusIsDraft_thenReturnUpdatedDetail() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());
        ExpenseClaimInput updateInput = ExpenseClaimInput.builder()
                .description("5月分")
                .amount(1000)
                .build();

        // Act
        ExpenseClaimDetail result =
                expenseClaimService.editExpenseClaim(draft.getExpenseClaimId(), updateInput);

        // Assert
        assertEquals("交通費精算", result.getTitle());
        assertEquals("5月分", result.getDescription());
        assertEquals(1000, result.getAmount());
        assertEquals(ExpenseClaimStatus.DRAFT, result.getStatus());
    }

    @Test
    void editExpenseClaim_whenPartialFieldsAreUpdated_thenOnlySpecifiedFieldsShouldBeChanged() {
        // Arrange
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());
        ExpenseClaimInput updateInput = ExpenseClaimInput.builder()
                .description("5月分")
                .build();

        // Act
        ExpenseClaimDetail result =
                expenseClaimService.editExpenseClaim(draft.getExpenseClaimId(), updateInput);

        // Assert
        assertEquals("交通費精算", result.getTitle());
        assertEquals("5月分", result.getDescription());
        assertEquals(5000, result.getAmount());
    }

    @Test
    void editExpenseClaim_whenStatusIsSubmitted_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.editExpenseClaim(submitted.getExpenseClaimId(), validEditInput()));
    }

    @Test
    void editExpenseClaim_whenStatusIsApproved_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail approved = createApprovedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.editExpenseClaim(approved.getExpenseClaimId(), validEditInput()));
    }

    @Test
    void editExpenseClaim_whenStatusIsRejected_thenThrowStatusConflictException() {
        // Arrange
        ExpenseClaimDetail rejected = createRejectedExpenseClaim();

        // Act & Assert
        assertThrows(StatusConflictException.class,
                () -> expenseClaimService.editExpenseClaim(rejected.getExpenseClaimId(), validEditInput()));
    }

    @Test
    void editExpenseClaim_whenIdIsInvalid_thenThrowNotFoundException() {
        // Arrange
        String invalidId = "invalid_id";

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> expenseClaimService.editExpenseClaim(invalidId, validEditInput()));
    }

    // =========================
    // helper methods
    // =========================

    private ExpenseClaimInput validInput() {
        return ExpenseClaimInput.builder()
                .title("交通費精算")
                .description("4月分")
                .amount(5000)
                .build();
    }

    private ExpenseClaimInput validEditInput() {
        return ExpenseClaimInput.builder()
                .description("5月分")
                .amount(1000)
                .build();
    }

    private ExpenseClaimDetail createSubmittedExpenseClaim() {
        ExpenseClaimDetail draft = expenseClaimService.createExpenseClaim(validInput());
        return expenseClaimService.submitExpenseClaim(draft.getExpenseClaimId());
    }

    private ExpenseClaimDetail createApprovedExpenseClaim() {
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();
        return expenseClaimService.approveExpenseClaim(submitted.getExpenseClaimId(), "OK!");
    }

    private ExpenseClaimDetail createRejectedExpenseClaim() {
        ExpenseClaimDetail submitted = createSubmittedExpenseClaim();
        return expenseClaimService.rejectExpenseClaim(submitted.getExpenseClaimId(), "NG!");
    }
}

