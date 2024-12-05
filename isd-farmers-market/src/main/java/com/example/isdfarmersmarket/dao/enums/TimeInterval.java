package com.example.isdfarmersmarket.dao.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeInterval {

    HOURS_1(3600000),
    MINUTES_1(60000),
    MINUTES_5(300000),
    SECONDS_5(5000),
    HOURS_24(86400000),
    RESET_INTERVAL(86400000);

    private final long milliseconds;
}
