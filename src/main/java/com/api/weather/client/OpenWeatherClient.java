package com.api.weather.client;

import com.api.weather.dto.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OpenWeatherClient {

    private final RestClient openWeatherRestClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponseDTO getCurrentWeather(String city) {
        return openWeatherRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .body(WeatherResponseDTO.class);
    }
}