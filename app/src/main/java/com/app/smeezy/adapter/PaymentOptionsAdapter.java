package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.PaymentOption;

import java.util.ArrayList;


public class PaymentOptionsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<PaymentOption> paymentOptionsList;

    public PaymentOptionsAdapter(Context context, ArrayList<PaymentOption> paymentOptionsList) {

        this.mContext = context;
        this.paymentOptionsList = paymentOptionsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.payment_option_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            final PaymentOption paymentOption = paymentOptionsList.get(position);

            viewHolder.cb_title.setText(paymentOption.getTitle());

            if (paymentOption.getIsSelected()) {
                viewHolder.cb_title.setChecked(true);
            }else {
                viewHolder.cb_title.setChecked(false);
            }

            viewHolder.cb_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        paymentOptionsList.get(position).setIsSelected(true);
                    } else {
                        paymentOptionsList.get(position).setIsSelected(false);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return paymentOptionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cb_title;

        public ViewHolder(View itemView) {
            super(itemView);

            cb_title = (CheckBox) itemView.findViewById(R.id.cb_title);
        }
    }
}
