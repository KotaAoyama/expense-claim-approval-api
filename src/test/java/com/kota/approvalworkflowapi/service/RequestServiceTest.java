package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestDetail;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
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
    void createRequest_shouldReturnDraftSummary() {

        RequestInput input = RequestInput.builder()
                .title("交通費精算")
                .description("4月分")
                .build();

        RequestSummary result = requestService.createRequest(input);

        assertNotNull(result);
        assertNotNull(result.getRequestId());
        assertEquals("交通費精算", result.getTitle());
        assertEquals(RequestStatus.DRAFT, result.getStatus());
        assertEquals("user1", result.getUserName());
        assertNotNull(result.getCreatedAt());
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

        RequestSummary requestSummary = requestService.createRequest(input1);

        RequestDetail result = requestService.getRequestById(requestSummary.getRequestId());

        assertNotNull(result);
        assertEquals(requestSummary.getRequestId(), result.getRequestId());
        assertEquals(requestSummary.getUserName(), result.getUserName());
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

        RequestSummary requestSummary = requestService.createRequest(input1);
        RequestDetail result = requestService.submitRequest(requestSummary.getRequestId());

        assertNotNull(result);
        assertEquals(requestSummary.getRequestId(), result.getRequestId());
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

        RequestSummary requestSummary = requestService.createRequest(input1);
        RequestDetail submitted = requestService.submitRequest(requestSummary.getRequestId());
        LocalDateTime submittedTime = submitted.getUpdatedAt();
        RequestDetail result = requestService.approveRequest(submitted.getRequestId());

        assertNotNull(result);
        assertEquals(requestSummary.getRequestId(), result.getRequestId());
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

        RequestSummary requestSummary = requestService.createRequest(input1);
        RequestDetail submitted = requestService.submitRequest(requestSummary.getRequestId());
        LocalDateTime submittedTime = submitted.getUpdatedAt();
        RequestDetail result = requestService.rejectRequest(submitted.getRequestId());

        assertNotNull(result);
        assertEquals(requestSummary.getRequestId(), result.getRequestId());
        assertEquals(RequestStatus.REJECTED, result.getStatus());
        assertNotNull(submittedTime);
        assertNotNull(result.getUpdatedAt());
        assertNotEquals(submittedTime, result.getUpdatedAt());
    }
}