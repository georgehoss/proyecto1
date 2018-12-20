package com.tenneco.tennecoapp.Emails.List;


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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DowntimeAdapter;
import com.tenneco.tennecoapp.Adapter.EmailAdapter;
import com.tenneco.tennecoapp.Adapter.EmailSelectionAdapter;
import com.tenneco.tennecoapp.Emails.EmailContract;
import com.tenneco.tennecoapp.Emails.EmailFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEmailListFragment extends Fragment implements AddEmailListContract.View {
    private AddEmailListContract.Presenter mPresenter;
    private DatabaseReference dbEmailList;
    private DatabaseReference dbEmails;
    private MainActivity main;
    private EmailSelectionAdapter mAdapter;
    private EmailList emailList;
    private String id;
    private ProgressDialog progressDialog;
    private ArrayList<Email> mEmails;
    @BindView(R.id.rv_downtime) RecyclerView mRvDw;
    @BindView(R.id.et_name)
    EditText mEtName;

    public AddEmailListFragment() {
        // Required empty public constructor
    }

    public static AddEmailListFragment newInstance(String id) {

        Bundle args = new Bundle();

        AddEmailListFragment fragment = new AddEmailListFragment();
        args.putString("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.fb_add) void saveDw(){
        emailList.setEmails(mAdapter.getEmails());
        mPresenter.saveChanges(mEtName.getText().toString(),emailList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_email_list, container, false);
        ButterKnife.bind(this,view);
        dbEmailList = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(EmailList.DB);
        dbEmails = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(Email.DB);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (getArguments()!=null && getArguments().getString("id")!=null)
        {
            id = getArguments().getString("id");
            getData();
        }
        else {

            id = dbEmailList.push().getKey();
            emailList = mPresenter.initData(getContext(),id);
            initAdapters();
        }

        getEmails();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");

    }

    @Override
    public void hideProgressBar() {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    public void getData() {
        Query postsQuery;
        postsQuery = dbEmailList.child(id);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailList = dataSnapshot.getValue(EmailList.class);
                if (emailList!=null) {

                    mEtName.setText(emailList.getName());
                    initAdapters();
                }
                else
                if (getFragmentManager()!=null)
                    getFragmentManager().popBackStack();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (getFragmentManager()!=null)
                    getFragmentManager().popBackStack();
            }
        });

    }

    @Override
    public void saveData(EmailList emailList) {
        progressDialog.show();
        dbEmailList.child(emailList.getId()).setValue(emailList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (getFragmentManager()!=null)
                    getFragmentManager().popBackStack();
                hideProgressBar();
            }
        });
    }

    @Override
    public void initAdapters() {
        mRvDw.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EmailSelectionAdapter(emailList.getEmails());
        mRvDw.setAdapter(mAdapter);
    }

    @Override
    public void showAddEmailDialog(Context context, ArrayList<Email> emails) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter == null)
            mPresenter = new AddEmailListPresenter(this);
        else
            mPresenter.bindView(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showNameEmptyError() {
        mEtName.setError("Please introduce List Name!");
        mEtName.requestFocus();
    }

    @Override
    public void showExitDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Save Changes");
        alertDialogBuilder.setMessage("Do you want to save the changes of the list "+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                emailList.setEmails(mAdapter.getEmails());
                mPresenter.saveChanges(mEtName.getText().toString(),emailList);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getFragmentManager().popBackStack();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();
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
                Collections.sort(mEmails,Email.NameComparator);
                emailList.setEmails(mPresenter.setSelected(mEmails,emailList.getEmails()));
                mAdapter.setEmails(emailList.getEmails());
                mAdapter.notifyDataSetChanged();
                if (mEmails.size()==0)
                    showEmailsError();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void showEmailsError() {
        Toast.makeText(main, "You must add at least one (1) Email!", Toast.LENGTH_LONG).show();
        launchEmails();
    }

    @Override
    public void showSelectionError() {
        Toast.makeText(main, "You must select at least one (1) Email!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void launchEmails() {
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmailFragment()).addToBackStack(null).commit();
    }

    @Override
    public void bindPresenter(AddEmailListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
