package com.vbutrim.bot;

import com.vbutrim.weather.AvailableCitiesManager;
import com.vbutrim.weather.WeatherForecastManager;
import com.vbutrim.weather.WeatherResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Optional;

public class CheckCommand extends AuthorizedBotCommand {

    private static final String WEATHER_RESPONSE_TEMPLATE = "<b>%s</b>\n\n" +
            "Temperature is %s°C\n" +
            "Pressure is %s hPa\n" +
            "Wind speed is %s m/s";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private final AvailableCitiesManager availableCitiesManager;
    private final WeatherForecastManager weatherForecastManager;

    static {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

    CheckCommand(
            ConnectedUsersManager connectedUsersManager,
            AvailableCitiesManager availableCitiesManager,
            WeatherForecastManager weatherForecastManager)
    {
        super("check", "check current weather", connectedUsersManager);
        this.availableCitiesManager = availableCitiesManager;
        this.weatherForecastManager = weatherForecastManager;
    }

    @Override
    SendMessage handleAndGetMessageForResponse(User user, Chat chat, String[] arguments, ConnectedUser connectedUser) {

        SendMessage message = new SendMessage()
                .setChatId(chat.getId())
                .enableHtml(true);

        Optional<String> cityNameO = availableCitiesManager.getCityById(connectedUser.getCityId());
        if (cityNameO.isEmpty()) {
            return message
                    .setText("Current city with id " + connectedUser.getCityId() + " not found\n" +
                            "Please, change your preferences with <b>/change</b> command");
        }

        WeatherResponse weatherResponse = weatherForecastManager.getWeatherForecastByCityId(connectedUser.getCityId());

        return message.setText(
                String.format(
                        WEATHER_RESPONSE_TEMPLATE,
                        cityNameO.get(),
                        DECIMAL_FORMAT.format(weatherResponse.getTempCelsius()),
                        weatherResponse.getPressureHPa(),
                        DECIMAL_FORMAT.format(weatherResponse.getWindSpeedMeterSec())
                )
        );
    }

}
