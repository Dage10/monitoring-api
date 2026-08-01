package com.david.monitoring.schedulers;

import com.david.monitoring.entities.Metric;
import com.david.monitoring.entities.ServiceEntity;
import com.david.monitoring.metrics.MetricCollectorService;
import com.david.monitoring.metrics.MetricsStreamService;
import com.david.monitoring.services.ServiceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricScheduler {

    private final ServiceService serviceService;
    private final MetricCollectorService metricCollectorService;
    private final MetricsStreamService metricsStreamService;

    public MetricScheduler(ServiceService serviceService,
                           MetricCollectorService metricCollectorService,
                           MetricsStreamService metricsStreamService) {
        this.serviceService = serviceService;
        this.metricCollectorService = metricCollectorService;
        this.metricsStreamService = metricsStreamService;
    }

    @Scheduled(fixedRate = 30_000)
    public void collectMetrics() {

        List<ServiceEntity> services = serviceService.findAllServices();

        services.parallelStream().forEach(service -> {
            Metric metric = metricCollectorService.collect(service);
            metricsStreamService.sendMetric(service.getUserId(), metric);
        });
    }
}