package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.DowntimeViewHolder> {
    private ArrayList<Product> products;
    private OnItemClick onItemClick;

    public ProductAdapter(ArrayList<Product> products, OnItemClick onItemClick) {
        if (products !=null)
            Collections.sort(products,Product.NameComparator);

        this.products = products;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DowntimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row,parent,false);
        return new DowntimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DowntimeViewHolder holder, int position) {
        final Product product = products.get(position);
        if (product!=null)
        {
            holder.mTvName.setText(product.getName());
            if (product.getCode()!=null && !product.getCode().isEmpty()) {
                holder.mTvCode.setText(product.getCode());
                holder.mTvCode.setVisibility(View.VISIBLE);
            }
            else
                holder.mTvCode.setVisibility(View.GONE);

            if (product.getDescription()!=null && !product.getCode().isEmpty()) {
                holder.mTvDescription.setText(product.getDescription());
                holder.mTvDescription.setVisibility(View.VISIBLE);
            }
            else
                holder.mTvDescription.setVisibility(View.GONE);

            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.productClick(product);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    class DowntimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_description) TextView mTvDescription;
        @BindView(R.id.tv_code) TextView mTvCode;
        @BindView(R.id.ll) CardView mLl;
        //@BindView(R.id.iv_delete) ImageView mBtDelete;
        DowntimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void productClick(Product product);
        void onDelete(Product product);
    }
}
