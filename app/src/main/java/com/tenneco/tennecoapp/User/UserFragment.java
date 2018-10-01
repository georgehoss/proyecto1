package com.tenneco.tennecoapp.User;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.UserAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends Fragment implements UserContract, UserAdapter.OnUserInteraction {

    private DatabaseReference dbUsers;
    private ArrayList<User> mUsers;
    private UserAdapter mAdapter;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;
    @BindView(R.id.rv_user) RecyclerView mRvUsers;

    @OnClick(R.id.fb_add) void add(){
        addEditDialog(new User(dbUsers.push().getKey(),"","",0),getActivity());
    }
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this,view);
        dbUsers = FirebaseDatabase.getInstance().getReference(User.DB_USER);
        mUsers = new ArrayList<>();
        mRvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new UserAdapter(mUsers,this,getActivity());
        mRvUsers.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUsers();
        MainActivity main = (MainActivity) getActivity();
        if (main!=null) {
            main.hideMenu();
        }
        //showFloatingButton();
    }

    @Override
    public void hideProgressBar() {
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showFloatingButton() {
        mFbAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void getUsers() {
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers = new ArrayList<>();
                hideProgressBar();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    User user = itemSnapshot.getValue(User.class);
                    if (user!=null)
                        mUsers.add(user);
                }
                Collections.sort(mUsers,User.UserNameComparator);
                mAdapter.setUsers(mUsers);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void addEditDialog(final User user, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_user, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(user.getName());
        final EditText mEtName = view.findViewById(R.id.et_name);
        mEtName.setText(user.getName());
        final EditText mEtInfo = view.findViewById(R.id.et_info);
        mEtInfo.setText(user.getEmail());
        Button mBtSave = (Button) view.findViewById(R.id.bt_save);
        Button mBtCancel = (Button) view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        final Spinner spinner = view.findViewById(R.id.sp_user);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.edit_user, R.layout.spinner_row);
        spinner.setAdapter(adapter);
        if (user.getType()!=0)
        spinner.setSelection(user.getType()-1);

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mEtName.getText().toString().length()>0)) {
                    mEtName.setError("Introduce Name!");
                    mEtName.requestFocus();
                }
                else
                if (!(Patterns.EMAIL_ADDRESS.matcher(mEtInfo.getText().toString().trim()).matches()))
                {
                    mEtInfo.setError(getString(R.string.login_email_invalid));
                    mEtInfo.requestFocus();
                }
                else
                {
                    user.setName(mEtName.getText().toString());
                    user.setEmail(mEtInfo.getText().toString());
                    user.setType(spinner.getSelectedItemPosition()+1);
                    addEditEmployee(user);
                    dialog.dismiss();
                }

            }
        });

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void editDeleteDialog(final User user) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setItems(R.array.edit_employee, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (position!=0) {
                    dialogInterface.dismiss();
                    deleteDialog(user);
                }
                else{
                    dialogInterface.dismiss();
                    addEditDialog(user,getContext());
                }
            }
        });
        alertDialogBuilder.create().show();
    }

    @Override
    public void delete(String id) {
        dbUsers.child(id).removeValue();
    }

    @Override
    public void addEditEmployee(User user) {
        dbUsers.child(user.getId()).setValue(user);
    }

    @Override
    public void deleteDialog(final User user) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete User");
        alertDialogBuilder.setMessage("Do you want to delete the user: "+user.getName()+" ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                delete(user.getId());
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    @Override
    public void EditUser(User user) {
        editDeleteDialog(user);
    }
}
