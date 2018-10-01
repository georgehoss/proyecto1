package com.tenneco.tennecoapp.Login;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Register.RegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private FirebaseAuth mAuth;
    @BindView(R.id.et_email) EditText mEtEmail;
    @BindView(R.id.et_psw) EditText mEtPsw;
    @BindView(R.id.bt_sign_in) Button mBtLogin;



    public LoginFragment() {
        // Required empty public constructor
    }



    @OnClick(R.id.bt_sign_in) void singIn()
    {
        mPresenter.login(mEtEmail.getText().toString().trim(),mEtPsw.getText().toString().trim());
    }

    @OnClick(R.id.tv_password) void restorePassword(){
        passwordReset();
    }
    @OnClick(R.id.tv_register) void registerFragment(){
        launchRegister();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment}
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        MainActivity main = (MainActivity) getActivity();
        if (main!=null) {
            main.hideMenu();
        }
    }

    @Override
    public void showUserError() {
        mEtEmail.setError(getString(R.string.login_email_empty));
        mEtEmail.requestFocus();
    }

    @Override
    public void showPasswordError() {
        mEtPsw.setError(getString(R.string.login_psw_empty));
        mEtPsw.requestFocus();
    }

    @Override
    public void showLoginError() {
        Toast.makeText(getActivity(), R.string.login_email_or_psw_incorrect, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmailError() {
        mEtEmail.setError(getString(R.string.login_email_invalid));
        mEtEmail.requestFocus();
    }

    @Override
    public void showPswError() {
        mEtPsw.setError(getString(R.string.login_short_psw));
        mEtPsw.requestFocus();
    }

    @Override
    public void signIn(String user, String psw) {
        mBtLogin.setEnabled(false);
        mAuth.signInWithEmailAndPassword(user, psw)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressBar.setVisibility(View.GONE);
                        mBtLogin.setEnabled(true);

                        if (!task.isSuccessful()) {
                            showLoginError();
                        }
                    }
                });
    }

    @Override
    public void launchRegister() {
        if (getActivity()!=null)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
    }

    @Override
    public void passwordReset() {
        if (mPresenter.validEmail(mEtEmail.getText().toString().trim()))
            mAuth.sendPasswordResetEmail(mEtEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showPswResetMsg(mEtEmail.getText().toString().trim());
                            }
                        }
                    });
    }

    @Override
    public void showPswResetMsg(String email) {
        Toast.makeText(getActivity(), getString(R.string.login_rest_psw) + email, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter == null)
            mPresenter = new LoginPresenter(this);
        else
            mPresenter.bindView(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void bindPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.bindView(this);
    }
}
