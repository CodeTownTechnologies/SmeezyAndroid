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
import com.app.smeezy.interfacess.BlockedListListener;
import com.app.smeezy.responsemodels.BlockedUser;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class BlockedListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<BlockedUser> userList;
    private BlockedListListener blockedListListener;


    public BlockedListAdapter(Context context, ArrayList<BlockedUser> userList, BlockedListListener blockedListListener){

       this.mContext = context;
       this.userList = userList;
       this.blockedListListener = blockedListListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.blocked_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            final BlockedUser user = userList.get(position);

            viewHolder.tv_name.setText(user.getName());
            viewHolder.tv_location.setText(String.format("%s, %s", user.getCity(), user.getRegion()));

            Glide.with(mContext)
                    .load(user.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

           /* viewHolder.ll_blocked_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", user.getSecondUserId());
                    mContext.startActivity(intent);
                }
            });*/

            viewHolder.btn_unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    blockedListListener.onUnblock(position, user.getSecondUserId());
                }
            });
            

        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_blocked_item;
        public TextView tv_name, tv_location;
        public ImageView img_user;
        public Button btn_unblock;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_blocked_item = (LinearLayout) itemView.findViewById(R.id.ll_blocked_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_blocked_item_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_blocked_item_location);
            img_user = (ImageView) itemView.findViewById(R.id.img_blocked_item_user);
            btn_unblock = (Button) itemView.findViewById(R.id.btn_blocked_item_unblock);
        }
    }
}
