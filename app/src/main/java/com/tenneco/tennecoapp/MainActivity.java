package com.tenneco.tennecoapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tenneco.tennecoapp.Configuration.ConfigurationFragment;
import com.tenneco.tennecoapp.ConfigurationMenu.MenuConfigFragment;
import com.tenneco.tennecoapp.Emails.EmailFragment;
import com.tenneco.tennecoapp.Emails.EmailMainFragment;
import com.tenneco.tennecoapp.Employee.EmployeeFragment;
import com.tenneco.tennecoapp.Lines.AddEditLineActivity;
import com.tenneco.tennecoapp.Main.MainFragment;
import com.tenneco.tennecoapp.Plants.PlantsActivity;
import com.tenneco.tennecoapp.Splash.SplashFragment;
import com.tenneco.tennecoapp.User.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private FirebaseAuth mAuth;
    public FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.footer_menu) LinearLayout mMenu;
    @BindView(R.id.tv_production) ImageView mTvProduction;
    @BindView(R.id.tv_users) ImageView mTvUsers;
    @BindView(R.id.tv_configuration) ImageView mTvConfiguration;
    @BindView(R.id.tv_email) ImageView mTvEmail;

    @OnClick(R.id.tv_production) void production(){
        launchProduction();
    }

    @OnClick(R.id.tv_users) void users(){
        launchUsers();
    }

    @OnClick(R.id.tv_configuration) void configuration(){
        launchConfiguration();
    }

    @OnClick(R.id.tv_email) void  email(){launchEmail();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");
        mAuth.useAppLanguage();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    clearBackStack();
                    launchMain();
                } else {
                    // User is signed out
                    clearBackStack();
                    launchSplash();
                    hideMenu();
                }

                invalidateOptionsMenu();
            }
        };


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_signout);

        if (mUser!=null){
           item.setVisible(true);
        }
            else
        {
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_signout)
            signOut();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser==null)
            launchSplash();
        else {
            launchMain();
            logUser();
        }

        mAuth.addAuthStateListener(mAuthListener);

    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(mUser.getUid());
        if (mUser.getEmail()!=null)
            Crashlytics.setUserEmail(mUser.getEmail());
        if (mUser.getDisplayName()!=null)
            Crashlytics.setUserName(mUser.getDisplayName());
    }




    @Override
    protected void onPause() {
        super.onPause();
        restoreButtons();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public void launchSplash() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SplashFragment()).commit();
    }

    @Override
    public void launchMain() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();

    }

    @Override
    public void launchProduction() {
       launchMain();
    }

    @Override
    public void launchUsers() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MenuConfigFragment()).commit();

    }

    @Override
    public void launchConfiguration() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ConfigurationFragment()).commit();

    }

    @Override
    public void launchEmail() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmailMainFragment()).commit();
    }

    @Override
    public void showMenu() {
        mMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMenu() {
        mMenu.setVisibility(View.GONE);
    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }


    @Override
    public void restoreButtons() {
        mTvConfiguration.setEnabled(true);
        mTvConfiguration.setBackground(getResources().getDrawable(R.drawable.config_icon));
        mTvUsers.setEnabled(true);
        mTvUsers.setBackground(getResources().getDrawable(R.drawable.user_icon));
        mTvProduction.setEnabled(true);
        mTvProduction.setBackground(getResources().getDrawable(R.drawable.home_icon));
        mTvEmail.setEnabled(true);
        mTvEmail.setBackground(getResources().getDrawable(R.drawable.email_icon));

    }

    @Override
    public void setProduction() {
        mTvProduction.setBackground(getResources().getDrawable(R.drawable.home_blue_icon));
        mTvProduction.setEnabled(false);
    }

    @Override
    public void setUsers() {
        mTvUsers.setBackground(getResources().getDrawable(R.drawable.user_blue_icon));
        mTvUsers.setEnabled(false);
    }

    @Override
    public void setConfiguration() {
        mTvConfiguration.setBackground(getResources().getDrawable(R.drawable.config_blue_icon));
        mTvConfiguration.setEnabled(false);
    }

    @Override
    public void setEmail() {
        mTvEmail.setBackground(getResources().getDrawable(R.drawable.email_blue_icon));
        mTvEmail.setEnabled(false);
    }
}
