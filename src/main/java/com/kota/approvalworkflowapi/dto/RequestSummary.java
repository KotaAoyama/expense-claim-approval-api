package com.kota.approvalworkflowapi.dto;

import com.kota.approvalworkflowapi.common.RequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestSummary {
    private String requestId;
    private String userName;
    private RequestStatus status;
    private String title;
    private LocalDateTime createdAt;
}
