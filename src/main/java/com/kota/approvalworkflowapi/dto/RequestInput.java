package com.kota.approvalworkflowapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class RequestInput {
    private String title;
    private String description;
}
