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
import com.app.smeezy.responsemodels.Feedback;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class MyFeedbackAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Feedback> feedbackList;


    public MyFeedbackAdapter(Context context, ArrayList<Feedback> feedbackList) {

        this.mContext = context;
        this.feedbackList = feedbackList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.feedback_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            final Feedback feedback = feedbackList.get(position);

            viewHolder.tv_name.setText(feedback.getName());
            viewHolder.tv_message.setText(feedback.getComment());
            viewHolder.tv_date_time.setText(feedback.getAddedOn());

            if (feedback.getRate().equals("like")) {
                viewHolder.img_like.setVisibility(View.VISIBLE);
                viewHolder.img_dislike.setVisibility(View.GONE);
            } else if (feedback.getRate().equals("dislike")) {
                viewHolder.img_dislike.setVisibility(View.VISIBLE);
                viewHolder.img_like.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(feedback.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            viewHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!feedback.getUserId().equals(PreferenceUtils.getId(mContext))) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("memberId", feedback.getUserId());
                        mContext.startActivity(intent);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_message, tv_date_time;
        public ImageView img_user, img_like, img_dislike;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_feedback_list_item_name);
            tv_message = (TextView) itemView.findViewById(R.id.tv_feedback_list_item_message);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_feedback_list_item_date_time);
            img_user = (ImageView) itemView.findViewById(R.id.img_feedback_list_item_user);
            img_like = (ImageView) itemView.findViewById(R.id.img_feedback_list_item_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_feedback_list_item_dislike);
        }
    }
}
