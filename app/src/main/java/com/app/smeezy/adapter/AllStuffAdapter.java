package com.app.smeezy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.AddStuffActivity;
import com.app.smeezy.responsemodels.Stuff;
import com.app.smeezy.responsemodels.StuffCategory;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class AllStuffAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ADD = 0;
    private static final int TYPE_LIST = 1;

    private Context mContext;
    private ArrayList<Stuff> stuffList;
    private ArrayList<StuffCategory> stuffCategoryList;

    public AllStuffAdapter(Context context, ArrayList<Stuff> stuffList,
                           ArrayList<StuffCategory> stuffCategoryList) {

        this.mContext = context;
        this.stuffList = stuffList;
        this.stuffCategoryList = stuffCategoryList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ADD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.all_stuff_list_add_item, parent, false);

            return new AddViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.all_stuff_list_item, parent, false);

            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof AddViewHolder) {

            AddViewHolder viewHolder = (AddViewHolder) holder;

            viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, AddStuffActivity.class);
                    intent.putExtra("isManual", true);
                    intent.putExtra("stuffCategoryList", stuffCategoryList);
                    mContext.startActivity(intent);

                }
            });

        } else if (holder instanceof ListViewHolder) {

            ListViewHolder viewHolder = (ListViewHolder) holder;

            final Stuff stuff = stuffList.get(position - 1);

            viewHolder.tv_name.setText(stuff.getTitle());

            viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddStuffActivity.class);
                    intent.putExtra("isManual", false);
                    intent.putExtra("stuff", stuff);
                    mContext.startActivity(intent);
                }
            });



        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ADD;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public int getItemCount() {
        return stuffList.size() + 1;
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {

        public Button btn_add;

        public AddViewHolder(View itemView) {
            super(itemView);

            btn_add = (Button) itemView.findViewById(R.id.btn_add_stuff);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public LinearLayout ll_item;

        public ListViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_all_stuff_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_all_stuff_item_name);
        }
    }
}
