package com.vbutrim.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

class StopCommand extends AuthorizedBotCommand {

    StopCommand(ConnectedUsersManager connectedUsersManager) {
        super("stop", "stop this chat", connectedUsersManager);
    }

    @Override
    SendMessage handleAndGetMessageForResponse(
            User user,
            Chat chat,
            String[] arguments,
            ConnectedUser connectedUser)
    {
        connectedUsersManager.unregisterAndRemoveAllData(connectedUser);
        return new SendMessage()
                .setChatId(chat.getId())
                .setText("Bye!");
    }
}
