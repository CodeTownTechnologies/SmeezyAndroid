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
import com.app.smeezy.interfacess.SuggestedFriendsListener;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SuggestedFriendsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<User> userList;
    private SuggestedFriendsListener suggestedFriendsListener;


    public SuggestedFriendsAdapter(Context context, ArrayList<User> userList,
                                   SuggestedFriendsListener suggestedFriendsListener) {
        this.mContext = context;
        this.userList = userList;
        this.suggestedFriendsListener = suggestedFriendsListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.suggested_friends_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            final User user = userList.get(position);

            viewHolder.tv_name.setText(user.getName());
            if (user.getCity().isEmpty() && user.getRegion().isEmpty()) {
                viewHolder.tv_location.setVisibility(View.GONE);
            } else if (!user.getCity().isEmpty()) {
                viewHolder.tv_location.setText(String.format("%s", user.getCity()));
            } else if (!user.getRegion().isEmpty()) {
                viewHolder.tv_location.setText(String.format("%s", user.getRegion()));
            } else {
                viewHolder.tv_location.setText(String.format("%s, %s", user.getCity(), user.getRegion()));
            }

            if (user.getMutualFriends() == 0){
                viewHolder.tv_mutual_friends.setVisibility(View.GONE);
            }else {
                viewHolder.tv_mutual_friends.setVisibility(View.VISIBLE);
                viewHolder.tv_mutual_friends.setText(mContext.getResources().getQuantityString(R.plurals.mutual_friends,
                        user.getMutualFriends(), user.getMutualFriends()));
            }


            Glide.with(mContext)
                    .load(user.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            if (user.getIsRequestSent().equals("1")) {
                if (user.getConnectionRecord().getFromMemberId().equals(PreferenceUtils.getId(mContext))) {
                    viewHolder.btn_add_friend.setVisibility(View.VISIBLE);
                    viewHolder.btn_add_friend.setText(mContext.getString(R.string.request_sent));
                    viewHolder.btn_add_friend.setEnabled(false);
                    viewHolder.btn_add_friend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (user.getConnectionRecord().getFromMemberId().equals(user.getId())) {
                    viewHolder.btn_add_friend.setVisibility(View.GONE);
                }
            } else {
                viewHolder.btn_add_friend.setVisibility(View.VISIBLE);
                viewHolder.btn_add_friend.setText(mContext.getString(R.string.add_friend));
                viewHolder.btn_add_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        suggestedFriendsListener.onAddFriend(position, user);
                    }
                });

            }

            viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", user.getId());
                    mContext.startActivity(intent);


                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_name, tv_location, tv_mutual_friends;
        public ImageView img_user;
        public Button btn_add_friend;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_suggested_friends);
            tv_name = (TextView) itemView.findViewById(R.id.tv_suggested_friends_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_suggested_friends_location);
            img_user = (ImageView) itemView.findViewById(R.id.img_suggested_friends_user);
            btn_add_friend = (Button) itemView.findViewById(R.id.btn_suggested_friends_add_friend);
            tv_mutual_friends = (TextView) itemView.findViewById(R.id.tv_suggested_friends_mutual_friends);
        }
    }
}
