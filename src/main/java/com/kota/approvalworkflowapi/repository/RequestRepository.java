package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.RequestEntity;

import java.util.List;

public interface RequestRepository {
    RequestEntity saveRequest(RequestEntity entity);

    List<RequestEntity> getRequestsByUserId(String userId);

    RequestEntity getRequestById(String requestId);
}
