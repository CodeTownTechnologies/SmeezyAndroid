package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.PaymentOption;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class StuffDetailPaymentOptionsAdapter extends RecyclerView.Adapter {

    public int mSelectedItem = -1;
    private Context mContext;
    private ArrayList<PaymentOption> paymentOptionList;


    public StuffDetailPaymentOptionsAdapter(Context context, ArrayList<PaymentOption> paymentOptionList) {

        this.mContext = context;
        this.paymentOptionList = paymentOptionList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.stuff_detail_payment_options_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            PaymentOption paymentOption = paymentOptionList.get(position);

            viewHolder.tv_title.setText(paymentOption.getTitle());

            viewHolder.rb_stuff.setChecked(position == mSelectedItem);


        }

    }

    @Override
    public int getItemCount() {
        return paymentOptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_title;
        public RadioButton rb_stuff;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            rb_stuff = (RadioButton) itemView.findViewById(R.id.rb_item);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedItem != -1) {
                        paymentOptionList.get(mSelectedItem).setIsSelected(false);
                    }
                    mSelectedItem = getAdapterPosition();
                    paymentOptionList.get(mSelectedItem).setIsSelected(true);
                    notifyDataSetChanged();
                }
            };
            ll_item.setOnClickListener(clickListener);
            rb_stuff.setOnClickListener(clickListener);
        }
    }

    public void clearSelection() {
        if (mSelectedItem != -1) {
            paymentOptionList.get(mSelectedItem).setIsSelected(false);
        }
        mSelectedItem = -1;
        notifyDataSetChanged();

    }
}
