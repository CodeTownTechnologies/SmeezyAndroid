package com.app.smeezy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.EventsViewPagerAdapter;
import com.app.smeezy.fragment.DiscoverEventFragment;
import com.app.smeezy.fragment.PastEventFragment;
import com.app.smeezy.fragment.UpcomingEventFragment;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsActivity extends BaseActivity {

    @BindView(R.id.viewpager_event)
    ViewPager viewPager;

    @BindView(R.id.tab_layout_event)
    TabLayout tabLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ButterKnife.bind(this);

        mContext = this;

        setUpToolbar();

        setUpViewPager();
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

        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        Glide.with(mContext)
                .load(PreferenceUtils.getLogoUrl(mContext))
                .asBitmap()
                .into(img_logo);

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_events));
    }

    private void setUpViewPager() {

        tabLayout.setupWithViewPager(viewPager);

        EventsViewPagerAdapter viewPagerAdapter = new EventsViewPagerAdapter(mContext,
                getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case StaticData.EVENT_DETAIL:

                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragmentList) {

                    if (viewPager.getCurrentItem() == 0 && fragment instanceof UpcomingEventFragment) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    } else if (viewPager.getCurrentItem() == 1 && fragment instanceof PastEventFragment) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    } else if (viewPager.getCurrentItem() == 2 && fragment instanceof DiscoverEventFragment) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }

                break;

            case StaticData.CREATE_EVENT:

                List<Fragment> fragmentList1 = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragmentList1) {

                    if (viewPager.getCurrentItem() == 0 && fragment instanceof UpcomingEventFragment) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }

                break;
        }

    }
}
