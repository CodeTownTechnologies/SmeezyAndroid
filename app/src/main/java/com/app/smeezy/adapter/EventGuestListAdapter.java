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
import com.app.smeezy.responsemodels.EventGuest;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class EventGuestListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<EventGuest> guestList;


    public EventGuestListAdapter(Context context, ArrayList<EventGuest> guestList){

        this.mContext = context;
        this.guestList = guestList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.event_guest_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            final EventGuest guest = guestList.get(position);

            viewHolder.tv_name.setText(guest.getName());
            viewHolder.tv_location.setText(String.format("%s, %s", guest.getCity(), guest.getRegion()));

            Glide.with(mContext)
                    .load(guest.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(viewHolder.img_user);

            viewHolder.ll_event_guest_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!guest.getUserId().equals(PreferenceUtils.getId(mContext))) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("memberId", guest.getUserId());
                        mContext.startActivity(intent);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_event_guest_item;
        public TextView tv_name, tv_location;
        public ImageView img_user;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_event_guest_item = (LinearLayout) itemView.findViewById(R.id.ll_event_guest_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_event_guest_item_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_event_guest_item_location);
            img_user = (ImageView) itemView.findViewById(R.id.img_event_guest_item_user);
        }
    }
}
