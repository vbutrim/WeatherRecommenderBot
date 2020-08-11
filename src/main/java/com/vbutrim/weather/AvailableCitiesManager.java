package com.vbutrim.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

class AvailableCitiesManager {

    private static final Logger logger = LogManager.getLogger(AvailableCitiesManager.class);

    private static final int CITIES_COUNT_IN_SEARCH_RESPONSE = 5;

    private final Map<Integer, String> cityNameById;
    private final Map<String, Integer> cityIdByName;

    AvailableCitiesManager() {

        this.cityNameById = new HashMap<>();
        this.cityIdByName = new HashMap<>();

        logger.info("Initializing...");
        try (Scanner sc = new Scanner(
                new FileReader(this.getClass().getResource("available_city_ids_utf8").toString().substring(6))))
        {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    List<Map.Entry<String, Integer>> getFirstNCitiesStartsWith(String str) {
        Objects.requireNonNull(str, "input str can't be null");

        return cityIdByName
                .entrySet()
                .stream()
                .filter(x -> x.getKey().startsWith(str))
                .limit(CITIES_COUNT_IN_SEARCH_RESPONSE)
                .collect(Collectors.toList());
    }

    Optional<String> getCityById(int cityId) {
        if (!cityNameById.containsKey(cityId)) {
            return Optional.empty();
        }

        return Optional.of(cityNameById.get(cityId));
    }
}
