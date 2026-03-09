package com.api.weather.service;

import com.api.weather.client.OpenWeatherClient;
import com.api.weather.dto.WeatherResponseDTO;
import com.api.weather.entity.WeatherRecord;
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

    @Cacheable(value = "weatherCache", key = "#city")
    public WeatherRecord getCurrentWeather(String city) {

        // Call OpenWeatherMap API
        WeatherResponseDTO dto = openWeatherClient.getCurrentWeather(city);

        // Map DTO to WeatherRecord
        WeatherRecord record = mapDtoToRecord(dto);

        // Save in MongoDB
        weatherRepository.save(record);

        return record;
    }


    public List<WeatherRecord> getHistoricalWeather(String city, int limit) {
        List<WeatherRecord> records = weatherRepository.findByCityOrderByTimestampDesc(city);
        return records.stream().limit(limit).toList();
    }

    private WeatherRecord mapDtoToRecord(WeatherResponseDTO dto) {
        WeatherRecord record = new WeatherRecord();
        record.setCity(dto.name());
        record.setCountry(dto.sys().country());

        record.setLatitude(dto.coord().lat());
        record.setLongitude(dto.coord().lon());

        record.setTemperature(dto.main().temp());
        record.setFeelsLike(dto.main().feels_like());
        record.setTempMin(dto.main().temp_min());
        record.setTempMax(dto.main().temp_max());
        record.setPressure(dto.main().pressure());
        record.setHumidity(dto.main().humidity());

        if (!dto.weather().isEmpty()) {
            WeatherResponseDTO.Weather w = dto.weather().get(0);
            record.setWeatherMain(w.main());
            record.setWeatherDescription(w.description());
            record.setWeatherIcon(w.icon());
        }

        record.setWindSpeed(dto.wind().speed());
        record.setWindDeg(dto.wind().deg());
        record.setWindGust(dto.wind().gust());

        record.setCloudCoverage(dto.clouds().all());
        record.setVisibility(dto.visibility());

        record.setTimestamp(dto.dt());
        record.setSunrise(dto.sys().sunrise());
        record.setSunset(dto.sys().sunset());

        return record;
    }
}
