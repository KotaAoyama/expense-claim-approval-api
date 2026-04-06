package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.RequestEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class JpaRequestRepositoryImpl implements RequestRepository {

    private final SpringDataJpaRequestRepository springDataJpaRequestRepository;

    public JpaRequestRepositoryImpl(SpringDataJpaRequestRepository springDataJpaRequestRepository) {
        this.springDataJpaRequestRepository = springDataJpaRequestRepository;
    }

    @Override
    public RequestEntity saveRequest(RequestEntity entity) {
        return springDataJpaRequestRepository.save(entity);
    }

    @Override
    public List<RequestEntity> getRequestsByUserId(String userId) {
        return springDataJpaRequestRepository.findByUserId(userId);
    }

    @Override
    public RequestEntity getRequestById(String requestId) {
        return springDataJpaRequestRepository.findById(requestId).orElseThrow();
    }
}
