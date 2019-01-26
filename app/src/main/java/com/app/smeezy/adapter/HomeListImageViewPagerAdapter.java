package com.app.smeezy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.smeezy.R;
import com.app.smeezy.activity.ImageActivity;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 15-01-2018.
 */

public class HomeListImageViewPagerAdapter extends PagerAdapter {


    private Context mContext;
    private ArrayList<String> imageUrlList;
    private String baseUrl;


    public HomeListImageViewPagerAdapter(Context context, String baseUrl, ArrayList<String> imageUrlList) {
        this.mContext = context;
        this.imageUrlList = imageUrlList;
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_viewpager_image_item, container, false);

        ImageView view_image = view.findViewById(R.id.img_home_list_image);
        Glide.with(mContext)
                .load(String.format("%s%s%s", baseUrl, imageUrlList.get(position), StaticData.THUMB_300))
                .asBitmap()
                .into(view_image);

        view_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putStringArrayListExtra("imageUrlList", imageUrlList);
                intent.putExtra("baseUrl", baseUrl);
                mContext.startActivity(intent);
            }
        });

        ((ViewPager) container).addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ViewGroup)object);
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }


}
