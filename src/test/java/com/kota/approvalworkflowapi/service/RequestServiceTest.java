package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestDetail;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.repository.InMemoryRequestRepository;
import com.kota.approvalworkflowapi.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestServiceTest {

    RequestRepository requestRepository;
    RequestService requestService;

    @BeforeEach
    void setup() {
        requestRepository = new InMemoryRequestRepository();
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
        assertEquals("user1", result.getUserName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }
}