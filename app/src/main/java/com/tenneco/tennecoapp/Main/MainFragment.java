package com.tenneco.tennecoapp.Main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends Fragment {

    private FirebaseUser mUser;
    private MainActivity main;
    private DatabaseReference dbUsers;


    private ValueEventListener itemListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            if (user==null)
            {
                saveUser(mUser);
            }
            else
            {
                if (user.getType()==3)
                    main.showMenu();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


   /* @OnClick (R.id.tv_email) void onVerify(){
        if (!mUser.isEmailVerified())
            mUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Correo de verificación enviado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }*/


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        dbUsers = FirebaseDatabase.getInstance().getReference(User.DB_USER);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (main!=null) {
            mUser = main.mUser;
            if (mUser != null) {
                Query postsQuery;
                postsQuery = dbUsers.child(mUser.getUid());
                postsQuery.addListenerForSingleValueEvent(itemListener);
            }
        }


    }

    private void saveUser(FirebaseUser user){
        User mUser = new User(user.getUid(),user.getDisplayName(),user.getEmail(),0);
        dbUsers.child(mUser.getId()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
