package com.tenneco.tennecoapp.Emails;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.EmailListAdapter;
import com.tenneco.tennecoapp.Downtime.EditCreateDowntimeFragment;
import com.tenneco.tennecoapp.Emails.List.AddEmailListFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailListFragment extends Fragment implements EmailListAdapter.OnItemClick {
    private MainActivity main;
    private EmailListAdapter mAdapter;
    private DatabaseReference dbEmailList;
    private ArrayList<EmailList> emailLists;
    private ProgressDialog progressDialog;

    @BindView(R.id.rv_email_list)
    RecyclerView mRv;
    @BindView(R.id.pb_loading)
    ProgressBar mPb;
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mPb.setVisibility(View.GONE);
            List<EmailList> list = new ArrayList<EmailList>();
            for (DataSnapshot child: dataSnapshot.getChildren()) {
                list.add(child.getValue(EmailList.class));
            }
            emailLists = new ArrayList<>();
            emailLists.addAll(list);
            Collections.sort(emailLists,EmailList.NameComparator);
            mAdapter.setEmails(emailLists);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            mPb.setVisibility(View.GONE);
        }
    };


    @OnClick(R.id.fb_add) void addDowntime(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddEmailListFragment()).addToBackStack(null).commit();
    }

    public EmailListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_email_list, container, false);
        ButterKnife.bind(this,view);
        dbEmailList =FirebaseDatabase.getInstance().getReference(EmailList.DB).child(StorageUtils.getPlantId(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        emailLists = new ArrayList<>();
        initAdapters();
        getEmails();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");
    }

    public void initAdapters(){
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EmailListAdapter(emailLists,this);
        mRv.setAdapter(mAdapter);

    }



    public void getEmails() {
        dbEmailList.addValueEventListener(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        dbEmailList.removeEventListener(valueEventListener);
    }

    @Override
    public void emailClick(EmailList email) {
        if (email!=null) {
            AddEmailListFragment fragment = AddEmailListFragment.newInstance(email.getId());
            main.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDelete(EmailList email) {
        showDeleteDialog(getContext(),email);
    }

    public void showDeleteDialog(Context context, final EmailList email) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Downtime List");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+email.getName()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(email.getId());
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();

    }

    public void delete(String id) {
        progressDialog.show();
        dbEmailList.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();

            }
        });
    }

}
