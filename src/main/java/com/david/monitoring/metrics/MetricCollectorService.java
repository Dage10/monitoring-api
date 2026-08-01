package com.david.monitoring.metrics;

import com.david.monitoring.entities.Metric;
import com.david.monitoring.entities.ServiceEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MetricCollectorService {

    private final RestClient restClient;
    private final MetricService metricService;

    public MetricCollectorService(RestClient restClient, MetricService metricService) {
        this.restClient = restClient;
        this.metricService = metricService;
    }

    public Metric collect(ServiceEntity service) {
        long start = System.nanoTime();
        int statusCode;
        double availability;

        try{

            var response = restClient.get()
                    .uri(service.getUrl())
                    .retrieve()
                    .toEntity(String.class);

            statusCode = response.getStatusCode().value();
            availability = (statusCode >= 200 && statusCode < 300) ? 1.0 : 0.0;

        } catch (Exception ignored) {
            statusCode = 0;
            availability = 0.0;
        }

        long latencyMs = (System.nanoTime() - start) / 1_000_000;

        return metricService.saveMetric(service, latencyMs, statusCode, availability);

    }

}
