package com.api.weather.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherRecordDTO {

    private String id;

    private String city;
    private String country;

    private Double latitude;
    private Double longitude;

    private Double temperature;
    private Double feelsLike;
    private Double tempMin;
    private Double tempMax;

    private Integer pressure;
    private Integer humidity;

    private Double windSpeed;
    private Integer windDeg;
    private Double windGust;

    private Integer cloudCoverage;
    private Integer visibility;

    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;

    private Long timestamp;
    private Long sunrise;
    private Long sunset;

    public WeatherRecordDTO(String city, Double temperature, String weatherMain) {
        this.city = city;
        this.temperature = temperature;
        this.weatherMain = weatherMain;
    }
}
