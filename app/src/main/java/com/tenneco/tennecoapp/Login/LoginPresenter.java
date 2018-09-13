package com.tenneco.tennecoapp.Login;

/**
 * Created by ghoss on 11/09/2018.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    LoginPresenter(LoginContract.View view)
    {
        bindView(view);
    }

    @Override
    public void login(String user, String password) {
        if (validData(user,password))
            mView.signIn(user,password);
    }

    @Override
    public boolean validData(String user, String psw) {

        if (user.isEmpty())
        {
            mView.showUserError();
            return false;
        }
        else
        if (!validEmail(user))
            return  false;
        else
        if (psw.isEmpty())
        {
            mView.showPasswordError();
            return false;
        }
        return validPsw(psw);
    }

    @Override
    public boolean validEmail(CharSequence email) {
        if (!(email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            mView.showEmailError();
        }
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validPsw(CharSequence psw) {
        if (!(psw != null && psw.length()>=6)) {
            mView.showPswError();
        }
        return psw != null && psw.length()>=6;
    }

    @Override
    public void bindView(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}

