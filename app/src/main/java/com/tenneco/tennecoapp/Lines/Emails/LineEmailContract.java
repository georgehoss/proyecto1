package com.tenneco.tennecoapp.Lines.Emails;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.EmailList;

/**
 * Created by ghoss on 08/12/2018.
 */
public interface LineEmailContract {
    interface View extends BaseView<Presenter>{
        void updateLine();
        void showSelectListDialog(int position);
        void setAddressDw(String addressDw);
        void setAddressR1(String addressR1);
        void setAddressR2(String addressR2);
        void setAddressR3(String addressR3);
        void setAddressFQT (String addressFQT);
        void setAddressLine (String addressLine);
        void showEmailListError();
        void launchEmailList();
        void hideLine();
        void showLine();
        void hideDw();
        void showDw();
        void hideR1();
        void showR1();
        void hideR2();
        void showR2();
        void hideR3();
        void showR3();
        void hideLeak();
        void showLeak();

    }

    interface Presenter extends BasePresenter<View>{
        String getAddresses(EmailList shift1, EmailList shift2, EmailList shift3);
    }
}
