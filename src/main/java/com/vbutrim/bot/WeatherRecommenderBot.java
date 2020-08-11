package com.vbutrim.bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class WeatherRecommenderBot extends TelegramLongPollingCommandBot {

    private static final Logger logger = LogManager.getLogger(WeatherRecommenderBot.class);

    private final String botToken;

    public WeatherRecommenderBot(
            String botUsername,
            String botToken)
    {
        super(botUsername);
        this.botToken = botToken;

        logger.info("Bot is ready to use");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        sendMessageToChat(update.getMessage().getChat(), "Unknown command");
    }

    private void sendMessageToChat(Chat chat, String text) {

        SendMessage sendMessage = new SendMessage()
                .setText(text)
                .setChatId(chat.getId())
                .enableHtml(true);

        executeSending(sendMessage);
    }

    private void executeSending(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.ERROR,
                    String.format("Error while sending message '%s' to chatId '%s': %s",
                            sendMessage.getText(), sendMessage.getChatId(), e));
        }
    }
}
