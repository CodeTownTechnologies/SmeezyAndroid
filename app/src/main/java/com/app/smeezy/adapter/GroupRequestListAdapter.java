package com.app.smeezy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.interfacess.GroupRequestListener;
import com.app.smeezy.responsemodels.GroupRequest;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class GroupRequestListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<GroupRequest> groupRequestList;
    private GroupRequestListener groupRequestListener;


    public GroupRequestListAdapter(Context context, ArrayList<GroupRequest> groupRequestList,
                                   GroupRequestListener groupRequestListener) {

        this.mContext = context;
        this.groupRequestList = groupRequestList;
        this.groupRequestListener = groupRequestListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.group_request_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            final GroupRequest groupRequest = groupRequestList.get(position);

            viewHolder.tv_name.setText(groupRequest.getName());

            Glide.with(mContext)
                    .load(groupRequest.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .into(viewHolder.img_user);

            viewHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", groupRequest.getUserId());
                    mContext.startActivity(intent);
                }
            });


            viewHolder.btn_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   groupRequestListener.onApprove(position,
                           groupRequest.getId());
                }
            });

            viewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupRequestListener.onReject(position,
                            groupRequest.getId());
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return groupRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public ImageView img_user;
        public Button btn_approve, btn_reject;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_group_request_item_name);
            img_user = (ImageView) itemView.findViewById(R.id.img_group_request_item);
            btn_approve = (Button) itemView.findViewById(R.id.btn_group_request_approve);
            btn_reject = (Button) itemView.findViewById(R.id.btn_group_request_reject);
        }
    }
}
