package com.david.monitoring.metrics;

import com.david.monitoring.entities.Metric;
import com.david.monitoring.entities.ServiceEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class MetricService {

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public Metric saveMetric(ServiceEntity service, long latencyMs, int statusCode, double availability) {
        return metricRepository.save(new Metric(service, latencyMs, statusCode, availability));
    }

    public List<Metric> getMetricsForService(ServiceEntity service) {
        return metricRepository.findByServiceOrderByCreatedAtDesc(service);
    }

    public List<Metric> getMetricsLastMinutes(ServiceEntity service, int minutes) {
        Instant from = Instant.now().minus(Duration.ofMinutes(minutes));
        return metricRepository.findRecentMetrics(service, from);
    }

    public Double getAverageLatencyLastHour(ServiceEntity service) {
        Instant from = Instant.now().minus(Duration.ofHours(1));
        return metricRepository.findAverageLatency(service, from);
    }

}