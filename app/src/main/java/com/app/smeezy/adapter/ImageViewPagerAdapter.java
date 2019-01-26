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
import android.widget.ProgressBar;

import com.app.smeezy.R;
import com.app.smeezy.activity.ImageActivity;
import com.app.smeezy.utills.StaticData;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Rahul on 15-01-2018.
 */

public class ImageViewPagerAdapter extends PagerAdapter {


    private Context mContext;
    private ArrayList<String> imageUrlList;
    private String baseUrl;


    public ImageViewPagerAdapter(Context context, String baseUrl, ArrayList<String> imageUrlList) {
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.image_viewpager_image_item, container, false);

        ImageView view_image = view.findViewById(R.id.img_list_image);

        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(String.format("%s%s", baseUrl, imageUrlList.get(position)))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(view_image);

        view_image.setOnTouchListener(new ImageMatrixTouchHandler(mContext));

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
