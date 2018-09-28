package com.tenneco.tennecoapp.Emails;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.EmailAdapter;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EmailFragment extends Fragment implements EmailContract.View, EmailAdapter.OnEmailInteraction {
    private DatabaseReference dbEmails;
    private EmailContract.Preseneter mPresenter;
    private ArrayList<Email> mEmails;
    private EmailAdapter mAdapter;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;
    @BindView(R.id.rv_emails) RecyclerView mRvEmails;

    @OnClick(R.id.fb_add) void add(){
        addEditDialog(new Email(dbEmails.push().getKey()),getActivity());
    }

    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_email, container, false);
        ButterKnife.bind(this,view);
        dbEmails = FirebaseDatabase.getInstance().getReference(Email.DB);
        mEmails = new ArrayList<>();
        mRvEmails.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EmailAdapter(mEmails,this);
        mRvEmails.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEmails();
        showFloatingButton();
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
    public void getEmails() {
        dbEmails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEmails = new ArrayList<>();
                hideProgressBar();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Email email = itemSnapshot.getValue(Email.class);
                    if (email!=null)
                        mEmails.add(email);
                }
                Collections.sort(mEmails,Email.EmailNameComparator);
                mAdapter.setEmails(mEmails);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void addEditDialog(final Email email, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_email, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(email.getName());
        final EditText mEtName = view.findViewById(R.id.et_name);
        mEtName.setText(email.getName());
        final EditText mEtInfo = view.findViewById(R.id.et_info);
        mEtInfo.setText(email.getEmail());
        Button mBtSave = (Button) view.findViewById(R.id.bt_save);
        Button mBtCancel = (Button) view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mEtName.getText().toString().length()>0)) {
                    mEtName.setError("Introduce Name!");
                    mEtName.requestFocus();
                }
                else
                if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(mEtInfo.getText().toString().trim()).matches()))
                {
                    mEtInfo.setError(getString(R.string.login_email_invalid));
                    mEtInfo.requestFocus();
                }
                else
                {
                    email.setName(mEtName.getText().toString());
                    email.setEmail(mEtInfo.getText().toString());
                    addEditEmployee(email);
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
    public void editDeleteDialog(final Email email) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setItems(R.array.edit_employee, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (position!=0) {
                    dialogInterface.dismiss();
                    deleteDialog(email);
                }
                else{
                    dialogInterface.dismiss();
                    addEditDialog(email,getContext());
                }
            }
        });
        alertDialogBuilder.create().show();
    }

    @Override
    public void delete(String id) {
        dbEmails.child(id).removeValue();
    }

    @Override
    public void addEditEmployee(Email email) {
        dbEmails.child(email.getId()).setValue(email);
    }

    @Override
    public void deleteDialog(final Email email) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete Email");
        alertDialogBuilder.setMessage("Do you want to delete the email: "+email.getEmail()+" ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                delete(email.getId());
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
    public void bindPresenter(EmailContract.Preseneter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void EditEmail(Email email) {
        editDeleteDialog(email);
    }
}
