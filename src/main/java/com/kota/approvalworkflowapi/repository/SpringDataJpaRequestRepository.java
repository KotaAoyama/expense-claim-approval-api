package com.kota.approvalworkflowapi.repository;

import com.kota.approvalworkflowapi.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaRequestRepository extends JpaRepository<RequestEntity, String> {
    List<RequestEntity> findByUserId(String userId);
}
