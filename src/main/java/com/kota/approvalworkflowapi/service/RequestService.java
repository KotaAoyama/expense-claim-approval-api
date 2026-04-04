package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.entity.RequestEntity;
import com.kota.approvalworkflowapi.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RequestService {

    // Phase1では固定値
    private static final String USER_ID = "userId1234";
    private static final String USER_NAME = "user1";

    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public RequestSummary createRequest(RequestInput input) {
        String requestId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        RequestEntity savedEntity = requestRepository.saveRequest(
                RequestEntity.builder()
                        .requestId(requestId)
                        .userId(USER_ID)
                        .status(RequestStatus.DRAFT)
                        .title(input.getTitle())
                        .description(input.getDescription())
                        .createdAt(now)
                        .updatedAt(now)
                        .build());
        return RequestSummary.builder()
                .requestId(savedEntity.getRequestId())
                .userName(USER_NAME)
                .status(savedEntity.getStatus())
                .title(savedEntity.getTitle())
                .createdAt(savedEntity.getCreatedAt())
                .build();
    }

    public List<RequestSummary> getRequests() {
        List<RequestEntity> requestEntities = requestRepository.getRequestsByUserId(USER_ID);
        return requestEntities.stream()
                .map(entity -> RequestSummary.builder()
                        .requestId(entity.getRequestId())
                        .userName(USER_NAME)
                        .title(entity.getTitle())
                        .status(entity.getStatus())
                        .createdAt(entity.getCreatedAt())
                        .build()).toList();
    }
}
