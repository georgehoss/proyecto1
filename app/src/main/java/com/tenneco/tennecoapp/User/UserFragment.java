package com.tenneco.tennecoapp.User;


import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.UserAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends Fragment implements UserContract, UserAdapter.OnUserInteraction {

    private DatabaseReference dbUsers;
    private ArrayList<User> mUsers;
    private UserAdapter mAdapter;
    private boolean leads = false;
    private boolean gt= false;
    private ValueEventListener valueEventListener = new ValueEventListener() {
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
    };

    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;
    @BindView(R.id.rv_user) RecyclerView mRvUsers;
    @BindView(R.id.tv_title) TextView mTvTitle;


    @OnClick(R.id.fb_add) void add(){
        addEditDialog(new User(dbUsers.push().getKey(),"","",0),getActivity());
    }
    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String db) {

        Bundle args = new Bundle();
        args.putString("db",db);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this,view);
        if (getArguments()!=null &&  getArguments().getString("db")!=null) {
            if (Objects.requireNonNull(getArguments().getString("db")).equals(User.DB_GROUP)) {
                setTitle("Group Leads");
                gt = true;
                dbUsers = FirebaseDatabase.getInstance().getReference(User.DB_GROUP).child(StorageUtils.getPlantId(getContext()));
            }
            else {
                setTitle("Team Leads");
                gt = true;
                dbUsers = FirebaseDatabase.getInstance().getReference(User.DB_TEAM).child(StorageUtils.getPlantId(getContext()));
            }

            showFloatingButton();
            leads=true;
        }
        else
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

    @SuppressLint("RestrictedApi")
    @Override
    public void showFloatingButton() {
        mFbAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void getUsers() {
        dbUsers.addValueEventListener(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        dbUsers.removeEventListener(valueEventListener);
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
        ImageView imageView = view.findViewById(R.id.iv_icon);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        final Spinner spinner = view.findViewById(R.id.sp_user);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.edit_user, R.layout.spinner_row);
        spinner.setAdapter(adapter);
        if (user.getType()!=0)
        spinner.setSelection(user.getType()-1);
        final EditText etPsw = view.findViewById(R.id.et_psw);
        if (gt) {
            view.findViewById(R.id.ll_psw).setVisibility(View.VISIBLE);
            if (user.getPwd()!=null)
            etPsw.setText(user.getPwd());
        }



        switch (user.getType())
        {
            default:
                imageView.setBackground(getResources().getDrawable(R.drawable.supervisor_icon,null));
                break;
            case 1:
                imageView.setBackground(getResources().getDrawable(R.drawable.employee_icon,null));
                break;
            case 3:
                imageView.setBackground(getResources().getDrawable(R.drawable.admin_icon,null));
                break;

        }

        if (leads) {
            spinner.setSelection(1);
            TextView textView = view.findViewById(R.id.tv_type);
            textView.setVisibility(View.GONE);
            LinearLayout linearLayout = view.findViewById(R.id.ll_spinner);
            linearLayout.setVisibility(View.GONE);
        }

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

                    if (gt)
                        user.setPwd(etPsw.getText().toString());
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
        if (user.getId()==null || user.getId().isEmpty())
            user.setId(dbUsers.push().getKey());
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
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    @Override
    public void EditUser(User user) {
        editDeleteDialog(user);
    }
}
