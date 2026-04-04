package com.kota.approvalworkflowapi.controller;

import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.dto.request.CreateRequestRequest;
import com.kota.approvalworkflowapi.dto.response.CreateRequestResponse;
import com.kota.approvalworkflowapi.service.RequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public CreateRequestResponse createRequest(@RequestBody CreateRequestRequest req) {
        RequestInput input = RequestInput.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .build();
        RequestSummary savedRequest = requestService.createRequest(input);
        return CreateRequestResponse.builder()
                .requestId(savedRequest.getRequestId())
                .userName(savedRequest.getUserName())
                .status(savedRequest.getStatus())
                .title(savedRequest.getTitle())
                .createdAt(savedRequest.getCreatedAt())
                .build();
    }
}
