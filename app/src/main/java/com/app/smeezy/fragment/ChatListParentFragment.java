package com.app.smeezy.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.ChatListParentViewPagerAdapter;
import com.app.smeezy.utills.StaticData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListParentFragment extends Fragment {


    @BindView(R.id.chat_list_parent_viewpager)
    ViewPager viewPager;

    @BindView(R.id.chat_list_parent_tablayout)
    TabLayout tabLayout;

    private Context mContext;
    private TextView tv_message_count, tv_private_message_count;


    private BroadcastReceiver chatCountRefreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            setChatCount(intent.getStringExtra("messageCount"),
                    intent.getStringExtra("privateMessageCount"));

        }
    };


    public ChatListParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list_parent, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();

        setUpViewPager();

        if (getArguments() != null){
            String messageCount = getArguments().getString("messageCount");
            String privateMessageCount = getArguments().getString("privateMessageCount");

            setChatCount(messageCount, privateMessageCount);


        }

        return view;
    }

    private void setUpViewPager() {
        ChatListParentViewPagerAdapter viewPagerAdapter = new ChatListParentViewPagerAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);

        createTabs();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setHasOptionsMenu(true);
                } else {
                    setHasOptionsMenu(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createTabs() {

        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_chat_tab, null);
        TextView tv_heading1 = (TextView) tabOne.findViewById(R.id.tab_heading);
        tv_heading1.setText(getString(R.string.chat));
        tv_message_count = (TextView) tabOne.findViewById(R.id.tv_chat_count);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        RelativeLayout tabTwo = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_chat_tab, null);
        TextView tv_heading2 = (TextView) tabTwo.findViewById(R.id.tab_heading);
        tv_heading2.setText(getString(R.string.private_chat));
        tv_private_message_count = (TextView) tabTwo.findViewById(R.id.tv_chat_count);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    @Override
    public void onResume() {
        super.onResume();

        mContext.registerReceiver(chatCountRefreshBroadcast, new IntentFilter(StaticData.CHAT_COUNT_REFRESH_BROADCAST));


    }

    @Override
    public void onPause() {
        super.onPause();

        mContext.unregisterReceiver(chatCountRefreshBroadcast);
    }

    private void setChatCount(String messageCount, String privateMessageCount){

        if (messageCount.equals("0")){
            tv_message_count.setVisibility(View.GONE);
        }else {
            tv_message_count.setText(messageCount);
            tv_message_count.setVisibility(View.VISIBLE);
        }

        if (privateMessageCount.equals("0")){
            tv_private_message_count.setVisibility(View.GONE);
        }else {
            tv_private_message_count.setText(privateMessageCount);
            tv_private_message_count.setVisibility(View.VISIBLE);
        }

    }
}
