package com.tenneco.tennecoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 26/09/2018.
 */
public class UserSelectorAdapter extends RecyclerView.Adapter<UserSelectorAdapter.UserViewHolder> {
    private ArrayList<User> users;
    private Context context;

    public UserSelectorAdapter(ArrayList<User> users, Context context) {
        if (users!=null)
            Collections.sort(users,User.UserNameComparator);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_selector,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        final User user = users.get(position);
        if(user!=null)
        {
            holder.mTvName.setText(user.getName());
            holder.mTvInfo.setText(user.getEmail());
            holder.mCbSelect.setChecked(user.isSelected());

            switch (user.getType())
            {
                case 1:
                    holder.mIvIcon.setVisibility(View.VISIBLE);
                    holder.mIvIcon.setBackground(context.getResources().getDrawable(R.drawable.employee_icon));
                    break;
                case 2:
                    holder.mIvIcon.setVisibility(View.VISIBLE);
                    holder.mIvIcon.setBackground(context.getResources().getDrawable(R.drawable.supervisor_icon));
                    break;
                case 3:
                    holder.mIvIcon.setVisibility(View.VISIBLE);
                    holder.mIvIcon.setBackground(context.getResources().getDrawable(R.drawable.admin_icon));
                    break;
                default:
                        holder.mIvIcon.setVisibility(View.GONE);
                        break;
            }

            holder.mCbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.setSelected(holder.mCbSelect.isChecked());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (users!=null && users.size()>0)
            return users.size();
        return 0;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_info) TextView mTvInfo;
        @BindView(R.id.ll_user) LinearLayout mLlUser;
        @BindView(R.id.iv_icon) ImageView mIvIcon;
        @BindView(R.id.cb_select) CheckBox mCbSelect;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
