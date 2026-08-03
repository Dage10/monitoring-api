package com.david.monitoring.metrics;

import com.david.monitoring.entities.Metric;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MetricsStreamService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long userId, SseEmitter emitter) {
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);
    }

    public void removeEmitter(Long userId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(userId);
        if (list != null) {
            list.remove(emitter);
        }
    }

    public void sendMetric(Long userId, Metric metric) {
        sendEvent(userId, "metric", metric);
    }

    public void sendAlert(Long userId, String serviceName) {
        sendEvent(userId, "alert", "Anomaly detected in service: " + serviceName);
    }

    private void sendEvent(Long userId, String eventName, Object data) {
        List<SseEmitter> list = emitters.get(userId);
        if (list == null || list.isEmpty()) {
            return;
        }

        for (SseEmitter emitter : list) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name(eventName)
                                .data(data)
                );
            } catch (IOException e) {
                emitter.complete();
                removeEmitter(userId, emitter);
            }
        }
    }


}
