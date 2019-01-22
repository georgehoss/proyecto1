package com.tenneco.tennecoapp.Lines.Product;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tenneco.tennecoapp.Adapter.HourAdapter;
import com.tenneco.tennecoapp.Adapter.ProductAdapter;
import com.tenneco.tennecoapp.Lines.ConfigLineActivity;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LineProductFragment extends Fragment implements LineProductContract.View, HourAdapter.ItemInteraction, ProductAdapter.OnItemClick {
    private LineProductContract.Presenter mPresenter;
    private HourAdapter mAdapter1;
    private HourAdapter mAdapter2;
    private HourAdapter mAdapter3;
    private ArrayAdapter<Product> mAdapter;
    private Product mProduct;

    private ConfigLineActivity main;
    @BindView(R.id.ll_shift1) LinearLayout mLlS1;
    @BindView(R.id.ll_shift2) LinearLayout mLlS2;
    @BindView(R.id.ll_shift3) LinearLayout mLlS3;
    @BindView(R.id.rv_shift1) RecyclerView mRvS1;
    @BindView(R.id.rv_shift2) RecyclerView mRvS2;
    @BindView(R.id.rv_shift3) RecyclerView mRvS3;
    @BindView(R.id.cv_s1) CardView mCvS1;
    @BindView(R.id.cv_s2) CardView mCvS2;
    @BindView(R.id.cv_s3) CardView mCvS3;
    @BindView(R.id.iv_delete) ImageView mBtDelete;
    @BindView(R.id.sp_product) Spinner spProducts;
    private AlertDialog dialog;

    @OnClick(R.id.bt_add) void showDialog(){
        showProductListDialog(main.mProducts);
    }

    @OnClick(R.id.iv_delete) void onDelete(){
        if (mProduct!=null) {
            main.mLine.getProducts().remove(mProduct);
            updateLine();
            hideshift1();
            hideshift2();
            hideshift3();
            hideDelete();
        }

    }


    public LineProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_product, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity()!=null)
            main = (ConfigLineActivity) getActivity();
        initAdapters();
        updateLine();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter==null)
            mPresenter = new LineProductPresenter(this);
        else
            mPresenter.bindView(this);
    }

    @Override
    public void initAdapters() {
        mRvS1.setLayoutManager(new LinearLayoutManager(main));
        mAdapter1 = new HourAdapter(new ArrayList<WorkHour>(),this,1);
        mRvS1.setAdapter(mAdapter1);
        mRvS2.setLayoutManager(new LinearLayoutManager(main));
        mAdapter2 = new HourAdapter(new ArrayList<WorkHour>(),this,2);
        mRvS2.setAdapter(mAdapter2);
        mRvS3.setLayoutManager(new LinearLayoutManager(main));
        mAdapter3 = new HourAdapter(new ArrayList<WorkHour>(),this,3);
        mRvS3.setAdapter(mAdapter3);
    }

    @Override
    public void updateLine() {
        if (main!=null && main.mLine!=null && main.mLine.getProducts()!=null) {
            mAdapter = new ArrayAdapter<>(main, R.layout.spinner_row, main.mLine.getProducts());
            spProducts.setAdapter(mAdapter);
            spProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mProduct = (Product) adapterView.getItemAtPosition(i);
                    if (mProduct != null) {
                        showDelete();
                        mAdapter1.setHours(mProduct.getFirst().getHours());
                        mAdapter2.setHours(mProduct.getSecond().getHours());
                        mAdapter3.setHours(mProduct.getThird().getHours());
                        mAdapter1.notifyDataSetChanged();
                        mAdapter2.notifyDataSetChanged();
                        mAdapter3.notifyDataSetChanged();
                        if (mProduct.getFirst().getHours().size()>0) showshift1();
                        else
                            hideshift1();
                        if (mProduct.getSecond().getHours().size()>0)
                            showshift2();
                        else
                            hideshift2();
                        if (mProduct.getThird().getHours().size()>0)
                            showshift3();
                        else
                            hideshift3();

                    } else {
                        hideshift1();
                        hideshift2();
                        hideshift3();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else
            hideDelete();
    }

    @Override
    public void onTargetClick(int position) {
        if (position==1)
            showShiftDialog(mProduct.getFirst(),position,main);
        else
        if (position==2)
            showShiftDialog(mProduct.getSecond(),position,main);
        else
        if (position==3)
            showShiftDialog(mProduct.getThird(),position,main);
    }

    @Override
    public void hideshift1() {
        mLlS1.setVisibility(View.GONE);
    }

    @Override
    public void showshift1() {
        mCvS1.setVisibility(View.VISIBLE);
        mLlS1.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideshift2() {
        mLlS2.setVisibility(View.GONE);
    }

    @Override
    public void showshift2() {
        mCvS2.setVisibility(View.VISIBLE);
        mLlS2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideshift3() {
        mLlS3.setVisibility(View.GONE);
    }

    @Override
    public void showshift3()
    {
        mCvS3.setVisibility(View.VISIBLE);
        mLlS3.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDelete() {
        if (getContext()!=null)
            mBtDelete.setVisibility(View.GONE);
    }

    @Override
    public void showDelete() {
        mBtDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProductListDialog(ArrayList<Product> products) {
        Context context = main;

        if (context!=null && products!=null && products.size()>=0) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            final ProductAdapter adapter = new ProductAdapter(products, this);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            alertDialogBuilder.setView(recyclerView);
            String title = "Pick a Product";
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog = alertDialogBuilder.create();
            dialog.show();
        }
        else
            if (context!=null && (products==null || products.size()==0) )
            {
                showProductsEmpty();
            }


    }

    @Override
    public void showProductsEmpty() {
        Toast.makeText(main, "You must create a Product First. \n Go back to Main menu - Configuration - Products.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void launchProducts() {
    }

    @Override
    public void showShiftDialog(Shift shift, final int shiftNumber, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.dialog_shift, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
        TextView mTvName = view.findViewById(R.id.tv_shift1);
        if (shiftNumber==1)
            mTvName.setText(getString(R.string.add_1st_shift));
        else
        if (shiftNumber==2)
            mTvName.setText(getString(R.string.add_2nd_shift));
        else
            mTvName.setText(getString(R.string.add_3rd_shift));

        final EditText mEtS1s1 = view.findViewById(R.id.et_s1_shour1);
        final EditText mEtS1e1 = view.findViewById(R.id.et_s1_ehour1);
        final EditText mEtS1t1 = view.findViewById(R.id.et_s1_t1);
        if (shift.getHours().get(0)!=null) {
            mEtS1s1.setText(shift.getHours().get(0).getStartHour());
            mEtS1e1.setText(shift.getHours().get(0).getEndHour());
            mEtS1t1.setText(shift.getHours().get(0).getTarget());
        }
        final EditText mEtS1s2 = view.findViewById(R.id.et_s1_shour2);
        final EditText mEtS1e2 = view.findViewById(R.id.et_s1_ehour2);
        final EditText mEtS1t2 = view.findViewById(R.id.et_s1_t2);
        if (shift.getHours().get(1)!=null) {
            mEtS1s2.setText(shift.getHours().get(1).getStartHour());
            mEtS1e2.setText(shift.getHours().get(1).getEndHour());
            mEtS1t2.setText(shift.getHours().get(1).getTarget());
        }
        final EditText mEtS1s3 = view.findViewById(R.id.et_s1_shour3);
        final EditText mEtS1e3 = view.findViewById(R.id.et_s1_ehour3);
        final EditText mEtS1t3 = view.findViewById(R.id.et_s1_t3);
        if (shift.getHours().get(2)!=null) {
            mEtS1s3.setText(shift.getHours().get(2).getStartHour());
            mEtS1e3.setText(shift.getHours().get(2).getEndHour());
            mEtS1t3.setText(shift.getHours().get(2).getTarget());
        }
        final EditText mEtS1s4 = view.findViewById(R.id.et_s1_shour4);
        final EditText mEtS1e4 = view.findViewById(R.id.et_s1_ehour4);
        final EditText mEtS1t4 = view.findViewById(R.id.et_s1_t4);
        if (shift.getHours().get(3)!=null) {
            mEtS1s4.setText(shift.getHours().get(3).getStartHour());
            mEtS1e4.setText(shift.getHours().get(3).getEndHour());
            mEtS1t4.setText(shift.getHours().get(3).getTarget());
        }
        final EditText mEtS1s5 = view.findViewById(R.id.et_s1_shour5);
        final EditText mEtS1e5 = view.findViewById(R.id.et_s1_ehour5);
        final EditText mEtS1t5 = view.findViewById(R.id.et_s1_t5);
        if (shift.getHours().get(4)!=null) {
            mEtS1s5.setText(shift.getHours().get(4).getStartHour());
            mEtS1e5.setText(shift.getHours().get(4).getEndHour());
            mEtS1t5.setText(shift.getHours().get(4).getTarget());
        }
        final EditText mEtS1s6 = view.findViewById(R.id.et_s1_shour6);
        final EditText mEtS1e6 = view.findViewById(R.id.et_s1_ehour6);
        final EditText mEtS1t6 = view.findViewById(R.id.et_s1_t6);
        if (shift.getHours().get(5)!=null) {
            mEtS1s6.setText(shift.getHours().get(5).getStartHour());
            mEtS1e6.setText(shift.getHours().get(5).getEndHour());
            mEtS1t6.setText(shift.getHours().get(5).getTarget());
        }
        final EditText mEtS1s7 = view.findViewById(R.id.et_s1_shour7);
        final EditText mEtS1e7 = view.findViewById(R.id.et_s1_ehour7);
        final EditText mEtS1t7 = view.findViewById(R.id.et_s1_t7);
        if (shift.getHours().get(6)!=null) {
            mEtS1s7.setText(shift.getHours().get(6).getStartHour());
            mEtS1e7.setText(shift.getHours().get(6).getEndHour());
            mEtS1t7.setText(shift.getHours().get(6).getTarget());
        }
        final EditText mEtS1s8 = view.findViewById(R.id.et_s1_shour8);
        final EditText mEtS1e8 = view.findViewById(R.id.et_s1_ehour8);
        final EditText mEtS1t8 = view.findViewById(R.id.et_s1_t8);
        if (shift.getHours().get(7)!=null) {
            mEtS1s8.setText(shift.getHours().get(7).getStartHour());
            mEtS1e8.setText(shift.getHours().get(7).getEndHour());
            mEtS1t8.setText(shift.getHours().get(7).getTarget());
        }

        Button mBtSave = view.findViewById(R.id.bt_save);
        Button mBtCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1 = mEtS1s1.getText().toString().trim();
                String e1 = mEtS1e1.getText().toString().trim();
                String t1 = mEtS1t1.getText().toString().trim();
                String s2 = mEtS1s2.getText().toString().trim();
                String e2 = mEtS1e2.getText().toString().trim();
                String t2 = mEtS1t2.getText().toString().trim();
                String s3 = mEtS1s3.getText().toString().trim();
                String e3 = mEtS1e3.getText().toString().trim();
                String t3 = mEtS1t3.getText().toString().trim();
                String s4 = mEtS1s4.getText().toString().trim();
                String e4 = mEtS1e4.getText().toString().trim();
                String t4 = mEtS1t4.getText().toString().trim();
                String s5 = mEtS1s5.getText().toString().trim();
                String e5 = mEtS1e5.getText().toString().trim();
                String t5 = mEtS1t5.getText().toString().trim();
                String s6 = mEtS1s6.getText().toString().trim();
                String e6 = mEtS1e6.getText().toString().trim();
                String t6 = mEtS1t6.getText().toString().trim();
                String s7 = mEtS1s7.getText().toString().trim();
                String e7 = mEtS1e7.getText().toString().trim();
                String t7 = mEtS1t7.getText().toString().trim();
                String s8 = mEtS1s8.getText().toString().trim();
                String e8 = mEtS1e8.getText().toString().trim();
                String t8 = mEtS1t8.getText().toString().trim();

                if (s1.isEmpty())
                {
                    mEtS1s1.setError(getString(R.string.star_hour));
                    mEtS1s1.requestFocus();
                }
                else
                if (e1.isEmpty())
                {
                    mEtS1e1.setError(getString(R.string.end_hour));
                    mEtS1e1.requestFocus();
                }
                else
                if (t1.isEmpty())
                {
                    mEtS1t1.setError(getString(R.string.error_target));
                    mEtS1t1.requestFocus();
                }
                else
                if (s2.isEmpty())
                {
                    mEtS1s2.setError(getString(R.string.star_hour));
                    mEtS1s2.requestFocus();
                }
                else
                if (e2.isEmpty())
                {
                    mEtS1e2.setError(getString(R.string.end_hour));
                    mEtS1e2.requestFocus();
                }
                else
                if (t2.isEmpty())
                {
                    mEtS1t2.setError(getString(R.string.error_target));
                    mEtS1t2.requestFocus();
                }
                else
                if (s3.isEmpty())
                {
                    mEtS1s3.setError(getString(R.string.star_hour));
                    mEtS1s3.requestFocus();
                }
                else
                if (e3.isEmpty())
                {
                    mEtS1e3.setError(getString(R.string.end_hour));
                    mEtS1e3.requestFocus();
                }
                else
                if (t3.isEmpty())
                {
                    mEtS1t3.setError(getString(R.string.error_target));
                    mEtS1t3.requestFocus();
                }
                else
                if (s4.isEmpty())
                {
                    mEtS1s4.setError(getString(R.string.star_hour));
                    mEtS1s4.requestFocus();
                }
                else
                if (e4.isEmpty())
                {
                    mEtS1e4.setError(getString(R.string.end_hour));
                    mEtS1e4.requestFocus();
                }
                else
                if (t4.isEmpty())
                {
                    mEtS1t4.setError(getString(R.string.error_target));
                    mEtS1t4.requestFocus();
                }
                else
                if (s5.isEmpty())
                {
                    mEtS1s5.setError(getString(R.string.star_hour));
                    mEtS1s5.requestFocus();
                }
                else
                if (e5.isEmpty())
                {
                    mEtS1e5.setError(getString(R.string.end_hour));
                    mEtS1e5.requestFocus();
                }
                else
                if (t5.isEmpty())
                {
                    mEtS1t5.setError(getString(R.string.error_target));
                    mEtS1t5.requestFocus();
                }
                else
                if (s6.isEmpty())
                {
                    mEtS1s6.setError(getString(R.string.star_hour));
                    mEtS1s6.requestFocus();
                }
                else
                if (e6.isEmpty())
                {
                    mEtS1e6.setError(getString(R.string.end_hour));
                    mEtS1e6.requestFocus();
                }
                else
                if (t6.isEmpty())
                {
                    mEtS1t6.setError(getString(R.string.error_target));
                    mEtS1t6.requestFocus();
                }
                else
                if (s7.isEmpty())
                {
                    mEtS1s7.setError(getString(R.string.star_hour));
                    mEtS1s7.requestFocus();
                }
                else
                if (e7.isEmpty())
                {
                    mEtS1e7.setError(getString(R.string.end_hour));
                    mEtS1e7.requestFocus();
                }
                else
                if (t7.isEmpty())
                {
                    mEtS1t7.setError(getString(R.string.error_target));
                    mEtS1t7.requestFocus();
                }
                else
                if (s8.isEmpty())
                {
                    mEtS1s8.setError(getString(R.string.star_hour));
                    mEtS1s8.requestFocus();
                }
                else
                if (e8.isEmpty())
                {
                    mEtS1e8.setError(getString(R.string.end_hour));
                    mEtS1e8.requestFocus();
                }
                else
                if (t8.isEmpty())
                {
                    mEtS1t8.setError(getString(R.string.error_target));
                    mEtS1t8.requestFocus();
                }
                else
                if (shiftNumber==1)
                {
                    mProduct.setFirst(mPresenter.getshift(
                            s1,e1,t1,
                            s2,e2,t2,
                            s3,e3,t3,
                            s4,e4,t4,
                            s5,e5,t5,
                            s6,e6,t6,
                            s7,e7,t7,
                            s8,e8,t8));
                    for (Product p : main.mLine.getProducts())
                        if (p.getId().equals(mProduct.getId()))
                            p.setFirst(mProduct.getFirst());
                    mAdapter1.setHours(mProduct.getFirst().getHours());
                    mAdapter1.notifyDataSetChanged();
                    updateLine();
                    dialog.dismiss();
                }
                else
                if (shiftNumber==2)
                {
                    mProduct.setSecond(mPresenter.getshift(
                            s1,e1,t1,
                            s2,e2,t2,
                            s3,e3,t3,
                            s4,e4,t4,
                            s5,e5,t5,
                            s6,e6,t6,
                            s7,e7,t7,
                            s8,e8,t8));
                    for (Product p : main.mLine.getProducts())
                        if (p.getId().equals(mProduct.getId()))
                            p.setSecond(mProduct.getSecond());
                    mAdapter2.setHours(mProduct.getSecond().getHours());
                    mAdapter2.notifyDataSetChanged();
                    updateLine();
                    dialog.dismiss();
                }
                else
                if (shiftNumber==3)
                {
                    mProduct.setThird(mPresenter.getshift(
                            s1,e1,t1,
                            s2,e2,t2,
                            s3,e3,t3,
                            s4,e4,t4,
                            s5,e5,t5,
                            s6,e6,t6,
                            s7,e7,t7,
                            s8,e8,t8));

                    for (Product p : main.mLine.getProducts())
                        if (p.getId().equals(mProduct.getId()))
                            p.setThird(mProduct.getThird());

                    mAdapter3.setHours(mProduct.getThird().getHours());
                    mAdapter3.notifyDataSetChanged();
                    updateLine();
                    dialog.dismiss();
                }

            }
        });

    }

    @Override
    public void productClick(Product product) {

        ArrayList<Product> list = new ArrayList<>();
        list.add(product);

        if (main.mLine.getProducts()==null) {
            main.mLine.setProducts(list);
            Toast.makeText(main, "The product has been added to the list", Toast.LENGTH_SHORT).show();
            updateLine();
        }
        else {

            boolean exist = false;
            for (Product p : main.mLine.getProducts())
                if (p.getId().equals(product.getId()))
                    exist = true;

            if (!exist) {
                main.mLine.getProducts().add(product);
                Toast.makeText(main, "The product has been added to the list", Toast.LENGTH_SHORT).show();
                updateLine();
            } else
                Toast.makeText(main, "The product is already added in the list", Toast.LENGTH_SHORT).show();
        }

        dialog.dismiss();
    }

    @Override
    public void onDelete(Product product) {

    }

    @Override
    public void bindPresenter(LineProductContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
