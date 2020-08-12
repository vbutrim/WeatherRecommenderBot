package com.vbutrim.bot;

import com.vbutrim.weather.AvailableCitiesManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public class ChangeCommand extends AuthorizedBotCommand {

    private final AvailableCitiesManager availableCitiesManager;

    ChangeCommand(ConnectedUsersManager connectedUsersManager, AvailableCitiesManager availableCitiesManager) {
        super("change", "change current city", connectedUsersManager);
        this.availableCitiesManager = availableCitiesManager;
    }

    @Override
    SendMessage handleAndGetMessageForResponse(User user, Chat chat, String[] arguments, ConnectedUser connectedUser) {

        SendMessage message = new SendMessage()
                .setChatId(chat.getId());

        if (arguments.length != 1) {
            return message
                    .setText("Incorrect amount of arguments. Type cityId only");
        }

        try {
            int cityId = Integer.parseInt(arguments[0]);
            Optional<String> cityNameO = availableCitiesManager.getCityById(cityId);
            if (cityNameO.isEmpty()) {
                return message
                        .setText("City with id " + cityId + " not found");
            }

            connectedUsersManager.changeCityId(chat, user, cityId);
            return message
                    .setText("Current city is " + cityNameO.get());
        } catch (NumberFormatException e) {
            return message
                    .setText("Incorrect argument. Please, type cityId");
        }
    }
}
