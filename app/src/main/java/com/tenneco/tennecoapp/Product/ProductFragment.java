package com.tenneco.tennecoapp.Product;


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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.ProductAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
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
public class ProductFragment extends Fragment implements ProductContract.View, ProductAdapter.OnItemClick {
    private ProductContract.Presenter mPresenter;
    private ProductAdapter mAdapter;
    private DatabaseReference dbProducts;
    private ArrayList<Product> mProducts;
    @BindView(R.id.pb_loading)
    ProgressBar mPb;
    @BindView(R.id.rv_products)
    RecyclerView mRvProducts;
    @BindView(R.id.fb_add)
    FloatingActionButton mFb;
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mProducts = new ArrayList<>();
            hideProgressBar();
            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
            {
                Product product = itemSnapshot.getValue(Product.class);
                if (product!=null)
                    mProducts.add(product);
            }
            Collections.sort(mProducts,Product.NameComparator);
            mAdapter.setProducts(mProducts);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            hideProgressBar();
        }
    };

    public ProductFragment() {
        // Required empty public constructor
    }

    @OnClick (R.id.fb_add) void showDialog() {
        addEditDialog(null,getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ButterKnife.bind(this,view);
        dbProducts =  FirebaseDatabase.getInstance().getReference(Product.DB).child(StorageUtils.getPlantId(getContext()));
        mProducts = new ArrayList<>();
        mRvProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProductAdapter(mProducts,this);
        mRvProducts.setAdapter(mAdapter);
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getProducts();
        showFloatingButton();
        MainActivity main = (MainActivity) getActivity();
        if (main!=null) {
           main.hideMenu();
        }
    }

    @Override
    public void hideProgressBar() {
        mPb.setVisibility(View.GONE);
    }

    @Override
    public void showFloatingButton() {
        mFb.setVisibility(View.VISIBLE);
    }

    @Override
    public void getProducts() {
        dbProducts.addValueEventListener(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        dbProducts.removeEventListener(valueEventListener);
    }

    @Override
    public void addEditDialog(Product product, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_product, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEvName = view.findViewById(R.id.et_name);
        final EditText mEvCode = view.findViewById(R.id.et_code);
        final EditText mEvDescription = view.findViewById(R.id.et_description);
        Button btSave = view.findViewById(R.id.bt_save);
        Button btDelete = view.findViewById(R.id.bt_delete);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        String id="";
        if (product==null){
            id = dbProducts.push().getKey();
            product = setShift(id);
        }
        else
        {
            id = product.getId();
            mEvName.setText(product.getName());
            mEvCode.setText(product.getCode());
            mEvDescription.setText(product.getDescription());
            product = setShift(id);
            btDelete.setVisibility(View.VISIBLE);
        }

        final String finalId = id;
        final Product finalProduct = product;
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce Product Name!");
                    mEvName.requestFocus();
                }
                else
                if (mEvCode.getText().toString().isEmpty()) {
                    mEvCode.setError("Introduce Product Code!");
                    mEvCode.requestFocus();
                }
                else {
                    finalProduct.setName(mEvName.getText().toString());
                    finalProduct.setCode(mEvCode.getText().toString());
                    finalProduct.setDescription(mEvDescription.getText().toString());
                    saveData(finalProduct);
                    dialog.dismiss();
                    mPb.setVisibility(View.VISIBLE);
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finalProduct.setName(mEvName.getText().toString());
                finalProduct.setCode(mEvCode.getText().toString());
                finalProduct.setDescription(mEvDescription.getText().toString());
                editDeleteDialog(finalProduct);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void editDeleteDialog(final Product product) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete Email");
        alertDialogBuilder.setMessage("Do you want to delete the product: "+product.getName()+" ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                delete(product.getId());
                mPb.setVisibility(View.VISIBLE);
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
    public void delete(String id) {
        dbProducts.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void saveData(Product product) {
        dbProducts.child(product.getId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void bindPresenter(ProductContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void productClick(Product product) {
        addEditDialog(product,getContext());
    }

    @Override
    public void onDelete(Product product) {

    }

    private Product setShift(String id){
        Product product = new Product();
        product.setId(id);
        ArrayList<WorkHour> hours1 = new ArrayList<>();
        Context context = getContext();
        hours1.add(new WorkHour(context.getString(R.string.add_06_30),context.getString(R.string.add_07_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_29)));
        hours1.add(new WorkHour(context.getString(R.string.add_07_30),context.getString(R.string.add_08_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_68)));
        hours1.add(new WorkHour(context.getString(R.string.add_08_30),context.getString(R.string.add_09_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_97)));
        hours1.add(new WorkHour(context.getString(R.string.add_09_30),context.getString(R.string.add_10_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_136)));
        hours1.add(new WorkHour(context.getString(R.string.add_10_30),context.getString(R.string.add_11_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_175)));
        hours1.add(new WorkHour(context.getString(R.string.add_11_30),context.getString(R.string.add_12_30),context.getString(R.string.add_t_20),context.getString(R.string.add_t_195)));
        hours1.add(new WorkHour(context.getString(R.string.add_12_30),context.getString(R.string.add_01_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_234)));
        hours1.add(new WorkHour(context.getString(R.string.add_01_30),context.getString(R.string.add_02_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_273)));
        ArrayList<WorkHour> hours2 = new ArrayList<>();
        hours2.add(new WorkHour(context.getString(R.string.add_02_30),context.getString(R.string.add_03_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_29)));
        hours2.add(new WorkHour(context.getString(R.string.add_03_30),context.getString(R.string.add_04_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_68)));
        hours2.add(new WorkHour(context.getString(R.string.add_04_30),context.getString(R.string.add_05_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_97)));
        hours2.add(new WorkHour(context.getString(R.string.add_05_30),context.getString(R.string.add_06_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_136)));
        hours2.add(new WorkHour(context.getString(R.string.add_06_30),context.getString(R.string.add_07_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_175)));
        hours2.add(new WorkHour(context.getString(R.string.add_07_30),context.getString(R.string.add_08_30),context.getString(R.string.add_t_20),context.getString(R.string.add_t_195)));
        hours2.add(new WorkHour(context.getString(R.string.add_08_30),context.getString(R.string.add_09_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_234)));
        hours2.add(new WorkHour(context.getString(R.string.add_09_30),context.getString(R.string.add_10_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_273)));
        ArrayList<WorkHour> hours3 = new ArrayList<>();
        hours3.add(new WorkHour(context.getString(R.string.add_10_30),context.getString(R.string.add_11_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_29)));
        hours3.add(new WorkHour(context.getString(R.string.add_11_30),context.getString(R.string.add_12_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_68)));
        hours3.add(new WorkHour(context.getString(R.string.add_12_30),context.getString(R.string.add_01_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_97)));
        hours3.add(new WorkHour(context.getString(R.string.add_01_30),context.getString(R.string.add_02_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_136)));
        hours3.add(new WorkHour(context.getString(R.string.add_02_30),context.getString(R.string.add_03_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_175)));
        hours3.add(new WorkHour(context.getString(R.string.add_03_30),context.getString(R.string.add_04_30),context.getString(R.string.add_t_20),context.getString(R.string.add_t_195)));
        hours3.add(new WorkHour(context.getString(R.string.add_04_30),context.getString(R.string.add_05_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_234)));
        hours3.add(new WorkHour(context.getString(R.string.add_05_30),context.getString(R.string.add_06_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_273)));
        product.setFirst(new Shift(hours1,context.getString(R.string.add_1st_shift),context.getString(R.string.add_t_273)));
        product.setSecond(new Shift(hours2,context.getString(R.string.add_2nd_shift),context.getString(R.string.add_t_273)));
        product.setThird(new Shift(hours3,context.getString(R.string.add_3rd_shift),context.getString(R.string.add_t_273)));
        return product;
    }
}
