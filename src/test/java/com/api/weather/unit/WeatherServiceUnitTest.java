package com.api.weather.unit;

import com.api.weather.client.OpenWeatherClient;
import com.api.weather.dto.WeatherResponseDTO;
import com.api.weather.dto.WeatherRecordDTO;
import com.api.weather.entity.WeatherRecord;
import com.api.weather.mapping.WeatherRecordMapper;
import com.api.weather.repository.WeatherRepository;
import com.api.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Profile("test")
class WeatherServiceUnitTest {

    private OpenWeatherClient openWeatherClient;
    private WeatherRepository weatherRepository;
    private WeatherRecordMapper mapper;
    private WeatherService weatherService;

    // Simple manual cache for testing
    private Cache weatherCache;

    private final String city = "London";
    private WeatherResponseDTO responseDTO;
    private WeatherRecord recordEntity;
    private WeatherRecordDTO recordDTO;

    @BeforeEach
    void setUp() {
        openWeatherClient = mock(OpenWeatherClient.class);
        weatherRepository = mock(WeatherRepository.class);
        mapper = mock(WeatherRecordMapper.class);

        // Manual cache setup
        weatherCache = new ConcurrentMapCache("weatherCache");

        weatherService = new WeatherService(openWeatherClient, weatherRepository, mapper);

        // Sample data
        responseDTO = new WeatherResponseDTO(
                new WeatherResponseDTO.Coord(0.0, 51.0),
                List.of(new WeatherResponseDTO.Weather("Clear", "clear sky", "01d")),
                "stations",
                new WeatherResponseDTO.Main(20.0, 19.0, 18.0, 21.0, 1012, 50),
                10000,
                new WeatherResponseDTO.Wind(5.0, 180, 7.0),
                new WeatherResponseDTO.Clouds(0),
                123456789L,
                new WeatherResponseDTO.Sys("GB", 123450000L, 123460000L),
                0,
                2643743L,
                "London",
                200L
        );

        recordEntity = new WeatherRecord();
        recordEntity.setCity(city);
        recordEntity.setTemperature(20.0);
        recordEntity.setWeatherMain("Clear");

        recordDTO = new WeatherRecordDTO(
                "London", 20.0, "Clear"
        );
    }

    @Test
    void testGetWeather_cachingManually() {
        when(openWeatherClient.getCurrentWeather(city)).thenReturn(responseDTO);
        when(mapper.toEntity(responseDTO)).thenReturn(recordEntity);

        // Simulate caching manually
        WeatherRecordDTO firstCall = weatherService.getCurrentWeather(city);
        weatherCache.put(city, firstCall);

        // Second call → fetch from manual cache instead of API
        WeatherRecordDTO cachedCall = (WeatherRecordDTO) weatherCache.get(city).get();

        assertSame(firstCall, cachedCall);
        verify(openWeatherClient, times(1)).getCurrentWeather(city);
        verify(weatherRepository, times(1)).save(recordEntity);
    }

    @Test
    void testGetCurrentWeather_returnsDto() {
        when(openWeatherClient.getCurrentWeather(city)).thenReturn(responseDTO);
        when(mapper.toEntity(responseDTO)).thenReturn(recordEntity);
        when(mapper.toDto(recordEntity)).thenReturn(recordDTO);

        WeatherRecordDTO result = weatherService.getCurrentWeather(city);

        assertNotNull(result);
        assertEquals("London", result.getCity());
        assertEquals(20.0, result.getTemperature());
        assertEquals("Clear", result.getWeatherMain());

        verify(mapper).toDto(recordEntity);
    }

    @Test
    void testGetHistoricalWeather_limitsResults() {
        WeatherRecord record1 = new WeatherRecord();
        record1.setCity(city);
        WeatherRecord record2 = new WeatherRecord();
        record2.setCity(city);

        record1.setTimestamp(2L); // newer
        record2.setTimestamp(1L); // older

        List<WeatherRecord> records = List.of(record1, record2);

        when(weatherRepository.findByCityOrderByTimestampDesc(city)).thenReturn(records);
        when(mapper.toDto(record1)).thenReturn(new WeatherRecordDTO(city, 20.0, "Clear"));
        when(mapper.toDto(record2)).thenReturn(new WeatherRecordDTO(city, 19.0, "Clouds"));

        List<WeatherRecordDTO> result = weatherService.getHistoricalWeather(city, 1);

        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getTemperature());
    }
}