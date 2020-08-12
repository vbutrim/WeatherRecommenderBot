package com.vbutrim.bot;

import com.vbutrim.weather.AvailableCitiesManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindCommand extends AuthorizedBotCommand {

    private final AvailableCitiesManager availableCitiesManager;

    FindCommand(ConnectedUsersManager connectedUsersManager, AvailableCitiesManager availableCitiesManager) {
        super("find", "find city id by its name", connectedUsersManager);
        this.availableCitiesManager = availableCitiesManager;
    }

    @Override
    SendMessage handleAndGetMessageForResponse(User user, Chat chat, String[] arguments, ConnectedUser connectedUser) {

        SendMessage message = new SendMessage()
                .setChatId(chat.getId());

        if (arguments.length == 0) {
            return message
                    .setText("Incorrect arguments. Provide any city name to search");
        }

        List<Map.Entry<String, Integer>> cities = availableCitiesManager
                .getFirstNCitiesStartsWith(String.join(" ", Arrays.asList(arguments)));

        if (cities.isEmpty()) {
            return message
                    .setText("No cities :(");
        }

        int maxCityIdLength = cities
                .stream()
                .map(Map.Entry::getValue)
                .max(Comparator.naturalOrder())
                .map(Object::toString)
                .get()
                .length();

        return message
                .enableHtml(true)
                .setText(
                        cities
                                .stream()
                                .map((cityNameAndId) -> String.format(
                                        "<b>%s</b> %s",
                                        addSpacesToBeatify(cityNameAndId.getValue(), maxCityIdLength),
                                        cityNameAndId.getKey()))
                                .collect(Collectors.joining("\n"))
                );
    }

    private static String addSpacesToBeatify(int value, int maxLength) {

        String result = String.valueOf(value);
        if (result.length() == maxLength) {
            return result;
        }

        return " ".repeat(Math.max(0, maxLength - result.length() + 1)) + result;
    }
}
