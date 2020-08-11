package com.vbutrim.weather;

public interface WeatherForecastManager {


    WeatherResponse getWeatherForecastByCityId(long cityId);
}
