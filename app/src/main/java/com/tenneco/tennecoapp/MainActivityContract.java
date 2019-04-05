package com.tenneco.tennecoapp;

/**
 * Created by ghoss on 12/09/2018.
 */
public interface MainActivityContract {
    interface View {
        void launchSplash();
        void launchMain();
        void launchProduction();
        void launchUsers();
        void launchConfiguration();
        void launchSchedule();
        void launchEmail();
        void showMenu();
        void hideMenu();
        void signOut();
        void restoreButtons();
        void setProduction();
        void setUsers();
        void setConfiguration();
        void setEmail();
    }

}
