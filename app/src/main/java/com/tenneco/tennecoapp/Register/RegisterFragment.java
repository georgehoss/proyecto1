package com.tenneco.tennecoapp.Register;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.tenneco.tennecoapp.Login.LoginFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment implements RegisterContract.View {
    private RegisterContract.Presenter mPresenter;
    private FirebaseAuth mAuth;
    @BindView(R.id.et_firstName)
    EditText mEtFirstName;
    @BindView(R.id.et_lastName) EditText mEtLastName;
    @BindView(R.id.et_email) EditText mEtEmail;
    @BindView(R.id.et_psw) EditText mEtPsw;
    @BindView(R.id.bt_sign_up) Button mBtSignUp;


    @OnClick(R.id.bt_sign_up) void signUp(){
        mPresenter.register(mEtFirstName.getText().toString().trim(),
                mEtLastName.getText().toString().trim(),
                mEtEmail.getText().toString().trim(),
                mEtPsw.getText().toString().trim());
    }

    @OnClick (R.id.tv_login) void signIn()
    {
        launchSignIn();
    }


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter == null)
            mPresenter = new RegisterPresenter(this);
        else
            mPresenter.bindView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void showFirstNameError() {
        mEtFirstName.setError(getString(R.string.regiter_first_name));
        mEtFirstName.requestFocus();
    }

    @Override
    public void showLastNameError() {
        mEtLastName.setError(getString(R.string.register_last_name_empty));
        mEtLastName.requestFocus();
    }

    @Override
    public void showEmailError() {
        mEtEmail.setError(getString(R.string.login_email_empty));
        mEtEmail.requestFocus();
    }

    @Override
    public void showEmailFormatError() {
        mEtEmail.setError(getString(R.string.login_email_invalid));
        mEtEmail.requestFocus();
    }

    @Override
    public void showPswLengthError() {
        mEtPsw.setError(getString(R.string.login_short_psw));
        mEtPsw.requestFocus();
    }

    @Override
    public void showPasswordError() {
        mEtPsw.setError(getString(R.string.login_psw_empty));
        mEtPsw.requestFocus();
    }

    @Override
    public void signUpUser(final String firstName, final String lastName, String email, String password) {
        mBtSignUp.setEnabled(false);
        if (getActivity()!=null)
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            mBtSignUp.setEnabled(true);
                            if (!task.isSuccessful()) {

                                if (task.getException()!=null && task.getException().getMessage()!=null && task.getException().getMessage().contains("email address is already in use"))
                                    showEmailDuplicatedError();
                                else
                                    showSingUpFailedError();
                            }
                            else {
                                updateUserInfo(firstName+" "+lastName);
                            }

                        }
                    });
    }

    @Override
    public void showEmailDuplicatedError() {
        Toast.makeText(getActivity(), R.string.register_email_already, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSingUpFailedError() {
        Toast.makeText(getActivity(), R.string.register_something_wrong, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void updateUserInfo(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        if (user!=null)
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("UpdateUser", "User profile updated.");
                            }
                        }
                    });
    }

    @Override
    public void launchSignIn() {
        if (getActivity()!=null)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
    }

    @Override
    public void bindPresenter(RegisterContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
