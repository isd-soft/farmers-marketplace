package com.example.isdfarmersmarket.business.configurations.serverinfo.services;

import com.example.isdfarmersmarket.dao.enums.TimeInterval;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class RequestTracker {

    List<RequestRecord> requestRecords = new CopyOnWriteArrayList<>();

    @Data
    @AllArgsConstructor
    private static class RequestRecord {
        private String category;
        private long timestamp;
    }

    public void incrementRequestCount(String category) {
        requestRecords.add(new RequestRecord(category, System.currentTimeMillis()));
    }

    public Map<String, Integer> getRequestsInLastNTime(TimeInterval interval) {
        long targetTime = System.currentTimeMillis() - interval.getMilliseconds();
        int elementsToSkip = findClosest(targetTime);
        return requestRecords
                .stream()
                .skip(elementsToSkip)
                .collect(Collectors.groupingBy(RequestRecord::getCategory, Collectors.summingInt(r -> 1)));
    }

    @Scheduled(fixedRateString = "86400000")
    private void resetRequestCounts() {
        log.info("flushing request records: " + requestRecords);
        int index = findClosest(System.currentTimeMillis() - TimeInterval.RESET_INTERVAL.getMilliseconds());
        requestRecords = requestRecords.subList(index, requestRecords.size());
        log.info("flushing result: " + requestRecords);
    }

    private int findClosest(long target) {
        int left = 0;
        int right = requestRecords.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            long midTimestamp = requestRecords.get(mid).getTimestamp();

            if (midTimestamp < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }
}
