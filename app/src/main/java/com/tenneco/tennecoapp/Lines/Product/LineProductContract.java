package com.tenneco.tennecoapp.Lines.Product;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.Shift;

import java.util.ArrayList;

/**
 * Created by ghoss on 09/12/2018.
 */
public interface LineProductContract {
    interface View extends BaseView<Presenter>{
        void initAdapters();
        void updateLine();
        void hideshift1();
        void showshift1();
        void hideshift2();
        void showshift2();
        void hideshift3();
        void showshift3();
        void hideDelete();
        void showDelete();
        void showProductListDialog(ArrayList<Product> products);
        void showProductsEmpty();
        void launchProducts();
        void showShiftDialog(final Shift shift, final int shiftNumber, Context context);
    }

    interface Presenter extends BasePresenter<View>{
        Shift getshift(String sh1,String eh1,String t1,
                       String sh2,String eh2,String t2,
                       String sh3,String eh3,String t3,
                       String sh4,String eh4,String t4,
                       String sh5,String eh5,String t5,
                       String sh6,String eh6,String t6,
                       String sh7,String eh7,String t7,
                       String sh8,String eh8,String t8);
    }
}
