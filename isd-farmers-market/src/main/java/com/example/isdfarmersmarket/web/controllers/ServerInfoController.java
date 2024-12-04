package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.configurations.serverinfo.services.RequestTracker;
import com.example.isdfarmersmarket.dao.enums.TimeInterval;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/server-info")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServerInfoController {
    HealthEndpoint healthEndpoint;
    InfoEndpoint infoEndpoint;
    MetricsEndpoint metricsEndpoint;
    RequestTracker requestTracker;

//    @PreAuthorize("hasRole('ADMIN')")
@GetMapping
public ResponseEntity<Map<String, Object>> getServerInfo() {
    Map<String, Object> serverInfo = new HashMap<>();

    serverInfo.put("Health", healthEndpoint.health().getStatus().toString());

    serverInfo.put("Info", infoEndpoint.info());

    Map<String, Object> metrics = new HashMap<>();
    metrics.put("Processors available", Runtime.getRuntime().availableProcessors());
    metrics.put("Uptime (seconds)", metricsEndpoint.metric("process.uptime", null)
            .getMeasurements().stream().findFirst().map(MetricsEndpoint.Sample::getValue).orElse(0.0));
    metrics.put("Disk total (bytes)", metricsEndpoint.metric("disk.total", null)
            .getMeasurements().stream().findFirst().map(MetricsEndpoint.Sample::getValue).orElse(0.0));
    metrics.put("Disk free (bytes)", metricsEndpoint.metric("disk.free", null)
            .getMeasurements().stream().findFirst().map(MetricsEndpoint.Sample::getValue).orElse(0.0));
    metrics.put("Memory used (bytes)", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

    serverInfo.put("Metrics", metrics);

    return ResponseEntity.ok(serverInfo);
}
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/requests")
    public ResponseEntity<Map<String, Integer>> getRequestsInfo(TimeInterval interval) {
        return ResponseEntity.ok(requestTracker.getRequestsInLastNTime(interval));
    }
}
