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

public class MutualFriendsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<User> friendList;


    public MutualFriendsAdapter(Context context, ArrayList<User> friendList) {

        this.mContext = context;
        this.friendList = friendList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.mutual_friends_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            final User friend = friendList.get(position);

            viewHolder.tv_name.setText(friend.getName());
            viewHolder.tv_location.setText(String.format("%s, %s", friend.getCity(), friend.getRegion()));


            Glide.with(mContext)
                    .load(friend.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            viewHolder.ll_friend_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!friend.getId().equals(PreferenceUtils.getId(mContext))) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("memberId", friend.getId());
                        mContext.startActivity(intent);
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

        public LinearLayout ll_friend_item;
        public TextView tv_name, tv_location;
        public ImageView img_user;
        public Button btn_follow, btn_unfollow;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_friend_item = (LinearLayout) itemView.findViewById(R.id.ll_mutual_friends_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_mutual_friends_item_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_mutual_friends_item_location);
            img_user = (ImageView) itemView.findViewById(R.id.img_mutual_friends_item_user);
            btn_follow = (Button) itemView.findViewById(R.id.btn_mutual_friends_item_follow);
            btn_unfollow = (Button) itemView.findViewById(R.id.btn_mutual_friends_item_unfollow);
        }
    }
}
