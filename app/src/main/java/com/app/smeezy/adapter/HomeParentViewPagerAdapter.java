package com.app.smeezy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.smeezy.fragment.AllItemsFeedFragment;
import com.app.smeezy.fragment.HomeFragment;
import com.app.smeezy.fragment.ShareFeedFragment;
import com.app.smeezy.fragment.TradeFeedFragment;

/**
 * Created by Rahul on 15-01-2018.
 */

public class HomeParentViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomeParentViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch (position) {

            case 0:

                fragment = new AllItemsFeedFragment();
                fragment.setArguments(bundle);

                break;

            case 1:

                fragment = new TradeFeedFragment();
                fragment.setArguments(bundle);

                break;

            case 2:

                fragment = new ShareFeedFragment();
                fragment.setArguments(bundle);

                break;

            case 3:

                fragment = new HomeFragment();
                bundle.putString("postPrivacy", "community");
                fragment.setArguments(bundle);

                break;

            case 4:

                fragment = new HomeFragment();
                bundle.putString("postPrivacy", "friends");
                fragment.setArguments(bundle);

                break;

        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
