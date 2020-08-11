package com.vbutrim.bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
public class BotConfiguration {

    private static final Logger logger = LogManager.getLogger(BotConfiguration.class);

    @Bean
    public WeatherRecommenderBot weatherRecommenderBot(
            @Value("${telegram.bot.name}") String telegramBotName,
            @Value("${telegram.bot.token}") String telegramBotToken)
    {
        WeatherRecommenderBot weatherRecommenderBot = null;

        try {
            logger.log(Level.INFO, "Initializing API context");
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            logger.log(Level.INFO, "Registering WeatherRecommenderBot");
            weatherRecommenderBot = new WeatherRecommenderBot(
                    telegramBotName,
                    telegramBotToken
            );

            botsApi.registerBot(weatherRecommenderBot);
            logger.log(Level.INFO, "WeatherRecommenderBot is ready for work!");
        } catch (TelegramApiRequestException e) {
            logger.log(Level.ERROR, "Error while initializing bot!", e);
        }

        return weatherRecommenderBot;
    }
}
