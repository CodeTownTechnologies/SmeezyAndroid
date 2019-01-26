package com.app.smeezy.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.MapsActivity;
import com.app.smeezy.activity.StuffDetailActivity;
import com.app.smeezy.activity.TradeBorrowActivity;
import com.app.smeezy.interfacess.TradeFeedListener;
import com.app.smeezy.responsemodels.MenuType;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class TradeFeedAdapter extends RecyclerView.Adapter {

    public static final int TYPE_FEED = 1;
    public static final int TYPE_LOADING = 2;

    private Context mContext;
    private ArrayList<StuffFeedItem> feedList;
    private TradeFeedListener feedListener;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading = true;
    private boolean noMoreFeedItems;

    public TradeFeedAdapter(Context context, RecyclerView recyclerView, ArrayList<StuffFeedItem> feedList,
                            final TradeFeedListener feedListener) {

        this.mContext = context;
        this.feedList = feedList;
        this.feedListener = feedListener;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!noMoreFeedItems && !isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                    isLoading = true;
                    if (feedListener != null) {
                        feedListener.onBottomReached();
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_item, parent, false);
            return new LoadingHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.stuff_feed_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            final StuffFeedItem feedItem = feedList.get(position);

            viewHolder.tv_category.setText(feedItem.getCategoryName());
            viewHolder.tv_name.setText(feedItem.getStuffText());
            viewHolder.tv_distance.setText(String.format("%s %s", feedItem.getDistanceDisplay(),
                    mContext.getString(R.string.mi)));


            Glide.with(mContext)
                    .load(feedItem.getImage() + StaticData.THUMB_200)
                    .asBitmap()
                    .into(viewHolder.img_stuff);

           /* if (feedItem.getUserId().equals(PreferenceUtils.getId(mContext))){
                viewHolder.img_option.setVisibility(View.GONE);
            }else {
                viewHolder.img_option.setVisibility(View.VISIBLE);
            }

            viewHolder.img_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedListener.onOfferTrade(feedItem);
                }
            });*/

            viewHolder.img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);

                    final ArrayList<MenuType> menuList = getMenuList(feedItem);

                    listPopupWindow.setWidth(400);

                    FeedOptionAdapter feedOptionAdapter = new FeedOptionAdapter(mContext,
                            menuList);

                    listPopupWindow.setAdapter(feedOptionAdapter);
                    listPopupWindow.setAnchorView(viewHolder.img_more);


                    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                            if (listPopupWindow.isShowing()){
                                listPopupWindow.dismiss();
                            }
                            onOptionClick((int)id, position, feedItem);

                        }
                    });

                    listPopupWindow.show();
                }
            });

           /* viewHolder.img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu menu = new PopupMenu(mContext, view);
                    menu.getMenuInflater().inflate(R.menu.trade_borrow_option_menu, menu.getMenu());

                    if (feedItem.getIsBookmark().equals("1")){
                        menu.getMenu().removeItem(R.id.bookmark);
                    }else {
                        menu.getMenu().removeItem(R.id.remove_bookmark);
                    }

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                         onOptionClick(menuItem.getItemId(), position, feedItem);

                            return false;
                        }
                    });

                    Object menuHelper;
                    Class[] argTypes;
                    try {
                        Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                        fMenuHelper.setAccessible(true);
                        menuHelper = fMenuHelper.get(menu);
                        argTypes = new Class[]{boolean.class};
                        menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    menu.show();
                }
            });*/

            viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, StuffDetailActivity.class);
                    intent.putExtra("stuffFeedItem", feedItem);
                    intent.putExtra("from", "trade");
                    mContext.startActivity(intent);


                }
            });

            viewHolder.tv_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent mapIntent = new Intent(mContext, MapsActivity.class);
                        mapIntent.putExtra("latitude", feedItem.getLocationLatitude());
                        mapIntent.putExtra("longitude", feedItem.getLocationLongitude());
                        mContext.startActivity(mapIntent);

                }
            });

            if (feedItem.getBuy().equals("Yes")) {
                viewHolder.ll_buy.setVisibility(View.VISIBLE);
                viewHolder.btn_buy.setText(String.format("%s %s", mContext.getString(R.string.buy),
                        Utills.moneyConvert(feedItem.getBuyPrice())));
                //viewHolder.tv_buy_price.setText(Utills.moneyConvert(feedItem.getBuyPrice()));
                viewHolder.btn_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TradeBorrowActivity.class);
                        intent.putExtra("stuffFeedItem", feedItem);
                        intent.putExtra("from", "buy");
                        mContext.startActivity(intent);
                    }
                });
            } else {
                viewHolder.ll_buy.setVisibility(View.GONE);
            }

            if (feedItem.getRent().equals("Yes")) {
                viewHolder.ll_rent.setVisibility(View.VISIBLE);
                viewHolder.btn_rent.setText(String.format("%s %s", mContext.getString(R.string.rent),
                        Utills.moneyConvert(feedItem.getRentPrice())));
                //viewHolder.tv_rent_price.setText(Utills.moneyConvert(feedItem.getRentPrice()));
                viewHolder.btn_rent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TradeBorrowActivity.class);
                        intent.putExtra("stuffFeedItem", feedItem);
                        intent.putExtra("from", "rent");
                        mContext.startActivity(intent);
                    }
                });
            } else {
                viewHolder.ll_rent.setVisibility(View.GONE);
            }


            if (feedItem.getOptions().contains("share")){
                viewHolder.ll_borrow.setVisibility(View.VISIBLE);
                viewHolder.btn_borrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TradeBorrowActivity.class);
                        intent.putExtra("stuffFeedItem", feedItem);
                        intent.putExtra("from", "share");
                        mContext.startActivity(intent);
                    }
                });
            }else {
                viewHolder.ll_borrow.setVisibility(View.GONE);
            }


            if (feedItem.getOptions().contains("trade")){
                viewHolder.ll_trade.setVisibility(View.VISIBLE);
                viewHolder.btn_trade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TradeBorrowActivity.class);
                        intent.putExtra("stuffFeedItem", feedItem);
                        intent.putExtra("from", "trade");
                        mContext.startActivity(intent);
                    }
                });
            }else {
                viewHolder.ll_trade.setVisibility(View.GONE);
            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (feedList.size() == position && isLoading) {
            return TYPE_LOADING;
        }


        return TYPE_FEED;
    }

    @Override
    public int getItemCount() {

        if (isLoading && !noMoreFeedItems){
            return feedList.size() + 1;
        }

        return feedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_category, tv_distance, tv_buy_price, tv_rent_price;
        public ImageView img_stuff, img_option, img_more;
        public LinearLayout ll_item, ll_buy, ll_rent, ll_borrow, ll_trade;
        public Button btn_buy, btn_rent, btn_borrow, btn_trade;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_stuff_feed_item);
            tv_name = (TextView) itemView.findViewById(R.id.tv_stuff_feed_name);
            tv_category = (TextView) itemView.findViewById(R.id.tv_stuff_feed_category);
            img_stuff = (ImageView) itemView.findViewById(R.id.img_stuff_feed);
            img_option = (ImageView) itemView.findViewById(R.id.img_stuff_feed_option);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_stuff_feed_distance);
            tv_buy_price = (TextView) itemView.findViewById(R.id.tv_stuff_feed_buy_price);
            tv_rent_price = (TextView) itemView.findViewById(R.id.tv_stuff_feed_rent_price);
            ll_buy = (LinearLayout) itemView.findViewById(R.id.ll_stuff_feed_buy);
            ll_rent = (LinearLayout) itemView.findViewById(R.id.ll_stuff_feed_rent);
            btn_buy = (Button) itemView.findViewById(R.id.btn_stuff_feed_buy);
            btn_rent = (Button) itemView.findViewById(R.id.btn_stuff_feed_rent);
            ll_borrow = (LinearLayout) itemView.findViewById(R.id.ll_stuff_feed_borrow);
            ll_trade = (LinearLayout) itemView.findViewById(R.id.ll_stuff_feed_trade);
            btn_borrow = (Button) itemView.findViewById(R.id.btn_stuff_feed_borrow);
            btn_trade = (Button) itemView.findViewById(R.id.btn_stuff_feed_trade);


        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

        }
    }

    public void setLoaded(){
        isLoading = false;
    }

    public void stopLoader(){
        noMoreFeedItems = true;
    }

    private void onOptionClick(int id, int position, StuffFeedItem stuffFeedItem){

        switch (id){

            case  StaticData.UNFOLLOW_USER:

                feedListener.onUnfollowUser(position, stuffFeedItem);


                break;

            case StaticData.CHANGE_CATEGORY:

                feedListener.onChangeCategory(position, stuffFeedItem);

                break;

            case StaticData.BOOKMARK:

                feedListener.onBookmark(position, stuffFeedItem);

                break;

            case StaticData.REMOVE_BOOKMARK:

                feedListener.onRemoveBookmark(position, stuffFeedItem);

                break;

            case StaticData.REPORT:

                feedListener.onReportPost(position, stuffFeedItem);

                break;

        }


    }

    private ArrayList<MenuType> getMenuList(StuffFeedItem feedItem){

        ArrayList<MenuType> tempList = new ArrayList<>();

        if (feedItem.getIsBookmark().equals("1")){
            tempList.add(new MenuType(StaticData.REMOVE_BOOKMARK,
                    mContext.getString(R.string.remove_bookmark),
                    mContext.getString(R.string.remove_bookmark_desc),
                    R.drawable.remove_bookmarrk));
        }else {
            tempList.add(new MenuType(StaticData.BOOKMARK,
                    mContext.getString(R.string.bookmark_item),
                    mContext.getString(R.string.bookmark_desc),
                    R.drawable.bookmark_icon));
        }

        tempList.add(new MenuType(StaticData.CHANGE_CATEGORY,
                mContext.getString(R.string.change_category),
                mContext.getString(R.string.change_category_desc),
                R.drawable.change_category));

        tempList.add(new MenuType(StaticData.REPORT,
                mContext.getString(R.string.give_feedback),
                mContext.getString(R.string.give_feedback_desc),
                R.drawable.report_flag));

        tempList.add(new MenuType(StaticData.UNFOLLOW_USER,
                mContext.getString(R.string.unfollow_user),
                mContext.getString(R.string.unfollow_user_desc),
                R.drawable.unfollow_user));



        return tempList;
    }

}
