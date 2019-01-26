package com.app.smeezy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.smeezy.R;
import com.app.smeezy.fragment.ChatListFragment;
import com.app.smeezy.fragment.EventRequestsFragment;
import com.app.smeezy.fragment.FriendRequestsFragment;
import com.app.smeezy.fragment.PrivateChatListFragment;

/**
 * Created by Rahul on 15-01-2018.
 */

public class ChatListParentViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public ChatListParentViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;


        switch (position) {

            case 0:

                fragment = new ChatListFragment();

                break;

            case 1:

                fragment = new PrivateChatListFragment();

                break;

        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
