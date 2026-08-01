package com.david.monitoring.services;

import com.david.monitoring.entities.ServiceEntity;
import com.david.monitoring.services.dto.ServiceResponse;
import com.david.monitoring.services.dto.CreateServiceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceService {

    private final ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public ServiceResponse create(Long userId, CreateServiceRequest request) {
        ServiceEntity entity = new ServiceEntity(userId, request.name(), request.url());
        return toResponse(repository.save(entity));
    }

    public List<ServiceResponse> list(Long userId) {
        return repository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public ServiceResponse get(Long userId, Long id) {
        return toResponse(findByIdOrForbidden(userId, id));
    }

    public void delete(Long userId, Long id) {
        repository.delete(findByIdOrForbidden(userId, id));
    }

    public List<ServiceEntity> findAllServices() {
        return repository.findAll();
    }

    private ServiceEntity findByIdOrForbidden(Long userId, Long id) {
        ServiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!entity.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return entity;
    }

    private ServiceResponse toResponse(ServiceEntity entity) {
        return new ServiceResponse(entity.getId(), entity.getName(), entity.getUrl(), entity.getCreatedAt());
    }
}
