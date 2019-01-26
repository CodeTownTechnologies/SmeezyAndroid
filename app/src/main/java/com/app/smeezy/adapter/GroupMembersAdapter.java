package com.app.smeezy.adapter;

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
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.responsemodels.GroupMember;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class GroupMembersAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<GroupMember> adminList;
    private ArrayList<GroupMember> membersList;


    public GroupMembersAdapter(Context context, ArrayList<GroupMember> adminList,
                               ArrayList<GroupMember> membersList){

        this.mContext = context;
        this.adminList = adminList;
        this.membersList = membersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.group_member_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            final GroupMember member = membersList.get(position);

            viewHolder.tv_name.setText(member.getName());
            viewHolder.tv_location.setText(String.format("%s, %s", member.getCity(), member.getRegion()));

            if (member.getRole().equals("admin")){
                viewHolder.tv_role.setText(mContext.getString(R.string.role_admin));
            }else {
                viewHolder.tv_role.setText(mContext.getString(R.string.role_member));
            }

            Glide.with(mContext)
                    .load(member.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            viewHolder.ll_group_member_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!member.getUserId().equals(PreferenceUtils.getId(mContext))) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("memberId", member.getUserId());
                        mContext.startActivity(intent);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_group_member_item;
        public TextView tv_name, tv_role, tv_location;
        public ImageView img_user;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_group_member_item = (LinearLayout) itemView.findViewById(R.id.ll_group_member_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_group_member_item_name);
            tv_role = (TextView) itemView.findViewById(R.id.tv_group_member_item_role);
            tv_location = (TextView) itemView.findViewById(R.id.tv_group_member_item_location);
            img_user = (ImageView) itemView.findViewById(R.id.img_group_member_item_user);
        }
    }
}
