package com.tenneco.tennecoapp.Emails.List;

import android.content.Context;

import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;

import java.util.ArrayList;

/**
 * Created by ghoss on 08/12/2018.
 */
public class AddEmailListPresenter implements AddEmailListContract.Presenter {
    private AddEmailListContract.View mView;

    public AddEmailListPresenter(AddEmailListContract.View mView) {
        this.mView = mView;
    }

    @Override
    public EmailList initData(Context context, String id) {
        return new EmailList("",id,new ArrayList<Email>());
    }

    @Override
    public boolean validName(String name) {

        if (name.isEmpty())
            mView.showNameEmptyError();

        return !name.isEmpty();
    }

    @Override
    public void saveChanges(String name, EmailList emailList) {
        if ( emailList==null || !reviewSelected(emailList))
            mView.showSelectionError();
            else if (validName(name)) {
            emailList.setName(name);
            mView.saveData(emailList);
        }
    }

    @Override
    public ArrayList<Email> setSelected(ArrayList<Email> eDefault, ArrayList<Email> eSaved) {
        if (eDefault!=null  && eSaved!=null) {
            for (Email email : eDefault)
                for (Email saved : eSaved)
                    if (email.getId().equals(saved.getId())) {
                        email.setShift1(saved.isShift1());
                        email.setCc1(saved.isCc1());
                    }
        }

        return eDefault;
    }

    @Override
    public boolean reviewSelected(EmailList emailList) {
        boolean selected = false;

        for (Email email : emailList.getEmails())
            if (email.isShift1())
                selected = true;


        return selected;
    }

    @Override
    public void bindView(AddEmailListContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
