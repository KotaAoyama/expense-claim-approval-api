package com.kota.approvalworkflowapi.entity;

import com.kota.approvalworkflowapi.common.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "requests")
public class RequestEntity {

    @Id
    private String requestId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
