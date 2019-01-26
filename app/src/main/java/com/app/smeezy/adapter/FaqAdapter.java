package com.app.smeezy.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.FaqItem;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class FaqAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<FaqItem> faqList;


    public FaqAdapter(Context context, ArrayList<FaqItem> faqList) {

        this.mContext = context;
        this.faqList = faqList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.faq_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;
            FaqItem faqItem = faqList.get(position);

            viewHolder.tv_question.setText(faqItem.getFaqQuestion());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                viewHolder.tv_answer.setText(Html.fromHtml(faqItem.getFaqAnswer()));
            } else {
                viewHolder.tv_answer.setText(Html.fromHtml(faqItem.getFaqAnswer(), Html.FROM_HTML_MODE_LEGACY));
            }

            viewHolder.tv_answer.setMovementMethod(LinkMovementMethod.getInstance());

        }

    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_question, tv_answer;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_question = (TextView) itemView.findViewById(R.id.tv_faq_item_question);
            tv_answer = (TextView) itemView.findViewById(R.id.tv_faq_item_answer);
        }
    }
}
