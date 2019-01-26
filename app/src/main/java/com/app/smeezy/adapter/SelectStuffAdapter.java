package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.Stuff;
import com.app.smeezy.responsemodels.User;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class SelectStuffAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Stuff> stuffList;
    private ArrayList<Integer> selectedUsers;

    public SelectStuffAdapter(Context context, ArrayList<Stuff> stuffList, ArrayList<Integer> selectedUsers) {

        this.mContext = context;
        this.stuffList = stuffList;
        this.selectedUsers = selectedUsers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.choose_stuff_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            final Stuff stuff = stuffList.get(position);

            viewHolder.cb_name.setText(stuff.getTitle());

            if (stuff.getIsSelected()) {
                viewHolder.cb_name.setChecked(true);
            }else {
                viewHolder.cb_name.setChecked(false);
            }

            viewHolder.cb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        stuffList.get(viewHolder.getAdapterPosition()).setIsSelected(true);
                    } else {
                        stuffList.get(viewHolder.getAdapterPosition()).setIsSelected(false);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cb_name;

        public ViewHolder(View itemView) {
            super(itemView);

            cb_name = (CheckBox) itemView.findViewById(R.id.cb_choose_stuff_item_name);
        }
    }
}
