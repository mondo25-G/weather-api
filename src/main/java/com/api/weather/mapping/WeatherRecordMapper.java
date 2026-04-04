package com.api.weather.mapping;

import com.api.weather.dto.WeatherRecordDTO;
import com.api.weather.dto.WeatherResponseDTO;
import com.api.weather.entity.WeatherRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherRecordMapper {

    WeatherRecordDTO toDto(WeatherRecord weatherRecord);


    @Mapping(source = "name", target = "city")
    @Mapping(source = "sys.country", target = "country")

    @Mapping(source = "coord.lat", target = "latitude")
    @Mapping(source = "coord.lon", target = "longitude")

    @Mapping(source = "main.temp", target = "temperature")
    @Mapping(source = "main.feels_like", target = "feelsLike")
    @Mapping(source = "main.temp_min", target = "tempMin")
    @Mapping(source = "main.temp_max", target = "tempMax")
    @Mapping(source = "main.pressure", target = "pressure")
    @Mapping(source = "main.humidity", target = "humidity")

    @Mapping(source = "wind.speed", target = "windSpeed")
    @Mapping(source = "wind.deg", target = "windDeg")
    @Mapping(source = "wind.gust", target = "windGust")

    @Mapping(source = "clouds.all", target = "cloudCoverage")
    @Mapping(source = "visibility", target = "visibility")

    @Mapping(source = "dt", target = "timestamp")
    @Mapping(source = "sys.sunrise", target = "sunrise")
    @Mapping(source = "sys.sunset", target = "sunset")

    // Custom mapping for weather[0]
    @Mapping(source = "weather", target = "weatherMain", qualifiedByName = "mapWeatherMain")
    @Mapping(source = "weather", target = "weatherDescription", qualifiedByName = "mapWeatherDescription")
    @Mapping(source = "weather", target = "weatherIcon", qualifiedByName = "mapWeatherIcon")

    WeatherRecord toEntity(WeatherResponseDTO dto);

    // =========================
    // Custom helpers
    // =========================

    @Named("mapWeatherMain")
    default String mapWeatherMain(List<WeatherResponseDTO.Weather> weather) {
        return (weather != null && !weather.isEmpty()) ? weather.get(0).main() : null;
    }

    @Named("mapWeatherDescription")
    default String mapWeatherDescription(List<WeatherResponseDTO.Weather> weather) {
        return (weather != null && !weather.isEmpty()) ? weather.get(0).description() : null;
    }

    @Named("mapWeatherIcon")
    default String mapWeatherIcon(java.util.List<WeatherResponseDTO.Weather> weather) {
        return (weather != null && !weather.isEmpty()) ? weather.get(0).icon() : null;
    }

}
