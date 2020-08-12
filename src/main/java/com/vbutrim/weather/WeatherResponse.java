package com.vbutrim.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings("unused")
public class WeatherResponse {

    private static final double KELVIN_TO_CELSIUS_DELTA = -273.15;

    private double tempKelvin;  // Kelvin
    private int pressureHPa;    //hPa
    private double windSpeedMeterSec; // meter/sec

    @JsonProperty("main")
    private void unpackNestedMain(Map<String, Object> main) {
        this.tempKelvin = (double) main.get("temp");
        this.pressureHPa = (int) main.get("pressure");
    }

    @JsonProperty("wind")
    private void unpackNestedWind(Map<String, Object> wind) {
        try {
            this.windSpeedMeterSec = (double) wind.get("speed");
        } catch (ClassCastException e) {
            this.windSpeedMeterSec = (int) wind.get("speed");
        }
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "temp=" + tempKelvin +
                ", pressure=" + pressureHPa +
                ", windSpeed=" + windSpeedMeterSec +
                '}';
    }

    public double getTempCelsius() {
        return tempKelvin + KELVIN_TO_CELSIUS_DELTA;
    }

    public int getPressureHPa() {
        return pressureHPa;
    }

    public double getWindSpeedMeterSec() {
        return windSpeedMeterSec;
    }
}
