package com.api.weather.integration;

import com.api.weather.client.OpenWeatherClient;
import com.api.weather.dto.WeatherResponseDTO;
import com.api.weather.entity.WeatherRecord;
import com.api.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.cache.type=simple" // in-memory cache
})
@AutoConfigureMockMvc
@Testcontainers
@EnableCaching
class WeatherIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private WeatherRepository weatherRepository;

    @MockitoBean
    private OpenWeatherClient openWeatherClient;

    /**
    @TestConfiguration
    static class MockConfig {
        @Bean
        public OpenWeatherClient openWeatherClient() {
            return Mockito.mock(OpenWeatherClient.class);
        }
    }
    **/

    private final String city = "London";

    @BeforeEach
    void setUp() {

        weatherRepository.deleteAll();

        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    private WeatherResponseDTO createMockResponse() {
        return new WeatherResponseDTO(
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
    }

    // -----------------------------------
    // TEST 1: Current Weather Endpoint
    // -----------------------------------
    @Test
    void testGetCurrentWeather() throws Exception {
        when(openWeatherClient.getCurrentWeather(city)).thenReturn(createMockResponse());

        mockMvc.perform(get("/weather")
                        .param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.temperature").value(20.0))
                .andExpect(jsonPath("$.weatherMain").value("Clear"));

        // Verify DB save
        List<WeatherRecord> records = weatherRepository.findAll();
        assert(records.size() == 1);

        // Verify API called once
        verify(openWeatherClient, times(1)).getCurrentWeather(city);
    }

    // -----------------------------------
    // TEST 2: Caching Behavior
    // -----------------------------------
    @Test
    void testCaching() throws Exception {

    }

    // -----------------------------------
    // TEST 3: Historical Weather
    // -----------------------------------
    @Test
    void testGetHistoricalWeather() throws Exception {

        WeatherRecord r1 = new WeatherRecord();
        r1.setCity(city);
        r1.setTemperature(25.0);
        r1.setTimestamp(2L);

        WeatherRecord r2 = new WeatherRecord();
        r2.setCity(city);
        r2.setTemperature(20.0);
        r2.setTimestamp(1L);

        weatherRepository.saveAll(List.of(r1, r2));

        mockMvc.perform(get("/weather/history")
                        .param("city", city)
                        .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].temperature").value(25.0));
    }
}