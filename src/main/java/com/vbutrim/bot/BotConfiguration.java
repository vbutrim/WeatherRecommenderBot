package com.vbutrim.bot;

import com.vbutrim.weather.AvailableCitiesManager;
import com.vbutrim.weather.WeatherConfiguration;
import com.vbutrim.weather.WeatherForecastManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
@Import({
        WeatherConfiguration.class
})
public class BotConfiguration {

    private static final Logger logger = LogManager.getLogger(BotConfiguration.class);

    @Bean
    public ConnectedUsersManager connectedUsersManager() {
        return new ConnectedUsersManager();
    }

    @Bean
    public WeatherRecommenderBot weatherRecommenderBot(
            @Value("${telegram.bot.name}") String telegramBotName,
            @Value("${telegram.bot.token}") String telegramBotToken,
            WeatherForecastManager weatherForecastManager,
            ConnectedUsersManager connectedUsersManager,
            AvailableCitiesManager availableCitiesManager)
    {
        WeatherRecommenderBot weatherRecommenderBot = null;

        try {
            logger.log(Level.INFO, "Initializing API context");
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            logger.log(Level.INFO, "Registering WeatherRecommenderBot");
            weatherRecommenderBot = new WeatherRecommenderBot(
                    telegramBotName,
                    telegramBotToken,
                    weatherForecastManager,
                    connectedUsersManager,
                    availableCitiesManager
            );

            botsApi.registerBot(weatherRecommenderBot);
            logger.log(Level.INFO, "WeatherRecommenderBot is ready for work!");
        } catch (TelegramApiRequestException e) {
            logger.log(Level.ERROR, "Error while initializing bot!", e);
        }

        return weatherRecommenderBot;
    }
}
