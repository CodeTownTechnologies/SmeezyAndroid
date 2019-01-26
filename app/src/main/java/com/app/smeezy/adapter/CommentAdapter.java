package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.ChatListItem;
import com.app.smeezy.responsemodels.Comment;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class CommentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Comment> commentList;


    public CommentAdapter(Context context, ArrayList<Comment> commentList){

       this.mContext = context;
       this.commentList = commentList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            Comment comment = commentList.get(position);

            viewHolder.tv_name.setText(comment.getName());
            viewHolder.tv_message.setText(comment.getComment());
            viewHolder.tv_date_time.setText(comment.getAddedOn());

            Glide.with(mContext)
                    .load(comment.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_name, tv_message, tv_date_time;
        public ImageView img_user;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_comment_list_item_name);
            tv_message = (TextView) itemView.findViewById(R.id.tv_comment_list_item_message);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_comment_list_item_date_time);
            img_user = (ImageView) itemView.findViewById(R.id.img_comment_list_item_user);
        }
    }
}
