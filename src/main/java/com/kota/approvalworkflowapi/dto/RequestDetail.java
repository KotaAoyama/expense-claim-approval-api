package com.kota.approvalworkflowapi.dto;

import com.kota.approvalworkflowapi.common.RequestStatus;
import com.kota.approvalworkflowapi.entity.RequestEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestDetail {
    private String requestId;
    private String userId;
    private String userName;
    private RequestStatus status;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RequestDetail from(RequestEntity entity, String userName) { // TODO: users テーブルを作ったら修正
        return RequestDetail.builder()
                .requestId(entity.getRequestId())
                .userId(entity.getUserId())
                .userName(userName)
                .status(entity.getStatus())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
