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
import com.app.smeezy.activity.ChatActivity;
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.responsemodels.ChatListItem;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<ChatListItem> chatList;


    public ChatListAdapter(Context context, ArrayList<ChatListItem> chatList){

       this.mContext = context;
       this.chatList = chatList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            final ChatListItem chatListItem = chatList.get(position);

            viewHolder.tv_message.setText(chatListItem.getMessage());
            viewHolder.tv_name.setText(chatListItem.getName());
            viewHolder.tv_date_time.setText(chatListItem.getModifyDate());

            if (chatListItem.getUnreadCount().equals("0")){
                viewHolder.tv_count.setVisibility(View.GONE);
            }else {
                viewHolder.tv_count.setVisibility(View.VISIBLE);
                viewHolder.tv_count.setText(chatListItem.getUnreadCount());
            }

            Glide.with(mContext)
                    .load(chatListItem.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            viewHolder.ll_chat_list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("friendId", chatListItem.getFriendId());
                    intent.putExtra("friendImage", chatListItem.getImage());
                    intent.putExtra("friendName", chatListItem.getName());
                    intent.putExtra("myAllowed", chatListItem.getMyAllowed());
                    intent.putExtra("blockStatus", chatListItem.getBlockStatus());
                    intent.putExtra("isFriend", chatListItem.getIsFriend());
                    mContext.startActivity(intent);
                }
            });

            viewHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chatListItem.getBlockStatus() == 0) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("memberId", chatListItem.getFriendId());
                        mContext.startActivity(intent);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_chat_list_item;
        public TextView tv_name, tv_message, tv_date_time, tv_count;
        public ImageView img_user;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_chat_list_item = (LinearLayout) itemView.findViewById(R.id.ll_chat_list_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_chat_list_item_name);
            tv_message = (TextView) itemView.findViewById(R.id.tv_chat_list_item_message);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_chat_list_item_date_time);
            img_user = (ImageView) itemView.findViewById(R.id.img_chat_list_item_user);
            tv_count = (TextView) itemView.findViewById(R.id.tv_chat_list_item_count);
        }
    }
}
