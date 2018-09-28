package com.tenneco.tennecoapp.User;

import android.content.Context;

import com.tenneco.tennecoapp.Model.User;


/**
 * Created by ghoss on 26/09/2018.
 */
public interface UserContract {
    void hideProgressBar();
    void showFloatingButton();
    void getUsers();
    void addEditDialog(User user, Context context);
    void editDeleteDialog(User user);
    void delete(String id);
    void addEditEmployee(User user);
    void deleteDialog(User user);
}
