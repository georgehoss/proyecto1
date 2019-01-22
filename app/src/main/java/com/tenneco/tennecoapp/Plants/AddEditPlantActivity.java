package com.tenneco.tennecoapp.Plants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditPlantActivity extends AppCompatActivity implements AddEditPlantContract.View {
    private AddEditPlantContract.Presenter mPresenter;
    private DatabaseReference dbPlants;
    private String id;
    private ProgressDialog progressDialog;
    private boolean deletable = false;
    private Plant mPlant;
    private Query postsQuery;
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mPlant = dataSnapshot.getValue(Plant.class);
            if (mPlant!=null) {
                setData(mPlant);
                deletable = true;
                invalidateOptionsMenu();
            }
            else
                finish();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            finish();
        }
    };

    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.et_code) EditText mEtCode;
    @BindView(R.id.et_address) EditText mEtAddress;
    @BindView(R.id.et_psw) EditText mEtPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_plant);
        ButterKnife.bind(this);
        dbPlants = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS);
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("id")!=null)
        {
            id = getIntent().getExtras().getString("id");
            getData();
        }
        else
        if (getIntent().getExtras()==null){
            mPlant = new Plant();
            id = dbPlants.push().getKey();
            mPlant.setId(id);
        }
        else
            finish();


        if (mPresenter == null)
            mPresenter = new AddEditPlantPresenter(this) {
            };
        else
            mPresenter.bindView(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
        postsQuery.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    public void onBackPressed() {
        showExitDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        MenuItem item = menu.findItem(R.id.menu_delete);

        if (deletable){
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
        if (item.getItemId() == R.id.menu_save)
        {
            mPlant.setName(mEtName.getText().toString().trim());
            mPlant.setCode(mEtCode.getText().toString().trim());
            mPlant.setAddress(mEtAddress.getText().toString().trim());
            mPlant.setPsw( mEtPsw.getText().toString().trim());
            mPresenter.saveChanges(mPlant);
        }

        if (item.getItemId() == R.id.menu_delete)
        {
            showDeleteDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData() {

        postsQuery = dbPlants.child(id);
        postsQuery.addValueEventListener(valueEventListener);
    }



    @Override
    public void setData(Plant plant) {
        if (plant.getName()!= null)
            mEtName.setText(plant.getName());
        if (plant.getCode()!= null)
            mEtCode.setText(plant.getCode());
        if (plant.getAddress()!= null)
            mEtAddress.setText(plant.getAddress());
        if (plant.getPsw()!= null)
            mEtPsw.setText(plant.getPsw());
    }

    public void showExitDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Save Changes");
        alertDialogBuilder.setMessage("Do you want to save the changes of the Plant:"+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                mPlant.setName(mEtName.getText().toString().trim());
                mPlant.setCode(mEtCode.getText().toString().trim());
                mPlant.setAddress(mEtAddress.getText().toString().trim());
                mPlant.setPsw( mEtPsw.getText().toString().trim());
                mPresenter.saveChanges(mPlant);
           /*     if (mPresenter.validName(mEtName.getText().toString().trim()))
                    mPresenter.saveChanges(mEtName.getText().toString().trim(),id,shift1,shift2,shift3, mPositions,downtime,mReasons,mLine,mEmployees,mEmails
                            , mEtPsw.getText().toString().trim());
                else
                    showNameError();*/
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
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

    public void showDeleteDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Plant");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
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

    @Override
    public void showNameError() {
        mEtName.setError("Introduce "+getString(R.string.introduce_plant_name));
        mEtName.requestFocus();
    }

    @Override
    public void showCodeError() {
        mEtCode.setError("Introduce "+getString(R.string.introduce_plant_code));
        mEtCode.requestFocus();
    }

    public void delete() {
        progressDialog.show();
        dbPlants.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
                finish();

            }
        });
    }

    @Override
    public void savePlant(Plant plant) {
        progressDialog.show();
        dbPlants.child(plant.getId()).child("id").setValue(plant.getId());
        dbPlants.child(plant.getId()).child("name").setValue(plant.getName());
        dbPlants.child(plant.getId()).child("psw").setValue(plant.getPsw());
        dbPlants.child(plant.getId()).child("address").setValue(plant.getAddress());
        dbPlants.child(plant.getId()).child("code").setValue(plant.getCode()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
            }
        });
    }

    @Override
    public void bindPresenter(AddEditPlantContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
