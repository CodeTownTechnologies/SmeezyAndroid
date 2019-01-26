package com.app.smeezy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.responsemodels.MenuType;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FeedOptionAdapter extends BaseAdapter {


    private ArrayList<MenuType> menuList;
    private Context mContext;

    public FeedOptionAdapter(Context context, ArrayList<MenuType> menuList) {
        this.menuList = menuList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.feed_option_item,
                    parent, false);
        }

        TextView tv_name = convertView.findViewById(R.id.tv_menu_name);
        TextView tv_desc = convertView.findViewById(R.id.tv_menu_desc);
        ImageView img = convertView.findViewById(R.id.img_option);



        tv_name.setText(menuList.get(position).getName());
        tv_desc.setText(menuList.get(position).getDescription());

        Glide.with(mContext)
                .load(menuList.get(position).getImage())
                .asBitmap()
                .into(img);

        return convertView;
    }
}
