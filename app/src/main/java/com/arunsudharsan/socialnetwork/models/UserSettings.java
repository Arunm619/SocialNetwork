package com.arunsudharsan.socialnetwork.models;

/**
 * Created by root on 14/12/17.
 */

public class UserSettings {
    private User user;
    private UserAccountSettings settings;

    public UserSettings(User user, UserAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(UserAccountSettings settings) {
        this.settings = settings;
    }

    public UserSettings(User user) {

        this.user = user;
    }

    public UserSettings() {
    }
}
