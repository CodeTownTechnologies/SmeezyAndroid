package com.app.smeezy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.MyWishlistListener;
import com.app.smeezy.responsemodels.Stuff;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class MyWishlistAdapter extends RecyclerView.Adapter implements StickyRecyclerHeadersAdapter {

    private Context mContext;
    private ArrayList<Stuff> stuffList;
    private MyWishlistListener myWishlistListener;
    private boolean ownProfile;


    public MyWishlistAdapter(Context context, ArrayList<Stuff> stuffList,
                             MyWishlistListener myWishlistListener, boolean ownProfile) {

        this.mContext = context;
        this.stuffList = stuffList;
        this.myWishlistListener = myWishlistListener;
        this.ownProfile = ownProfile;
    }

    @Override
    public long getHeaderId(int position) {
/*
        if (stuffList.get(0).getIsSelected()) {
            if (position == 0) {
                return 1;
            } else if (!stuffList.get(position).getIsSelected()) {
                return 2;
            }else {
                return -1;
            }
        } else {
            if (position == 0) {
                return 1;
            } else {
                return 2;
            }
        }*/

        if (stuffList.get(0).getIsSelected()) {
            if (stuffList.get(position).getIsSelected()) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 2;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wishlist_header, parent, false);

        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        TextView tv_header = (TextView) holder.itemView.findViewById(R.id.tv_header);

        if (stuffList.get(0).getIsSelected()) {
            if (position == 0) {
                tv_header.setText(mContext.getString(R.string.my_wishlist));
            } else {
                tv_header.setText(mContext.getString(R.string.all_items));
            }
        } else {
            tv_header.setText(mContext.getString(R.string.all_items));
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_wishlist_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            final Stuff stuff = stuffList.get(position);

            myViewHolder.tv_name.setText(stuff.getTitle());

            if (ownProfile) {
                myViewHolder.cb_item.setVisibility(View.VISIBLE);
                if (stuff.getIsSelected()) {
                    myViewHolder.cb_item.setChecked(true);
                } else {
                    myViewHolder.cb_item.setChecked(false);
                }

                myViewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stuff.getIsSelected()){
                            myWishlistListener.onRemove(position);
                        }else {
                            myWishlistListener.onAdd(position);
                        }
                    }
                });

                myViewHolder.cb_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stuff.getIsSelected()){
                            myWishlistListener.onRemove(position);
                        }else {
                            myWishlistListener.onAdd(position);
                        }
                    }
                });
            }else {
                myViewHolder.cb_item.setVisibility(View.GONE);
            }



        }

    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_name;
        public CheckBox cb_item;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_my_wishlist_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_my_wishlist_item);
            cb_item = (CheckBox) itemView.findViewById(R.id.cb_my_whishlist_item);
        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_header;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            tv_header = (TextView) itemView.findViewById(R.id.tv_header);

        }

    }
}
