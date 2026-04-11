package com.kota.approvalworkflowapi.dto.response;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.dto.RequestDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestDetailResponse {
    private String requestId;
    private String userId;
    private String userName;
    private RequestStatus status;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RequestDetailResponse from(RequestDetail requestDetail) {
        return RequestDetailResponse.builder()
                .requestId(requestDetail.getRequestId())
                .userId(requestDetail.getUserId())
                .userName(requestDetail.getUserName())
                .status(requestDetail.getStatus())
                .title(requestDetail.getTitle())
                .description(requestDetail.getDescription())
                .createdAt(requestDetail.getCreatedAt())
                .updatedAt(requestDetail.getUpdatedAt())
                .build();
    }
}
