package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.InterestListener;
import com.app.smeezy.responsemodels.InterestItem;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class InterestListAdapter extends RecyclerView.Adapter {

    public int mSelectedItem = -1;
    private Context mContext;
    private ArrayList<InterestItem> interestList;
    private InterestListener interestListener;


    public InterestListAdapter(Context context, ArrayList<InterestItem> interestList,
                               InterestListener interestListener){

        this.mContext = context;
        this.interestList = interestList;
        this.interestListener = interestListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.interest_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder){

            final ViewHolder viewHolder = (ViewHolder) holder;

            final InterestItem interestItem = interestList.get(position);

            viewHolder.tv_name.setText(interestItem.getTitle());

            viewHolder.rb_stuff.setChecked(position == mSelectedItem);

        }

    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout ll_interest_item;
        public TextView tv_name;
        public RadioButton rb_stuff;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_interest_item = (LinearLayout) itemView.findViewById(R.id.ll_interest_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_interest_item_name);
            rb_stuff = (RadioButton) itemView.findViewById(R.id.rb_interest_item);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedItem != -1){
                        interestList.get(mSelectedItem).setIsSelected(false);
                    }
                    mSelectedItem = getAdapterPosition();
                    interestList.get(mSelectedItem).setIsSelected(true);
                    interestListener.onInterestClickListener(getAdapterPosition());
                    notifyDataSetChanged();
                }
            };
            ll_interest_item.setOnClickListener(clickListener);
            rb_stuff.setOnClickListener(clickListener);
        }
    }

    public void clearSelection(){

        if (mSelectedItem != -1) {
            interestList.get(mSelectedItem).setIsSelected(false);
        }
        mSelectedItem = -1;
        notifyDataSetChanged();

    }
}
