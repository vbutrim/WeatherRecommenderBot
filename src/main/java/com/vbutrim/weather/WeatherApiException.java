package com.vbutrim.weather;

public class WeatherApiException extends RuntimeException {

    public WeatherApiException(String message) {
        super(message);
    }

    WeatherApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
