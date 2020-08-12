package com.vbutrim.bot;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Actually this is only a simulation of {@link ConnectedUser} storage
 */
public class ConnectedUsersManager {

    private final Map<Long, ConnectedUser> connectedUserByChatId;

    public ConnectedUsersManager() {
        this.connectedUserByChatId = new HashMap<>();
    }

    public Optional<ConnectedUser> findUserByChatId(long chatId) {
        if (connectedUserByChatId.containsKey(chatId)) {
            return Optional.of(connectedUserByChatId.get(chatId));
        }
        return Optional.empty();
    }

    private ConnectedUser findUserByChatIdOrThrow(long chatId) {
        //noinspection OptionalGetWithoutIsPresent
        return findUserByChatId(chatId).get();
    }

    public ConnectedUser register(Chat chat, User user) {
        ConnectedUser newConnectedUser = new ConnectedUser(chat, user);

        connectedUserByChatId.put(
                chat.getId(),
                newConnectedUser
        );

        return newConnectedUser;
    }

    public void unregisterAndRemoveAllData(ConnectedUser connectedUser) {
        connectedUserByChatId.remove(connectedUser.getChatId());
    }

    public void changeCityId(Chat chat, User user, int cityId) {

    }
}
