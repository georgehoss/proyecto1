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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tenneco.tennecoapp.Configuration.ConfigurationFragment;
import com.tenneco.tennecoapp.ConfigurationMenu.MenuConfigFragment;
import com.tenneco.tennecoapp.Emails.EmailFragment;
import com.tenneco.tennecoapp.Employee.EmployeeFragment;
import com.tenneco.tennecoapp.Lines.AddEditLineActivity;
import com.tenneco.tennecoapp.Main.MainFragment;
import com.tenneco.tennecoapp.Splash.SplashFragment;
import com.tenneco.tennecoapp.User.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private FirebaseAuth mAuth;
    public FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.footer_menu) LinearLayout mMenu;
    @BindView(R.id.tv_production) TextView mTvProduction;
    @BindView(R.id.tv_users) TextView mTvUsers;
    @BindView(R.id.tv_configuration) TextView mTvConfiguration;
    @BindView(R.id.tv_email) TextView mTvEmail;

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
        }

        mAuth.addAuthStateListener(mAuthListener);

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
        mTvProduction.setText(R.string.bold_production);

    }

    @Override
    public void launchProduction() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmployeeFragment()).commit();
        restoreButtons();
        setProduction();
    }

    @Override
    public void launchUsers() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserFragment()).commit();
        restoreButtons();
        setUsers();
    }

    @Override
    public void launchConfiguration() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ConfigurationFragment()).commit();
        restoreButtons();
        setConfiguration();
    }

    @Override
    public void launchEmail() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmailFragment()).commit();
        restoreButtons();
        setEmail();
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
        mTvProduction.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTvUsers.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTvConfiguration.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTvEmail.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTvConfiguration.setEnabled(true);
        mTvUsers.setEnabled(true);
        mTvProduction.setEnabled(true);
        mTvEmail.setEnabled(true);
    }

    @Override
    public void setProduction() {
        mTvProduction.setText(getString(R.string.bold_production));
        mTvProduction.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTvProduction.setEnabled(false);
    }

    @Override
    public void setUsers() {
        mTvUsers.setText("Operators");
        mTvUsers.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTvUsers.setEnabled(false);
    }

    @Override
    public void setConfiguration() {
        mTvConfiguration.setText(getString(R.string.bold_configuration));
        mTvConfiguration.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTvConfiguration.setEnabled(false);
    }

    @Override
    public void setEmail() {
        mTvEmail.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTvEmail.setEnabled(false);
    }
}


/*
Lista de Operadores
Operador | Turno 1  Turno 2 Turno 3 |

Producion

Llenar informacion del turno. Y al completar el turno.

Al finalizar el downtime se agrega a los comentarios de la hora.

Scrap Event: Grupos de correos listas a partir de 1 o 2 o 3 eventos

Leak failured fix counter

**** Nuevas  *****

Moodificar target. con contraseña.
Inicio y fin de turno. Horas de trabajo





 */