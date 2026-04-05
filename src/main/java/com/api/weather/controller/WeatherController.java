package com.api.weather.controller;

import com.api.weather.dto.WeatherRecordDTO;
import com.api.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Get the current weather for a city.
     */
    @GetMapping
    public ResponseEntity<WeatherRecordDTO> getCurrentWeather(@RequestParam String city) {
        WeatherRecordDTO recordDTO = weatherService.getCurrentWeather(city);
        return ResponseEntity.ok(recordDTO);
    }

    /**
     * Get historical weather records for a city.
     */
    @GetMapping("/history")
    public ResponseEntity<List<WeatherRecordDTO>> getHistoricalWeather(
            @RequestParam String city,
            @RequestParam(defaultValue = "10") int limit) {

        List<WeatherRecordDTO> records = weatherService.getHistoricalWeather(city, limit);
        return ResponseEntity.ok(records);
    }
}