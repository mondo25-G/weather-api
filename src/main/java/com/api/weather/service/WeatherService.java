package com.api.weather.service;

import com.api.weather.client.OpenWeatherClient;
import com.api.weather.dto.WeatherRecordDTO;
import com.api.weather.dto.WeatherResponseDTO;
import com.api.weather.entity.WeatherRecord;
import com.api.weather.mapping.WeatherRecordMapper;
import com.api.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final WeatherRepository weatherRepository;
    private final WeatherRecordMapper mapper;


    @Cacheable(value = "weatherCache", key = "#city")
    public WeatherRecord getWeather(String city) {

        // Call OpenWeatherMap API
        WeatherResponseDTO dto = openWeatherClient.getCurrentWeather(city);

        // Map DTO to WeatherRecord
        WeatherRecord record = mapper.toEntity(dto);

        // Save in MongoDB
        weatherRepository.save(record);

        return record;
    }

    public WeatherRecordDTO getCurrentWeather(String city) {
        WeatherRecord record = getWeather(city);
        return mapper.toDto(record);
    }


    public List<WeatherRecordDTO> getHistoricalWeather(String city, int limit) {
        List<WeatherRecord> records = weatherRepository.findByCityOrderByTimestampDesc(city);
        return records.stream().limit(limit).map(mapper::toDto).toList();
    }

}
