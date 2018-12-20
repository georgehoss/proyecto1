package com.tenneco.tennecoapp.Lines.Emails;

import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;

/**
 * Created by ghoss on 08/12/2018.
 */
public class LineEmailPresenter implements LineEmailContract.Presenter {
    private LineEmailContract.View mView;

    public LineEmailPresenter(LineEmailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public String getAddresses(EmailList shift1, EmailList shift2, EmailList shift3) {
        final String nextLine ="\n";
        final String cc = "CC: ";
        StringBuilder ad = new StringBuilder();

        ad.append("1st Shift:");
        ad.append(nextLine);
        ad.append(nextLine);
        for (Email email : shift1.getEmails())
            if (email.isShift1() && !email.isCc1())
                ad.append(email.getEmail()).append(" ");
        ad.append(nextLine);
        ad.append(nextLine);
        ad.append(cc);
        for (Email email : shift1.getEmails())
            if (email.isShift1() && email.isCc1())
                ad.append(email.getEmail()).append(" ");
        ad.append(nextLine);
        ad.append(nextLine);


        ad.append("2nd Shift:");
        ad.append(nextLine);
        ad.append(nextLine);
        for (Email email : shift2.getEmails())
            if (email.isShift1() && !email.isCc1())
            ad.append(email.getEmail()).append(" ");
        ad.append(nextLine);
        ad.append(nextLine);
        ad.append(cc);
        for (Email email : shift1.getEmails())
            if (email.isShift1() && email.isCc1())
            ad.append(email.getEmail()).append(" ");
        ad.append(nextLine);
        ad.append(nextLine);


        ad.append("3rd Shift:");
        ad.append(nextLine);
        ad.append(nextLine);
        for (Email email : shift3.getEmails())
            if (email.isShift1() && !email.isCc1())
                ad.append(email.getEmail()).append(" ");
        ad.append(nextLine);
        ad.append(nextLine);
        ad.append(cc);
        for (Email email : shift3.getEmails())
            if (email.isShift1() && email.isCc1())
                ad.append(email.getEmail()).append(" ");

        return ad.toString();
    }

    @Override
    public void bindView(LineEmailContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
