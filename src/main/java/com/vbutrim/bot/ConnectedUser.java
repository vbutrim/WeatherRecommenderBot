package com.vbutrim.bot;

import com.google.common.base.Strings;
import com.vbutrim.weather.AvailableCitiesManager;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "telegram_connected_users")
public class ConnectedUser {

    static final int DEFAULT_CITY_ID = AvailableCitiesManager.DEFAULT_CITY_ID;

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "city_id")
    private int cityId;

    // jpa only
    protected ConnectedUser() {
    }

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

    @Override
    public String toString() {
        return "ConnectedUser{" +
                "chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", cityId=" + cityId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectedUser)) return false;
        ConnectedUser that = (ConnectedUser) o;
        return getCityId() == that.getCityId() &&
                getChatId() == that.getChatId() &&
                Objects.equals(getUserName(), that.getUserName()) &&
                Objects.equals(getFullName(), that.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getUserName(), getFullName(), getCityId());
    }
}
