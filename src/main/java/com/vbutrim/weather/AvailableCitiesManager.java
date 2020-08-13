package com.vbutrim.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AvailableCitiesManager {

    private static final Logger logger = LogManager.getLogger(AvailableCitiesManager.class);

    public static final int DEFAULT_CITY_ID = 524894;
    private static final String DEFAULT_CITY_NAME = "Moscow";
    private static final int CITIES_COUNT_IN_SEARCH_RESPONSE = 5;

    private final Map<Integer, String> cityNameById;
    private final Map<String, Integer> cityIdByName;

    AvailableCitiesManager() {

        this.cityNameById = new HashMap<>();
        this.cityIdByName = new HashMap<>();

        logger.info("Initializing...");
        try (InputStream inputStream =
                     AvailableCitiesManager.class.getResourceAsStream("available_city_ids_utf8"))
        {
            try (Scanner sc = new Scanner(inputStream)) {
                while (sc.hasNextLine()) {
                    String[] cityIdAndName = sc.nextLine().split("\t");
                    int cityId = Integer.parseInt(cityIdAndName[0]);
                    String cityName = cityIdAndName[1];

                    if (cityIdByName.containsKey(cityName)) {
                        continue;
                    }

                    cityIdByName.put(cityName, cityId);
                    cityNameById.put(cityId, cityName);
                }

                logger.info("Successfully initialized");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Map.Entry<String, Integer>> getFirstNCitiesStartsWith(String str) {
        Objects.requireNonNull(str, "input str can't be null");

        final String startsWithPattern = str.trim().toLowerCase();

        return cityIdByName
                .entrySet()
                .stream()
                .filter(x -> x.getKey().toLowerCase().startsWith(startsWithPattern))
                .limit(CITIES_COUNT_IN_SEARCH_RESPONSE)
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }

    public Optional<String> getCityById(int cityId) {

        if (cityId == DEFAULT_CITY_ID) {
            return Optional.of(DEFAULT_CITY_NAME);
        }

        if (!cityNameById.containsKey(cityId)) {
            return Optional.empty();
        }

        return Optional.of(cityNameById.get(cityId));
    }
}
