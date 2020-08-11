package com.vbutrim.weather;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class WeatherForecastManagerImpl implements WeatherForecastManager, Closeable {

    private static final Logger logger = LogManager.getLogger(WeatherForecastManagerImpl.class);

    private static final String GET_WEATHER_API_METHOD = "/weather?id=%s&appid=%s&lang=ru";

    private static final long MOSCOW_ID = 524894;

    private final String apiUrl;
    private final String apiKey;

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WeatherForecastManagerImpl(
            String apiUrl,
            String apiKey)
    {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;

        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public WeatherResponse getWeatherForecastByCityId(long cityId) {

        HttpGet httpGet = new HttpGet(String.format(apiUrl + GET_WEATHER_API_METHOD, cityId, apiKey));
        logger.info("Going to perform: " + httpGet);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new WeatherApiException("Expected 200, got " + statusCode);
            }
            return objectMapper.readValue(response.getEntity().getContent(), WeatherResponse.class);
        } catch (IOException e) {
            throw new WeatherApiException(
                    String.format("Error getting weather forecast for city with id %s", cityId),
                    e);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
