package com.kota.approvalworkflowapi.entity;

import com.kota.approvalworkflowapi.common.RequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestEntity {
    private String requestId;
    private String userId;
    private RequestStatus status;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
