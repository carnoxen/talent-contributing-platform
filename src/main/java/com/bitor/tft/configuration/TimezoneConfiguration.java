package com.bitor.tft.configuration;

// import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties("zone")
@Configuration
@Data
public class TimezoneConfiguration {
    private String id;

    private ZoneId getZoneId() {
        return ZoneId.of(id);
    }

    // private Clock getClock() {
    //     return Clock.system(getZoneId());
    // }

    public LocalDate getNowLocalDate() {
        return LocalDate.now(getZoneId());
    }

    public LocalTime getNowLocalTime() {
        return LocalTime.now(getZoneId()).withNano(0).withSecond(0);
    }

    public LocalDateTime getNowLocalDatetime() {
        return LocalDateTime.now(getZoneId());
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(getZoneId());
    }
}
