package com.tenneco.tennecoapp.Register;

/**
 * Created by ghoss on 11/09/2018.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mView;

    RegisterPresenter(RegisterContract.View view) {
        this.mView = view;
    }

    @Override
    public void register(String firstName, String lastName, String email, String password) {
        if (validData(firstName,lastName,email,password))
            mView.signUpUser(firstName,lastName,email,password);
    }

    @Override
    public boolean validData(String firstName, String lastName, String email, String password) {

        if (firstName.isEmpty())
        {
            mView.showFirstNameError();
            return false;
        }
        else
        if (lastName.isEmpty())
        {
            mView.showLastNameError();
            return false;
        }
        else
        if (email.isEmpty())
        {
            mView.showEmailError();
            return false;
        }
        else
        if (!validEmail(email))
            return  false;
        else
        if (password.isEmpty())
        {
            mView.showPasswordError();
            return false;
        }
        return validPsw(password);
    }


    @Override
    public boolean validEmail(CharSequence email) {
        if (!(email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            mView.showEmailFormatError();
        }
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validPsw(CharSequence psw) {
        if (!(psw != null && psw.length()>=6)) {
            mView.showPswLengthError();
        }
        return psw != null && psw.length()>=6;
    }

    @Override
    public void bindView(RegisterContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}

