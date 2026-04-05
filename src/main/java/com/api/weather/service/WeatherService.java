package com.api.weather.service;

import com.api.weather.client.OpenWeatherClient;
import com.api.weather.dto.WeatherRecordDTO;
import com.api.weather.dto.WeatherResponseDTO;
import com.api.weather.entity.WeatherRecord;
import com.api.weather.mapping.WeatherRecordMapper;
import com.api.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final OpenWeatherClient client;
    private final WeatherRepository weatherRepository;
    private final WeatherRecordMapper mapper;


    @Cacheable(value = "weatherCache", key = "#city")
    public WeatherRecordDTO getCurrentWeather(String city) {
        WeatherResponseDTO dto = client.getCurrentWeather(city);;


        WeatherRecord record = mapper.toEntity(dto);
        log.info("mapper: record id {}",record.getId());

        record.setId(null);
        weatherRepository.save(record);
        log.info("repos: record id {}",record.getId());

        return mapper.toDto(record);
    }

    public List<WeatherRecordDTO> getHistoricalWeather(String city, int limit) {
        return weatherRepository.findByCityOrderByTimestampDesc(city)
                .stream()
                .limit(limit)
                .map(mapper::toDto)
                .toList();
    }
}
