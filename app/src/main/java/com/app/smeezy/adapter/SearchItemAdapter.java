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
import com.app.smeezy.activity.SearchActivity;
import com.app.smeezy.activity.StuffDetailActivity;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class SearchItemAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<StuffFeedItem> stuffList;


    public SearchItemAdapter(Context context, ArrayList<StuffFeedItem> stuffList) {

        this.mContext = context;
        this.stuffList = stuffList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.search_item_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            final StuffFeedItem feedItem = stuffList.get(position);

            viewHolder.tv_name.setText(feedItem.getStuffText());
            viewHolder.tv_category.setText(feedItem.getCategoryName());

            Glide.with(mContext)
                    .load(feedItem.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .into(viewHolder.img_item);

            viewHolder.ll_search_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, StuffDetailActivity.class);
                    intent.putExtra("stuffFeedItem", feedItem);
                    mContext.startActivity(intent);


                }
            });


            if (position == stuffList.size() - 1) {
                ((SearchActivity) mContext).onBottomReached();
            }

        }

    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_search_item;
        public TextView tv_name, tv_category;
        public ImageView img_item;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_search_item = (LinearLayout) itemView.findViewById(R.id.ll_search_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_search_item_name);
            tv_category = (TextView) itemView.findViewById(R.id.tv_search_item_category);
            img_item = (ImageView) itemView.findViewById(R.id.img_search_item);
        }
    }
}
