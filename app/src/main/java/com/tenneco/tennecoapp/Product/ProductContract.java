package com.tenneco.tennecoapp.Product;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Product;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface ProductContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void showFloatingButton();
        void getProducts();
        void addEditDialog(Product product, Context context);
        void editDeleteDialog(Product product);
        void delete(String id);
        void saveData(Product product);

    }
    interface Presenter extends BasePresenter<View> {

    }
}
