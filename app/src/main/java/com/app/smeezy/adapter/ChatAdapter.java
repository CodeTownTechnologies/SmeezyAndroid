package com.app.smeezy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.interfacess.ChatListener;
import com.app.smeezy.responsemodels.SingleChatItem;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int TYPE_DATE = 1;
    private static final int TYPE_RECEIVED = 2;
    private static final int TYPE_SENT = 3;

    private Context mContext;
    private ArrayList<SingleChatItem> singleChatList;
    private String friendImage;
    private ChatListener chatListener;

    public ChatAdapter(Context context, ArrayList<SingleChatItem> singleChatList, String friendImage,
                       ChatListener chatListener) {
        this.mContext = context;
        this.singleChatList = singleChatList;
        this.friendImage = friendImage;
        this.chatListener = chatListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_RECEIVED) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.received_chat_item, parent, false);
            return new ReceivedChatViewHolder(view);
        } else if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sent_chat_item, parent, false);
            return new SentChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.date_chat_item, parent, false);
            return new DateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SentChatViewHolder) {

            SentChatViewHolder viewHolder = (SentChatViewHolder) holder;
            SingleChatItem chatItem = singleChatList.get(position);

            if (chatItem.getMessage().isEmpty()) {
                viewHolder.tv_sent_message.setVisibility(View.GONE);
            } else {
                viewHolder.tv_sent_message.setText(chatItem.getMessage());
            }

            viewHolder.tv_sent_time.setText(chatItem.getAddDate());

            Glide.with(mContext)
                    .load(PreferenceUtils.getProfilePicUrl(mContext) + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_sent_user);

            if (chatItem.getData().getStuffType().equals("") ||
                    chatItem.getData().getStuffName().equals("null")) {
                viewHolder.tv_sent_type.setVisibility(View.GONE);
            } else {
                viewHolder.tv_sent_type.setVisibility(View.VISIBLE);
                if (chatItem.getData().getAction().equals("trade_offer") &&
                        chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_trade_something_for),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("trade_offer")) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_trade_their),
                            chatItem.getName(), chatItem.getData().getSelectedStuff(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("ask_borrow") && !chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_borrow_and_trade),
                            chatItem.getName(), chatItem.getData().getStuffName(), chatItem.getData().getSelectedStuff()));
                } else if (chatItem.getData().getAction().equals("ask_borrow")) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_borrow),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("inquire")) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_borrow_requesting),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("buy") && !chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_buy_and_trade),
                            chatItem.getName(), chatItem.getData().getStuffName(), chatItem.getData().getSelectedStuff()));
                } else if (chatItem.getData().getAction().equals("buy")) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_buy),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("rent") && !chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_rent_and_trade),
                            chatItem.getName(), chatItem.getData().getStuffName(), chatItem.getData().getSelectedStuff()));
                } else if (chatItem.getData().getAction().equals("rent")) {
                    viewHolder.tv_sent_type.setText(String.format(mContext.getString(R.string.would_like_to_rent),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                }
            }

        } else if (holder instanceof ReceivedChatViewHolder) {

            ReceivedChatViewHolder viewHolder = (ReceivedChatViewHolder) holder;
            final SingleChatItem chatItem = singleChatList.get(position);

            if (chatItem.getMessage().isEmpty()) {
                viewHolder.tv_received_message.setVisibility(View.GONE);
            } else {
                viewHolder.tv_received_message.setText(chatItem.getMessage());
            }

            viewHolder.tv_received_time.setText(chatItem.getAddDate());

            Glide.with(mContext)
                    .load(friendImage + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_received_user);

            viewHolder.img_received_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", chatItem.getSenderId());
                    mContext.startActivity(intent);
                }
            });

            if (chatItem.getData().getStuffType().equals("")||
                    chatItem.getData().getStuffName().equals("null")) {
                viewHolder.tv_received_type.setVisibility(View.GONE);
            } else {
                viewHolder.tv_received_type.setVisibility(View.VISIBLE);
                if (chatItem.getData().getAction().equals("trade_offer") &&
                        chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_trade_something_for),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("trade_offer")) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_trade_their),
                            chatItem.getName(), chatItem.getData().getSelectedStuff(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("ask_borrow") && !chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_borrow_and_trade),
                            chatItem.getName(), chatItem.getData().getStuffName(), chatItem.getData().getSelectedStuff()));
                } else if (chatItem.getData().getAction().equals("ask_borrow")) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_borrow),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("inquire")) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_borrow_requesting),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("buy") && !chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_buy_and_trade),
                            chatItem.getName(), chatItem.getData().getStuffName(), chatItem.getData().getSelectedStuff()));
                } else if (chatItem.getData().getAction().equals("buy")) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_buy),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                } else if (chatItem.getData().getAction().equals("rent") && !chatItem.getData().getSelectedStuff().isEmpty()) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_rent_and_trade),
                            chatItem.getName(), chatItem.getData().getStuffName(), chatItem.getData().getSelectedStuff()));
                } else if (chatItem.getData().getAction().equals("rent")) {
                    viewHolder.tv_received_type.setText(String.format(mContext.getString(R.string.would_like_to_rent),
                            chatItem.getName(), chatItem.getData().getStuffName()));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return singleChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (singleChatList.get(position).getSenderId().equals(PreferenceUtils.getId(mContext))) {
            return TYPE_SENT;
        } else if (singleChatList.get(position).getReceiverId().equals(PreferenceUtils.getId(mContext))) {
            return TYPE_RECEIVED;
        }

        return TYPE_DATE;
    }

    public class ReceivedChatViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_received_message, tv_received_time, tv_received_type;
        public ImageView img_received_user, img_received_content;

        public ReceivedChatViewHolder(View itemView) {
            super(itemView);

            tv_received_message = (TextView) itemView.findViewById(R.id.tv_received_chat_item_message);
            tv_received_time = (TextView) itemView.findViewById(R.id.tv_received_chat_item_time);
            img_received_user = (ImageView) itemView.findViewById(R.id.img_received_chat_item_user);
            img_received_content = (ImageView) itemView.findViewById(R.id.img_received_chat_item_content);
            tv_received_type = (TextView) itemView.findViewById(R.id.tv_received_chat_item_type);
        }
    }

    public class SentChatViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_sent_message, tv_sent_time, tv_sent_type;
        public ImageView img_sent_user, img_sent_content;

        public SentChatViewHolder(View itemView) {
            super(itemView);

            tv_sent_message = (TextView) itemView.findViewById(R.id.tv_sent_chat_item_message);
            tv_sent_time = (TextView) itemView.findViewById(R.id.tv_sent_chat_item_time);
            img_sent_user = (ImageView) itemView.findViewById(R.id.img_sent_chat_item_user);
            img_sent_content = (ImageView) itemView.findViewById(R.id.img_sent_chat_item_content);
            tv_sent_type = (TextView) itemView.findViewById(R.id.tv_sent_chat_item_type);

        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_date;

        public DateViewHolder(View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_date_chat_item);
        }
    }
}
