package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.User;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class InviteAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<User> friendList;
    private ArrayList<Integer> selectedUsers;

    public InviteAdapter(Context context, ArrayList<User> friendList, ArrayList<Integer> selectedUsers) {

        this.mContext = context;
        this.friendList = friendList;
        this.selectedUsers = selectedUsers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.invite_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            final User user = friendList.get(position);

            viewHolder.cb_name.setText(user.getName());

            if (user.getIsSelected()) {
                viewHolder.cb_name.setChecked(true);
            }else {
                viewHolder.cb_name.setChecked(false);
            }

            viewHolder.cb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        friendList.get(position).setIsSelected(true);
                    } else {
                        friendList.get(position).setIsSelected(false);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cb_name;

        public ViewHolder(View itemView) {
            super(itemView);

            cb_name = (CheckBox) itemView.findViewById(R.id.cb_invite_item_name);
        }
    }
}
