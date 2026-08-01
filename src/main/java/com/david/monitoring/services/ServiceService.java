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
        ServiceEntity entity = new ServiceEntity(
                userId,
                request.name(),
                request.url()
        );

        ServiceEntity saved = repository.save(entity);

        return new ServiceResponse(
                saved.getId(),
                saved.getName(),
                saved.getUrl(),
                saved.getCreatedAt()
        );
    }

    public List<ServiceResponse> list(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(s -> new ServiceResponse(
                        s.getId(),
                        s.getName(),
                        s.getUrl(),
                        s.getCreatedAt()
                ))
                .toList();
    }

    public ServiceResponse get(Long userId, Long id) {
        ServiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!entity.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return new ServiceResponse(
                entity.getId(),
                entity.getName(),
                entity.getUrl(),
                entity.getCreatedAt()
        );
    }

    public void delete(Long userId, Long id) {
        ServiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!entity.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        repository.delete(entity);
    }

}
