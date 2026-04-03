package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.RequestEntity;

public interface RequestRepository {
    RequestEntity saveRequest(RequestEntity entity);
}
