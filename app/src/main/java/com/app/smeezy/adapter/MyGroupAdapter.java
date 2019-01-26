package com.app.smeezy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.GroupDetailActivity;
import com.app.smeezy.responsemodels.Group;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.app.smeezy.utills.StaticData.GROUP_DETAIL;

/**
 * Created by Rahul on 03-01-2018.
 */

public class MyGroupAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Group> groupList;


    public MyGroupAdapter(Context context, ArrayList<Group> groupList){

       this.mContext = context;
       this.groupList = groupList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            final Group group = groupList.get(position);

            viewHolder.tv_name.setText(group.getTitle());
            viewHolder.tv_desc.setText(group.getDescription());

            Glide.with(mContext)
                    .load(group.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .into(viewHolder.img_group);

            viewHolder.ll_group_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GroupDetailActivity.class);
                    intent.putExtra("group", group);
                    ((Activity) mContext).startActivityForResult(intent, GROUP_DETAIL);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_group_item;
        public TextView tv_name, tv_desc;
        public ImageView img_group;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_group_item = (LinearLayout) itemView.findViewById(R.id.ll_group_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_group_item_name);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_group_item_desc);
            img_group = (ImageView) itemView.findViewById(R.id.img_group_item);
        }
    }
}
