package com.api.weather.dto;

import java.util.List;

public record WeatherResponseDTO (
        Coord coord,
        List<Weather> weather,
        String base,
        Main main,
        Integer visibility,
        Wind wind,
        Clouds clouds,
        Long dt,
        Sys sys,
        Integer timezone,
        Long id,
        String name,
        Long cod
) {

    public record Coord(
            Double lon,
            Double lat
    ) {}

    public record Weather(
            String main,
            String description,
            String icon
    ) {}

    public record Main(
            Double temp,
            Double feels_like,
            Double temp_min,
            Double temp_max,
            Integer pressure,
            Integer humidity
    ) {}

    public record Wind(
            Double speed,
            Integer deg,
            Double gust
    ) {}

    public record Clouds(
            Integer all
    ) {}

    public record Sys(
            String country,
            Long sunrise,
            Long sunset
    ) {}
}
