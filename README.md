## Project Overview

The Weather App is a backend application built with **Spring Boot**, **MongoDB**, and **Redis**, packaged with **Docker**.

It fetches real-time weather data from the **OpenWeatherMap API**, stores historical weather records in MongoDB, and optionally caches recent queries in Redis for faster responses.

## Key Features

### Weather Data Retrieval
- Fetches current weather for any city using the OpenWeatherMap API.
- Maps API responses to **immutable DTO records** for safe and clean handling.

### Historical Weather Records
- Stores weather records in **MongoDB**.
- Supports fetching recent historical records for any city.

### Caching
- Optionally caches recent weather requests in **Redis**.
- Reduces unnecessary external API calls and improves performance.

### DTOs & API Mapping
- All OpenWeatherMap API fields mapped to a **single nested `WeatherResponseDTO` record**.
- Maps API response to a **WeatherRecord entity** for MongoDB.

### REST API Design
- Synchronous Spring MVC controllers.
- Clean endpoints with proper HTTP status codes.
- Endpoints:
    - `GET /weather?city={cityName}` → Current weather
    - `GET /weather/history?city={cityName}&limit={N}` → Historical weather records

### Dockerized Deployment
- Runs the application, MongoDB, and Redis via **Docker Compose**.
- Simplifies local development and testing.