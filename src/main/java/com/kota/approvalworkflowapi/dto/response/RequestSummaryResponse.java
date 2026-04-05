package com.kota.approvalworkflowapi.dto.response;

import com.kota.approvalworkflowapi.common.RequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestSummaryResponse {
    private String requestId;
    private String userName;
    private RequestStatus status;
    private String title;
    private LocalDateTime createdAt;
}
