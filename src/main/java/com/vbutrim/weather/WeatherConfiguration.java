package com.vbutrim.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConfiguration {

    @Bean
    public WeatherForecastManager weatherForecastManager(
            @Value("${open.weather.api.url}") String apiUrl,
            @Value("${open.weather.api.key}") String apiKey)
    {
        return new WeatherForecastManagerImpl(apiUrl, apiKey);
    }
}
