package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.configurations.serverinfo.services.RequestTracker;
import com.example.isdfarmersmarket.dao.enums.TimeInterval;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.core.env.Environment;
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
    Environment environment;
    RequestTracker requestTracker;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getServerInfo() {
        Map<String, Object> serverInfo = new HashMap<>();

        serverInfo.put("Health", healthEndpoint.health().getStatus().toString());

        Map<String, Object> appInfo = infoEndpoint.info();
        if (appInfo.isEmpty()) {
            appInfo = new HashMap<>();
            appInfo.put("App Name", environment.getProperty("spring.application.name", "Unknown"));
            appInfo.put("App Version", environment.getProperty("info.app.version", "Unknown"));
            appInfo.put("Description", environment.getProperty("info.app.description", "No description provided"));
        }
        serverInfo.put("App Info", appInfo);

        Map<String, String> javaInfo = new HashMap<>();
        javaInfo.put("Java Version", System.getProperty("java.version"));
        javaInfo.put("Java Vendor", System.getProperty("java.vendor"));
        serverInfo.put("Java Info", javaInfo);

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("Processors Available", Runtime.getRuntime().availableProcessors());
        metrics.put("Uptime (seconds)", metricsEndpoint.metric("process.uptime", null)
                .getMeasurements().stream().findFirst().map(MetricsEndpoint.Sample::getValue).orElse(0.0));
        metrics.put("Disk Total (bytes)", metricsEndpoint.metric("disk.total", null)
                .getMeasurements().stream().findFirst().map(MetricsEndpoint.Sample::getValue).orElse(0.0));
        metrics.put("Disk Free (bytes)", metricsEndpoint.metric("disk.free", null)
                .getMeasurements().stream().findFirst().map(MetricsEndpoint.Sample::getValue).orElse(0.0));
        metrics.put("Memory Used (bytes)", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        serverInfo.put("Metrics", metrics);

        return ResponseEntity.ok(serverInfo);
    }

    @GetMapping("/requests")
    public ResponseEntity<Map<String, Integer>> getRequestsInfo(TimeInterval interval) {
        return ResponseEntity.ok(requestTracker.getRequestsInLastNTime(interval));
    }
}
