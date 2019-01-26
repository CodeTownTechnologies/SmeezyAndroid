package com.app.smeezy.adapter;

import android.app.Activity;
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
import com.app.smeezy.activity.EventDetailActivity;
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.responsemodels.BirthdayItem;
import com.app.smeezy.responsemodels.Event;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.app.smeezy.utills.StaticData.EVENT_DETAIL;

/**
 * Created by Rahul on 03-01-2018.
 */

public class BirthdayAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<BirthdayItem> birthdayList;


    public BirthdayAdapter(Context context, ArrayList<BirthdayItem> birthdayList){

       this.mContext = context;
       this.birthdayList = birthdayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.birthday_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder){

            ViewHolder viewHolder = (ViewHolder) holder;

            final BirthdayItem birthday = birthdayList.get(position);

            viewHolder.tv_name.setText(birthday.getName());

            Glide.with(mContext)
                    .load(birthday.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .into(viewHolder.img_birthday);

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd, yyyy");

            Date inputDate;

            try {
                if (birthday.getDob() != null) {
                    inputDate = inputDateFormat.parse(birthday.getDob());
                    viewHolder.tv_date.setText(outputDateFormat.format(inputDate));
                }
            }catch (ParseException e){
                e.printStackTrace();
            }

            viewHolder.ll_birthday_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", birthday.getFriendId());
                    mContext.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return birthdayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_birthday_item;
        public TextView tv_name, tv_date;
        public ImageView img_birthday;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_birthday_item = (LinearLayout) itemView.findViewById(R.id.ll_birthday_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_birthday_item_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_birthday_item_date);
            img_birthday = (ImageView) itemView.findViewById(R.id.img_birthday_item);
        }
    }
}
