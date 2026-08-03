package com.david.monitoring.metrics;

import com.david.monitoring.entities.ServiceEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnomalyService {

    private final MetricRepository metricRepository;

    public AnomalyService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public boolean isAnomalous(ServiceEntity service) {

        Instant now = Instant.now();

        List<Long> recent = metricRepository.findLatenciesSince(
                service.getId(),
                now.minusSeconds(300)
        );

        List<Long> historical = metricRepository.findLatenciesSince(
                service.getId(),
                now.minusSeconds(3600)
        );

        if (recent.isEmpty() || historical.isEmpty()) {
            return false;
        }

        double recentAvg = average(recent);
        double historicalAvg = average(historical);

        return recentAvg > historicalAvg * 3;
    }

    private double average(List<Long> values) {
        return values.stream().mapToLong(v -> v).average().orElse(0.0);
    }

}
