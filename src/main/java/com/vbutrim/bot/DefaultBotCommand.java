package com.vbutrim.bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class DefaultBotCommand extends BotCommand {

    private static final Logger logger = LogManager.getLogger(DefaultBotCommand.class);

    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    DefaultBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
        logger.info("Registering /" + commandIdentifier + " command");
    }

    void sendResponse(AbsSender sender, SendMessage message, User user) {
        try {
            logger.log(Level.INFO, String.format("Handling command '%s' from '%s'", getCommandIdentifier(), user.getId()));
            sender.execute(message);
        } catch (TelegramApiException e) {
            logger.error(String.format(
                    "Got an error while handling command '%s' from '%s'", getCommandIdentifier(), user.getId()), e);
        }
    }
}
