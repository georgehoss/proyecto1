package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {
    private ArrayList<Email> emails;
    private OnEmailInteraction onEmailInteraction;

    public EmailAdapter(ArrayList<Email> emails, OnEmailInteraction onEmailInteraction) {
        if (emails!=null)
            Collections.sort(emails,Email.EmailNameComparator);
        this.emails = emails;
        this.onEmailInteraction = onEmailInteraction;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_row,parent,false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        final Email email = emails.get(position);
        if (email!=null)
        {
            holder.mTvName.setText(email.getName());
            holder.mTvInfo.setText(email.getEmail());
            holder.mLlEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEmailInteraction.EditEmail(email);
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
        EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnEmailInteraction{
        void EditEmail(Email email);
    }
}
