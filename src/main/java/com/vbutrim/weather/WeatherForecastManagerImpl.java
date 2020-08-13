package com.vbutrim.weather;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class WeatherForecastManagerImpl implements WeatherForecastManager, Closeable {

    private static final int QUERY_TO_LOG_MAX_LENGTH = 69;

    private static final Logger logger = LogManager.getLogger(WeatherForecastManagerImpl.class);

    private static final String GET_WEATHER_API_METHOD = "%s/weather?id=%s&appid=%s&lang=ru";

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
    public WeatherResponse getWeatherForecastByCityId(int cityId) {

        HttpGet httpGet = new HttpGet(String.format(GET_WEATHER_API_METHOD, apiUrl, cityId, apiKey));

        logHttpRequest(httpGet);

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

    private void logHttpRequest(HttpRequestBase httpRequestBase) {

        String queryToLog = httpRequestBase.toString().replace(apiKey, "API_KEY");
        logger.info("Going to perform {}...",
                queryToLog.length() > QUERY_TO_LOG_MAX_LENGTH
                        ? queryToLog.substring(0, QUERY_TO_LOG_MAX_LENGTH)
                        : queryToLog);
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
