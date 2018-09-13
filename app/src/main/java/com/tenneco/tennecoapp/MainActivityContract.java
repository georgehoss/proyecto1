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
        void showMenu();
        void hideMenu();
        void signOut();
    }

}
