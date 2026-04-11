package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestDetail;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.exception.NotFoundException;
import com.kota.approvalworkflowapi.exception.StatusConflictException;
import com.kota.approvalworkflowapi.repository.InMemoryRequestRepositoryImpl;
import com.kota.approvalworkflowapi.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    RequestRepository requestRepository;
    RequestService requestService;

    @BeforeEach
    void setup() {
        requestRepository = new InMemoryRequestRepositoryImpl();
        requestService = new RequestService(requestRepository);
    }

    @AfterEach
    void cleanup() {
        requestRepository = null;
        requestService = null;
    }

    @Test
    void createRequest_shouldReturnDraftDetail() {

        RequestInput input = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail result = requestService.createRequest(input);

        assertNotNull(result);
        assertNotNull(result.getRequestId());
        assertEquals("交通費精算", result.getTitle());
        assertEquals(RequestStatus.DRAFT, result.getStatus());
        assertEquals("userId1234", result.getUserId());
        assertEquals("user1", result.getUserName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void getRequests_shouldReturnSummaryList() {

        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        requestService.createRequest(input1);
        List<RequestSummary> result = requestService.getRequests();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("交通費精算", result.getFirst().getTitle());
        assertEquals(RequestStatus.DRAFT, result.getFirst().getStatus());
        assertEquals("user1", result.getFirst().getUserName());
    }

    @Test
    void getRequestByID_shouldReturnDetail() {

        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail requestDetail = requestService.createRequest(input1);
        RequestDetail result = requestService.getRequestById(requestDetail.getRequestId());

        assertNotNull(result);
        assertEquals(requestDetail.getRequestId(), result.getRequestId());
        assertEquals(requestDetail.getUserName(), result.getUserName());
        assertEquals("交通費精算", result.getTitle());
        assertEquals("4月分", result.getDescription());
        assertEquals(RequestStatus.DRAFT, result.getStatus());
        assertEquals("userId1234", result.getUserId());
        assertEquals("user1", result.getUserName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void submitRequest_should_change_status_to_SUBMITTED() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);
        RequestDetail result = requestService.submitRequest(draftRequest.getRequestId());

        assertNotNull(result);
        assertEquals(draftRequest.getRequestId(), result.getRequestId());
        assertEquals(RequestStatus.SUBMITTED, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(result.getCreatedAt(), result.getUpdatedAt());
    }

    @Test
    void approveRequest_should_change_status_to_APPROVED() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);
        RequestDetail submittedRequest = requestService.submitRequest(draftRequest.getRequestId());
        LocalDateTime submittedTime = submittedRequest.getUpdatedAt();
        RequestDetail result = requestService.approveRequest(submittedRequest.getRequestId());

        assertNotNull(result);
        assertEquals(draftRequest.getRequestId(), result.getRequestId());
        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertNotNull(submittedTime);
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(submittedTime, result.getUpdatedAt());
    }

    @Test
    void rejectRequest_should_change_status_to_REJECTED() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);
        RequestDetail submittedRequest = requestService.submitRequest(draftRequest.getRequestId());
        LocalDateTime submittedTime = submittedRequest.getUpdatedAt();
        RequestDetail result = requestService.rejectRequest(submittedRequest.getRequestId());

        assertNotNull(result);
        assertEquals(draftRequest.getRequestId(), result.getRequestId());
        assertEquals(RequestStatus.REJECTED, result.getStatus());
        assertNotNull(submittedTime);
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(submittedTime, result.getUpdatedAt());
    }

    @Test
    void approveRequest_should_throw_StatusConflictException_when_status_is_DRAFT() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);

        assertThrows(StatusConflictException.class,
                () -> requestService.approveRequest(draftRequest.getRequestId()));
    }

    @Test
    void rejectRequest_should_throw_StatusConflictException_when_status_is_DRAFT() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);

        assertThrows(StatusConflictException.class,
                () -> requestService.rejectRequest(draftRequest.getRequestId()));
    }

    @Test
    void submitRequest_should_throw_StatusConflictException_when_status_is_SUBMITTED() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);
        RequestDetail submittedRequest = requestService.submitRequest(draftRequest.getRequestId());

        assertThrows(StatusConflictException.class,
                () -> requestService.submitRequest(submittedRequest.getRequestId()));
    }

    @Test
    void submitRequest_should_throw_NotFoundException_when_invalid_id_given() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        requestService.createRequest(input1);
        assertThrows(NotFoundException.class,
                () -> requestService.submitRequest("invalid_id"));
    }

    @Test
    void approveRequest_should_throw_NotFoundException_when_invalid_id_given() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);
        requestService.submitRequest(draftRequest.getRequestId());
        assertThrows(NotFoundException.class,
                () -> requestService.approveRequest("invalid_id"));
    }

    @Test
    void rejectRequest_should_throw_NotFoundException_when_invalid_id_given() {
        RequestInput input1 = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestDetail draftRequest = requestService.createRequest(input1);
        requestService.submitRequest(draftRequest.getRequestId());
        assertThrows(NotFoundException.class,
                () -> requestService.rejectRequest("invalid_id"));
    }
}
