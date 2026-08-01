package com.david.monitoring.schedulers;

import com.david.monitoring.entities.ServiceEntity;
import com.david.monitoring.metrics.MetricCollectorService;
import com.david.monitoring.services.ServiceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricScheduler {

    private final ServiceService serviceService;
    private final MetricCollectorService metricCollectorService;

    public MetricScheduler(ServiceService serviceService,
                           MetricCollectorService metricCollectorService) {
        this.serviceService = serviceService;
        this.metricCollectorService = metricCollectorService;
    }

    @Scheduled(fixedRate = 30_000)
    public void collectMetrics() {

        List<ServiceEntity> services = serviceService.findAllServices();

        for (ServiceEntity service : services) {
            metricCollectorService.collect(service);
        }
    }
}