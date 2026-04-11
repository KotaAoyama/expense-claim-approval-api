package com.kota.approvalworkflowapi.service;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestDetail;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.entity.RequestEntity;
import com.kota.approvalworkflowapi.exception.NotFoundException;
import com.kota.approvalworkflowapi.exception.StatusConflictException;
import com.kota.approvalworkflowapi.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    public RequestDetail createRequest(RequestInput input) {
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
        return toRequestDetail(savedEntity);
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

    public RequestDetail getRequestById(String requestId) {
        RequestEntity requestEntity = getRequestOrThrow(requestId);
        return toRequestDetail(requestEntity);
    }

    public RequestDetail submitRequest(String requestId) {
        RequestEntity requestEntity = getRequestOrThrow(requestId);
        if (!requestEntity.getStatus().canSubmit()) {
            throw new StatusConflictException("Only DRAFT requests can be submitted");
        }
        requestEntity.changeStatus(RequestStatus.SUBMITTED);
        RequestEntity updatedEntity = requestRepository.saveRequest(requestEntity);
        return toRequestDetail(updatedEntity);
    }

    public RequestDetail approveRequest(String requestId) {
        RequestEntity requestEntity = getRequestOrThrow(requestId);
        if (!requestEntity.getStatus().canApprove()) {
            throw new StatusConflictException("Only SUBMITTED requests can be approved");
        }
        requestEntity.changeStatus(RequestStatus.APPROVED);
        RequestEntity updatedEntity = requestRepository.saveRequest(requestEntity);
        return toRequestDetail(updatedEntity);
    }

    public RequestDetail rejectRequest(String requestId) {
        RequestEntity requestEntity = getRequestOrThrow(requestId);
        if (!requestEntity.getStatus().canReject()) {
            throw new StatusConflictException("Only SUBMITTED requests can be rejected");
        }
        requestEntity.changeStatus(RequestStatus.REJECTED);
        RequestEntity updatedEntity = requestRepository.saveRequest(requestEntity);
        return toRequestDetail(updatedEntity);
    }

    private RequestEntity getRequestOrThrow(String requestId) {
        try {
            return requestRepository.getRequestById(requestId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("No request is found");
        }
    }

    private RequestDetail toRequestDetail(RequestEntity entity) {
        return RequestDetail.builder()
                .requestId(entity.getRequestId())
                .userId(entity.getUserId())
                .userName(USER_NAME) // TODO
                .status(entity.getStatus())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
