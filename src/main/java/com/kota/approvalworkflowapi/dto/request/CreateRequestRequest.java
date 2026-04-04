package com.kota.approvalworkflowapi.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRequestRequest {
    private String title;
    private String description;
}
