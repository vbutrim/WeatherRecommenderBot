package com.vbutrim.bot;

import com.google.common.base.Strings;
import com.vbutrim.weather.AvailableCitiesManager;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ConnectedUser {

    static final int DEFAULT_CITY_ID = AvailableCitiesManager.DEFAULT_CITY_ID;

    private final long chatId;
    private final String userName;
    private final String fullName;
    private final int cityId;

    private ConnectedUser(long chatId, String userName, String fullName, int cityId) {
        this.chatId = chatId;
        this.userName = userName;
        this.fullName = fullName;
        this.cityId = cityId;
    }

    public ConnectedUser(Chat chat, User user) {
        this.chatId = chat.getId();
        this.userName = user.getUserName();
        this.fullName = (user.getFirstName() + " " + user.getLastName()).trim();
        this.cityId = DEFAULT_CITY_ID;
    }

    public ConnectedUser withCityId(int cityId) {
        return new ConnectedUser(
                chatId,
                userName,
                fullName,
                cityId
        );
    }

    public long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCityId() {
        return cityId;
    }

    public String getNotEmptyName() {
        if (!Strings.isNullOrEmpty(getUserName())) {
            return getUserName();
        }

        return getFullName();
    }
}
