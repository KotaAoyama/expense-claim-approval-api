package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.repository.InMemoryRequestRepository;
import com.kota.approvalworkflowapi.repository.RequestRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    @Test
    void createRequest_shouldReturnDraftSummary() {
        RequestRepository requestRepository = new InMemoryRequestRepository();
        RequestService requestService = new RequestService(requestRepository);

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
}