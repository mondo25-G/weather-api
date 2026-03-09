package com.api.weather.repository;

import com.app.weather.entity.WeatherRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends MongoRepository<WeatherRecord, String> {

    List<WeatherRecord> findByCityOrderByTimestampDesc(String city);
}
