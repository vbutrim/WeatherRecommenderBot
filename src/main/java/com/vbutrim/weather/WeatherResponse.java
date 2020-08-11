package com.vbutrim.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class WeatherResponse {

    private double temp;  // Kelvin
    private int pressure;
    private double windSpeed; // meter/sec

    @JsonProperty("main")
    private void unpackNestedMain(Map<String, Object> main) {
        this.temp = (double) main.get("temp");
        this.pressure = (int) main.get("pressure");
    }

    @JsonProperty("wind")
    private void unpackNestedWind(Map<String, Object> wind) {
        try {
            this.windSpeed = (double) wind.get("speed");
        } catch (ClassCastException e) {
            this.windSpeed = (int) wind.get("speed");
        }
    }
}
