package com.vbutrim.bot;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ConnectedUsersManager {

    private final ConnectedUsersRepository connectedUsersRepository;

    public ConnectedUsersManager(ConnectedUsersRepository connectedUsersRepository) {
        this.connectedUsersRepository = connectedUsersRepository;
    }

    public Optional<ConnectedUser> findUserByChatId(long chatId) {
        return connectedUsersRepository.findByChatId(chatId);
    }

    public ConnectedUser register(Chat chat, User user) {
        ConnectedUser newConnectedUser = new ConnectedUser(chat, user);
        return connectedUsersRepository.save(newConnectedUser);
    }

    public void unregisterAndRemoveAllData(ConnectedUser connectedUser) {
        connectedUsersRepository.deleteById(connectedUser.getChatId());
    }

    /**
     * @throws NoSuchElementException if user hasn't started chat with bot
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void changeCityId(Chat chat, int cityId) {
        connectedUsersRepository.save(
                findUserByChatId(chat.getId())
                        .get()
                        .withCityId(cityId)
        );
    }
}
