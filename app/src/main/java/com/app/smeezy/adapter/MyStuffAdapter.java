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
import com.app.smeezy.activity.TradeBorrowActivity;
import com.app.smeezy.interfacess.StuffListener;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.responsemodels.UserStuff;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rahul on 03-01-2018.
 */

public class MyStuffAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MY = 1;
    private static final int TYPE_OTHER = 2;

    private Context mContext;
    private ArrayList<UserStuff> stuffList;
    private StuffListener stuffListener;
    private boolean ownProfile;

    public MyStuffAdapter(Context context, ArrayList<UserStuff> stuffList,
                          StuffListener stuffListener, boolean ownProfile) {

        this.mContext = context;
        this.stuffList = stuffList;
        this.stuffListener = stuffListener;
        this.ownProfile = ownProfile;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_MY) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.stuff_list_item, parent, false);

            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.stuff_list_item_other, parent, false);

            return new OtherViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            final UserStuff stuff = stuffList.get(position);

            myViewHolder.tv_item_name.setText(stuff.getStuffText());

            List<String> categoryList = new ArrayList<>();

            /*if (stuff.getOptions().contains("trade") && stuff.getOptions().contains("share")) {
                myViewHolder.tv_category.setText(mContext.getString(R.string.trade_and_share));
            } else*/
            if (stuff.getOptions().contains("share")) {
                categoryList.add(mContext.getString(R.string.share));
            }
            if (stuff.getOptions().contains("trade")) {
                categoryList.add(mContext.getString(R.string.trade));
            }
            if (stuff.getRent().equals("Yes")) {
                categoryList.add(mContext.getString(R.string.rent));
            }
            if (stuff.getBuy().equals("Yes")) {
                categoryList.add(mContext.getString(R.string.buy));
            }

            StringBuilder temp = new StringBuilder();

            for (String s : categoryList) {
                temp.append(String.format("%s, ", s));
            }

            myViewHolder.tv_category.setText(temp.substring(0, temp.length() - 2));

            Glide.with(mContext)
                    .load(stuff.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.hand_icon)
                    .into(myViewHolder.img_item);


            if (PreferenceUtils.getId(mContext).equals(stuff.getUserId())) {

                myViewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stuffListener.onEditStuff(position, stuff);
                    }
                });

                myViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stuffListener.onDeleteStuff(position, stuff.getId());
                    }
                });

               /* myViewHolder.ll_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        final PopupMenu menu = new PopupMenu(mContext, view);
                        menu.getMenuInflater().inflate(R.menu.edit_delete_stuff_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                if (menuItem.getItemId() == R.id.edit_stuff) {

                                    stuffListener.onEditStuff(position, stuff);

                                } else if (menuItem.getItemId() == R.id.delete_stuff) {

                                    stuffListener.onDeleteStuff(position, stuff.getId());

                                }

                                return false;
                            }
                        });

                        menu.show();


                        return false;
                    }
                });*/

            }

        } else if (holder instanceof OtherViewHolder) {


            OtherViewHolder otherViewHolder = (OtherViewHolder) holder;

            final UserStuff stuff = stuffList.get(position);

            otherViewHolder.tv_item_name.setText(stuff.getStuffText());

            List<String> categoryList = new ArrayList<>();

            /*if (stuff.getOptions().contains("trade") && stuff.getOptions().contains("share")) {
                myViewHolder.tv_category.setText(mContext.getString(R.string.trade_and_share));
            } else*/
            if (stuff.getOptions().contains("share")) {
                categoryList.add(mContext.getString(R.string.share));
            }
            if (stuff.getOptions().contains("trade")) {
                categoryList.add(mContext.getString(R.string.trade));
            }
            if (stuff.getRent().equals("Yes")) {
                categoryList.add(mContext.getString(R.string.rent));
            }
            if (stuff.getBuy().equals("Yes")) {
                categoryList.add(mContext.getString(R.string.buy));
            }

            StringBuilder temp = new StringBuilder();

            for (String s : categoryList) {
                temp.append(String.format("%s, ", s));
            }

            otherViewHolder.tv_category.setText(temp.substring(0, temp.length() - 2));

            Glide.with(mContext)
                    .load(stuff.getImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.hand_icon)
                    .into(otherViewHolder.img_item);

            otherViewHolder.img_hand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StuffFeedItem stuffFeedItem = new StuffFeedItem()
                            .withId(stuff.getId())
                            .withStuffId(stuff.getStuffId())
                            .withStuffText(stuff.getStuffText())
                            .withOptions(stuff.getOptions())
                            .withImage(stuff.getImage())
                            .withUserId(stuff.getUserId())
                            .withBuy(stuff.getBuy())
                            .withRent(stuff.getRent())
                            .withBuyPrice(stuff.getBuyPrice())
                            .withRentPrice(stuff.getRentPrice())
                            .withPaymentAccepted(stuff.getPaymentAccepted());

                    Intent intent = new Intent(mContext, TradeBorrowActivity.class);
                    intent.putExtra("stuffFeedItem", stuffFeedItem);
                    if (stuff.getOptions().contains("share")) {
                        intent.putExtra("from", "share");
                    } else {
                        intent.putExtra("from", "trade");
                    }

                    mContext.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (ownProfile) {
            return TYPE_MY;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_name, tv_category;
        public LinearLayout ll_item;
        ImageView img_item, img_edit, img_delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_stuff_item);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_stuff_item_name);
            tv_category = (TextView) itemView.findViewById(R.id.tv_stuff_item_category);
            img_item = (ImageView) itemView.findViewById(R.id.img_stuff_item);
            img_edit = (ImageView) itemView.findViewById(R.id.img_stuff_edit);
            img_delete = (ImageView) itemView.findViewById(R.id.img_stuff_delete);

        }
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_name, tv_category;
        public LinearLayout ll_item;
        ImageView img_item, img_hand;

        public OtherViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_stuff_item);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_stuff_item_name);
            tv_category = (TextView) itemView.findViewById(R.id.tv_stuff_item_category);
            img_item = (ImageView) itemView.findViewById(R.id.img_stuff_item);
            img_hand = (ImageView) itemView.findViewById(R.id.img_stuff_hand);

        }
    }
}
