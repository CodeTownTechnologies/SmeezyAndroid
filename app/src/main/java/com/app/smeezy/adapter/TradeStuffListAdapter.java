package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.TradeStuffListener;
import com.app.smeezy.responsemodels.UserStuff;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class TradeStuffListAdapter extends RecyclerView.Adapter {

    public int mSelectedItem = -1;
    private Context mContext;
    private ArrayList<UserStuff> stuffList;
    private TradeStuffListener tradeStuffListener;


    public TradeStuffListAdapter(Context context, ArrayList<UserStuff> stuffList,
                                 TradeStuffListener tradeStuffListener) {

        this.mContext = context;
        this.stuffList = stuffList;
        this.tradeStuffListener = tradeStuffListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trade_stuff_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            final UserStuff stuff = stuffList.get(position);

            viewHolder.tv_name.setText(stuff.getStuffText());
            viewHolder.tv_category.setText(stuff.getCategoryName());

            viewHolder.rb_stuff.setChecked(position == mSelectedItem);

            Glide.with(mContext)
                    .load(stuff.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .into(viewHolder.img_stuff);


        }

    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_trade_stuff_item;
        public TextView tv_name, tv_category;
        public ImageView img_stuff;
        public RadioButton rb_stuff;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_trade_stuff_item = (LinearLayout) itemView.findViewById(R.id.ll_trade_stuff_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_trade_stuff_item_name);
            tv_category = (TextView) itemView.findViewById(R.id.tv_trade_stuff_item_category);
            img_stuff = (ImageView) itemView.findViewById(R.id.img_trade_stuff_item);
            rb_stuff = (RadioButton) itemView.findViewById(R.id.rb_trade_stuff_item);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedItem != -1) {
                        stuffList.get(mSelectedItem).setIsSelected(false);
                    }
                    mSelectedItem = getAdapterPosition();
                    stuffList.get(mSelectedItem).setIsSelected(true);
                    tradeStuffListener.onStuffClickListener(getAdapterPosition());
                    notifyDataSetChanged();
                }
            };
            ll_trade_stuff_item.setOnClickListener(clickListener);
            rb_stuff.setOnClickListener(clickListener);
        }
    }

    public void clearSelection() {
        if (mSelectedItem != -1) {
            stuffList.get(mSelectedItem).setIsSelected(false);
        }
        mSelectedItem = -1;
        notifyDataSetChanged();

    }
}
