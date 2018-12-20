package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 26/09/2018.
 */
public class EmailSelectionAdapter extends RecyclerView.Adapter<EmailSelectionAdapter.EmailViewHolder> {
    private ArrayList<Email> emails;

    public EmailSelectionAdapter(ArrayList<Email> emails) {
        if (emails!=null)
            Collections.sort(emails,Email.NameComparator);
        this.emails = emails;
    }


    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_row_selection,parent,false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmailViewHolder holder, final int position) {
        final Email email = emails.get(position);
        if (email!=null)
        {
            holder.mTvName.setText(email.getName());
            holder.mTvInfo.setText(email.getEmail());
            holder.mCbS1.setChecked(email.isShift1());
            holder.mCbCc1.setChecked(email.isCc1());

            holder.mCbCc1.setOnClickListener(new CompoundButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email.setCc1(holder.mCbCc1.isChecked());
                }
            });


            holder.mCbS1.setOnClickListener(new CompoundButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email.setShift1(holder.mCbS1.isChecked());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (emails!=null && emails.size()>0)
            return emails.size();
        return 0;
    }

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<Email> emails) {
        this.emails = emails;
    }

    class EmailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_info) TextView mTvInfo;
        @BindView(R.id.ll_email) LinearLayout mLlEmail;
        @BindView(R.id.cc_1) CheckBox mCbCc1;
        @BindView(R.id.cb_1) CheckBox mCbS1;


        EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
