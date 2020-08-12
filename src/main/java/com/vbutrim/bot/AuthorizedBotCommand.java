package com.vbutrim.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

abstract class AuthorizedBotCommand extends DefaultBotCommand {

    final ConnectedUsersManager connectedUsersManager;

    AuthorizedBotCommand(
            String commandIdentifier,
            String description,
            ConnectedUsersManager connectedUsersManager)
    {
        super(commandIdentifier, description);
        this.connectedUsersManager = connectedUsersManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        Optional<ConnectedUser> connectedUserO = connectedUsersManager.findUserByChatId(chat.getId());
        if (connectedUserO.isEmpty()) {
            SendMessage message = new SendMessage()
                    .setChatId(chat.getId())
                    .setText("Hey! You aren't authorized!\nPlease, do it with `/start` command");
            sendResponse(absSender, message, user);
            return;
        }

        SendMessage message = handleAndGetMessageForResponse(user, chat, arguments, connectedUserO.get());
        sendResponse(absSender, message, user);
    }

    abstract SendMessage handleAndGetMessageForResponse(
            User user,
            Chat chat,
            String[] arguments,
            ConnectedUser connectedUser);
}
