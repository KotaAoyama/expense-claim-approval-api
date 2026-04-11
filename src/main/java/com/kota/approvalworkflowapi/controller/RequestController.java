package com.kota.approvalworkflowapi.controller;

import com.kota.approvalworkflowapi.dto.RequestDetail;
import com.kota.approvalworkflowapi.dto.RequestInput;
import com.kota.approvalworkflowapi.dto.RequestSummary;
import com.kota.approvalworkflowapi.dto.request.CreateRequestRequest;
import com.kota.approvalworkflowapi.dto.response.RequestDetailResponse;
import com.kota.approvalworkflowapi.dto.response.RequestSummaryResponse;
import com.kota.approvalworkflowapi.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public RequestDetailResponse createRequest(@RequestBody CreateRequestRequest req) {
        RequestInput input = RequestInput.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .build();
        RequestDetail savedRequest = requestService.createRequest(input);
        return RequestDetailResponse.from(savedRequest);
    }

    @GetMapping
    public List<RequestSummaryResponse> getRequests() {
        List<RequestSummary> requestSummaries = requestService.getRequests();
        return requestSummaries.stream().map(requestSummary -> RequestSummaryResponse.builder()
                .requestId(requestSummary.getRequestId())
                .userName(requestSummary.getUserName())
                .status(requestSummary.getStatus())
                .title(requestSummary.getTitle())
                .createdAt(requestSummary.getCreatedAt())
                .build()).toList();
    }

    @GetMapping("/{requestId}")
    public RequestDetailResponse getRequestById(@PathVariable String requestId) {
        RequestDetail requestDetail = requestService.getRequestById(requestId);
        return RequestDetailResponse.from(requestDetail);
    }

    @PostMapping("/{requestId}/submit")
    public RequestDetailResponse submitRequest(@PathVariable String requestId) {
        RequestDetail requestDetail = requestService.submitRequest(requestId);
        return RequestDetailResponse.from(requestDetail);
    }

    @PostMapping("/{requestId}/approve")
    public RequestDetailResponse approveRequest(@PathVariable String requestId) {
        RequestDetail requestDetail = requestService.approveRequest(requestId);
        return RequestDetailResponse.from(requestDetail);
    }

    @PostMapping("/{requestId}/reject")
    public RequestDetailResponse rejectRequest(@PathVariable String requestId) {
        RequestDetail requestDetail = requestService.rejectRequest(requestId);
        return RequestDetailResponse.from(requestDetail);
    }
}
