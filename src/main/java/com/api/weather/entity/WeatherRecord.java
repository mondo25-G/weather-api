package com.api.weather.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "weather_records")
public class WeatherRecord implements Serializable {

    @Id
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
}
