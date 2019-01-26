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
import com.app.smeezy.interfacess.FriendListListener;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class FriendListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<User> friendList;
    private boolean ownProfile;
    private FriendListListener friendListListener;


    public FriendListAdapter(Context context, ArrayList<User> friendList, boolean ownProfile,
                             FriendListListener friendListListener) {

        this.mContext = context;
        this.friendList = friendList;
        this.ownProfile = ownProfile;
        this.friendListListener = friendListListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            final User friend = friendList.get(position);

            viewHolder.tv_name.setText(friend.getName());
            if (friend.getCity().isEmpty() && friend.getRegion().isEmpty()) {
                viewHolder.tv_location.setVisibility(View.GONE);
            } else if (!friend.getCity().isEmpty()) {
                viewHolder.tv_location.setText(String.format("%s", friend.getCity()));
            } else if (!friend.getRegion().isEmpty()) {
                viewHolder.tv_location.setText(String.format("%s", friend.getRegion()));
            } else {
                viewHolder.tv_location.setText(String.format("%s, %s", friend.getCity(), friend.getRegion()));
            }

            if (ownProfile) {
                if (friend.getIsUnfollowUser().equals("1")) {
                    viewHolder.btn_follow.setVisibility(View.VISIBLE);
                    viewHolder.btn_unfollow.setVisibility(View.GONE);
                } else {
                    //viewHolder.btn_unfollow.setVisibility(View.VISIBLE);
                    viewHolder.btn_unfollow.setVisibility(View.GONE);
                    viewHolder.btn_follow.setVisibility(View.GONE);
                }

            } else {
                viewHolder.btn_follow.setVisibility(View.GONE);
                viewHolder.btn_unfollow.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(friend.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            viewHolder.ll_friend_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!friend.getId().equals(PreferenceUtils.getId(mContext))) {
                        friendListListener.onProfileClick(position, friend);
                    }
                }
            });


            viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendListListener.onFollowUser(position, friend);
                }
            });

            viewHolder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendListListener.onUnfollowUser(position, friend);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_friend_item;
        public TextView tv_name, tv_location;
        public ImageView img_user;
        public Button btn_follow, btn_unfollow;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_friend_item = (LinearLayout) itemView.findViewById(R.id.ll_friend_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_friend_item_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_friend_item_location);
            img_user = (ImageView) itemView.findViewById(R.id.img_friend_item_user);
            btn_follow = (Button) itemView.findViewById(R.id.btn_friend_item_follow);
            btn_unfollow = (Button) itemView.findViewById(R.id.btn_friend_item_unfollow);
        }
    }
}
