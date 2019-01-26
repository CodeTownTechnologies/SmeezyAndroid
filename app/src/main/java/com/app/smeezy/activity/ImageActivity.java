package com.app.smeezy.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.app.smeezy.R;
import com.app.smeezy.adapter.ImageViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {

    @BindView(R.id.image_viewpager)
    ViewPager viewPager;

    @BindView(R.id.image_tablayout)
    TabLayout tabLayout;

    private ArrayList<String> imageUrlList = new ArrayList<>();
    private String baseUrl;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ButterKnife.bind(this);

        imageUrlList = getIntent().getStringArrayListExtra("imageUrlList");
        baseUrl = getIntent().getStringExtra("baseUrl");

        mContext = this;

        setUpToolbar();

        setUpViewpager();
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setUpViewpager() {

        ImageViewPagerAdapter viewPagerAdapter = new ImageViewPagerAdapter(mContext,
                baseUrl, imageUrlList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(imageUrlList.size());
        tabLayout.setupWithViewPager(viewPager);
        if (imageUrlList.size() == 1) {
            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
