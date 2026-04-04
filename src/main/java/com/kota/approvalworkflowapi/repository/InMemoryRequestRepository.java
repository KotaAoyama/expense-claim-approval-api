package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.RequestEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryRequestRepository implements RequestRepository {

    private final List<RequestEntity> requestEntities = new ArrayList<>();

    @Override
    public RequestEntity saveRequest(RequestEntity entity) {
        requestEntities.add(entity);
        return entity;
    }

    @Override
    public List<RequestEntity> getRequestsByUserId(String userId) {
        return requestEntities.stream().filter(x -> Objects.equals(x.getUserId(), userId)).toList();
    }

    @Override
    public RequestEntity getRequestById(String requestId) {
        return requestEntities.stream()
                .filter(x -> Objects.equals(x.getRequestId(), requestId))
                .toList().getFirst();
    }
}
