package com.vbutrim.bot;

import com.vbutrim.weather.AvailableCitiesManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

class StartCommand extends DefaultBotCommand {

    private final ConnectedUsersManager connectedUsersManager;
    private final AvailableCitiesManager availableCitiesManager;

    StartCommand(ConnectedUsersManager connectedUsersManager, AvailableCitiesManager availableCitiesManager) {
        super("start", "start a new chat");
        this.connectedUsersManager = connectedUsersManager;
        this.availableCitiesManager = availableCitiesManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        SendMessage message = new SendMessage()
                .setChatId(chat.getId())
                .enableHtml(true);

        Optional<ConnectedUser> connectedUserO = connectedUsersManager.findUserByChatId(chat.getId());

        if (connectedUserO.isPresent()) {
            int cityId = connectedUserO.get().getCityId();
            Optional<String> cityName = availableCitiesManager.getCityById(cityId);
            if (cityName.isEmpty()) {
                sendResponse(
                        absSender,
                        message
                                .enableHtml(true)
                                .setText("Current city with id " + cityId + " not found\n" +
                                        "Please, change your preferences with <b>/change</b> command"),
                        user
                );
                return;
            }

            sendResponse(
                    absSender,
                    message
                            .setText("You have already logged in!\n" +
                                    "Your current city is: <b>" + cityName.get() + "</b>"),
                    user);

            return;
        }

        ConnectedUser newConnectedUser = connectedUsersManager.register(chat, user);
        Optional<String> cityName = availableCitiesManager.getCityById(newConnectedUser.getCityId());
        if (cityName.isEmpty()) {
            sendResponse(
                    absSender,
                    message
                            .enableHtml(true)
                            .setText("Current city with id " + newConnectedUser.getCityId() + " not found\n" +
                                    "Please, change your preferences with <b>/change</b> command"),
                    user
            );
            return;
        }

        sendResponse(
                absSender,
                message
                        .setText(
                                String.format("Hi, %s!\n" +
                                                "Your current city is: <b>%s</b>\n\n" +
                                                "To change it use next commands:\n" +
                                                " - <b>/find</b> {cityName}\n" +
                                                " - <b>/change</b> {cityId}\n\n" +
                                                "To check the current weather use:\n" +
                                                " - <b>/check</b>\n\n" +
                                                "<i>Pay attention: currently only Russia Cities are available</i>",
                                        newConnectedUser.getNotEmptyName(),
                                        cityName.get())),
                user
        );
    }
}
