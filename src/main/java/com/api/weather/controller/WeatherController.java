package com.api.weather.controller;

import com.api.weather.entity.WeatherRecord;
import com.api.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/com/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Get the current weather for a city.
     */
    @GetMapping
    public ResponseEntity<WeatherRecord> getCurrentWeather(@RequestParam String city) {
        WeatherRecord record = weatherService.getCurrentWeather(city);
        return ResponseEntity.ok(record);
    }

    /**
     * Get historical weather records for a city.
     */
    @GetMapping("/history")
    public ResponseEntity<List<WeatherRecord>> getHistoricalWeather(
            @RequestParam String city,
            @RequestParam(defaultValue = "10") int limit) {

        List<WeatherRecord> records = weatherService.getHistoricalWeather(city, limit);
        return ResponseEntity.ok(records);
    }
}