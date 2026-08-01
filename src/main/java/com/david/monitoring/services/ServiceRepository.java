package com.david.monitoring.services;

import com.david.monitoring.entities.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByUserId(Long userId);
}
