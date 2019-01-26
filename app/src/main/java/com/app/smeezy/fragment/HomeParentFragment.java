package com.app.smeezy.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.PostActivity;
import com.app.smeezy.adapter.HomeParentViewPagerAdapter;
import com.app.smeezy.utills.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeParentFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.home_parent_viewpager)
    ViewPager viewPager;

    @BindView(R.id.home_parent_tablayout)
    TabLayout tabLayout;

    @BindView(R.id.ll_home_parent_top_bar)
    LinearLayout ll_top_bar;

    @BindView(R.id.img_trade)
    ImageView img_trade;

    @BindView(R.id.img_donate)
    ImageView img_donate;

    @BindView(R.id.img_borrow)
    ImageView img_borrow;

    @BindView(R.id.img_share)
    ImageView img_share;

    @BindView(R.id.tv_trade)
    TextView tv_trade;

    @BindView(R.id.tv_donate)
    TextView tv_donate;

    @BindView(R.id.tv_borrow)
    TextView tv_borrow;

    @BindView(R.id.tv_share)
    TextView tv_share;

    private Context mContext;
    ImageView img_tab0, img_tab1, img_tab2, img_tab3, img_tab4;

    public HomeParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_parent, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();

        setUpViewPager();

        img_trade.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_trade.setPressed(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    tv_trade.setPressed(false);
                }
                return false;
            }

        });


        img_donate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_donate.setPressed(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    tv_donate.setPressed(false);
                }
                return false;
            }

        });

        img_borrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_borrow.setPressed(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    tv_borrow.setPressed(false);
                }
                return false;
            }

        });

        img_share.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_share.setPressed(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    tv_share.setPressed(false);
                }
                return false;
            }

        });


        return view;
    }

    private void setUpViewPager() {
        final HomeParentViewPagerAdapter viewPagerAdapter = new HomeParentViewPagerAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        tabLayout.setupWithViewPager(viewPager);
        createTabs();

        if (img_tab0 != null){
            img_tab0.setActivated(true);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*if (tab.getPosition() == 0){
                    ll_top_bar.setVisibility(View.VISIBLE);
                }else {
                    ll_top_bar.setVisibility(View.GONE);
                }*/

                switch (tab.getPosition()) {

                    case 0:

                        if (img_tab0 != null){
                            img_tab0.setActivated(true);
                        }

                        if (img_tab1 != null) {
                            img_tab1.setActivated(false);
                        }

                        if (img_tab2 != null){
                            img_tab2.setActivated(false);
                        }

                        if (img_tab3 != null){
                            img_tab3.setActivated(false);
                        }

                        if (img_tab4 != null){
                            img_tab4.setActivated(false);
                        }

                        break;

                    case 1:

                        if (img_tab0 != null){
                            img_tab0.setActivated(false);
                        }

                        if (img_tab1 != null) {
                            img_tab1.setActivated(true);
                        }

                        if (img_tab2 != null){
                            img_tab2.setActivated(false);
                        }

                        if (img_tab3 != null){
                            img_tab3.setActivated(false);
                        }

                        if (img_tab4 != null){
                            img_tab4.setActivated(false);
                        }


                        break;


                    case 2:
                        if (img_tab0 != null){
                            img_tab0.setActivated(false);
                        }

                        if (img_tab1 != null) {
                            img_tab1.setActivated(false);
                        }

                        if (img_tab2 != null){
                            img_tab2.setActivated(true);
                        }

                        if (img_tab3 != null){
                            img_tab3.setActivated(false);
                        }

                        if (img_tab4 != null){
                            img_tab4.setActivated(false);
                        }

                        break;

                    case 3:

                        if (img_tab0 != null){
                            img_tab0.setActivated(false);
                        }

                        if (img_tab1 != null) {
                            img_tab1.setActivated(false);
                        }

                        if (img_tab2 != null){
                            img_tab2.setActivated(false);
                        }

                        if (img_tab3 != null){
                            img_tab3.setActivated(true);
                        }

                        if (img_tab4 != null){
                            img_tab4.setActivated(false);
                        }

                        break;

                    case 4:

                        if (img_tab0 != null){
                            img_tab0.setActivated(false);
                        }

                        if (img_tab1 != null) {
                            img_tab1.setActivated(false);
                        }

                        if (img_tab2 != null){
                            img_tab2.setActivated(false);
                        }

                        if (img_tab3 != null){
                            img_tab3.setActivated(false);
                        }

                        if (img_tab4 != null){
                            img_tab4.setActivated(true);
                        }

                        break;
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

        LinearLayout tabZero = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_home_parent_tab, null);
        TextView tv_heading0 = (TextView) tabZero.findViewById(R.id.tab_heading);
        TextView tv_sub_heading0 = (TextView) tabZero.findViewById(R.id.tab_sub_heading);
        img_tab0 = (ImageView) tabZero.findViewById(R.id.img_tab);
        tv_heading0.setText(getString(R.string.all_items));
        //tv_sub_heading1.setText(getString(R.string.public_post));
        tv_sub_heading0.setVisibility(View.GONE);
        img_tab0.setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(tabZero);

        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_home_parent_tab, null);
        TextView tv_heading1 = (TextView) tabOne.findViewById(R.id.tab_heading);
        TextView tv_sub_heading1 = (TextView) tabOne.findViewById(R.id.tab_sub_heading);
        img_tab1 = (ImageView) tabOne.findViewById(R.id.img_tab);
        tv_heading1.setText(getString(R.string.trade));
        //tv_sub_heading1.setText(getString(R.string.public_post));
        tv_sub_heading1.setVisibility(View.GONE);
        img_tab1.setImageResource(R.drawable.selector_trade_icon);
        tabLayout.getTabAt(1).setCustomView(tabOne);


        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_home_parent_tab, null);
        TextView tv_heading2 = (TextView) tabTwo.findViewById(R.id.tab_heading);
        TextView tv_sub_heading2 = (TextView) tabTwo.findViewById(R.id.tab_sub_heading);
        img_tab2 = (ImageView) tabTwo.findViewById(R.id.img_tab);
        tv_heading2.setText(getString(R.string.borrow));
        //tv_sub_heading2.setText(getString(R.string.private_post));
        tv_sub_heading2.setVisibility(View.GONE);
        img_tab2.setImageResource(R.drawable.selector_borrow_icon);
        img_tab2.setPadding(0, 0, 5, 5);
        tabLayout.getTabAt(2).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_home_parent_tab, null);
        TextView tv_heading3 = (TextView) tabThree.findViewById(R.id.tab_heading);
        TextView tv_sub_heading3 = (TextView) tabThree.findViewById(R.id.tab_sub_heading);
        img_tab3 = (ImageView) tabThree.findViewById(R.id.img_tab);
        tv_heading3.setText(getString(R.string.my_community));
        tv_sub_heading3.setText(getString(R.string.public_post));
        img_tab3.setImageResource(R.drawable.selector_my_community_icon);
        tabLayout.getTabAt(3).setCustomView(tabThree);

        LinearLayout tabFour = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_home_parent_tab, null);
        TextView tv_heading4 = (TextView) tabFour.findViewById(R.id.tab_heading);
        TextView tv_sub_heading4 = (TextView) tabFour.findViewById(R.id.tab_sub_heading);
        img_tab4 = (ImageView) tabFour.findViewById(R.id.img_tab);
        tv_heading4.setText(getString(R.string.share_with_friends));
        tv_sub_heading4.setText(getString(R.string.private_post));
        img_tab4.setImageResource(R.drawable.selector_share_friends_icon);
        tabLayout.getTabAt(4).setCustomView(tabFour);

    }

    @OnClick({R.id.img_trade, R.id.img_donate, R.id.img_borrow, R.id.img_share})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_trade:

                /*Intent postIntent = new Intent(mContext, PostActivity.class);
                postIntent.putExtra("groupPost", false);
                postIntent.putExtra("postPrivacy", "community");
                startActivity(postIntent);*/

                viewPager.setCurrentItem(0, true);

                break;

            case R.id.img_donate:

                Intent post1Intent = new Intent(mContext, PostActivity.class);
                post1Intent.putExtra("groupPost", false);
                post1Intent.putExtra("postPrivacy", "community");
                startActivity(post1Intent);

                break;


            case R.id.img_borrow:

                /*Intent searchIntent = new Intent(mContext, SearchActivity.class);
                startActivity(searchIntent);*/
                viewPager.setCurrentItem(1, true);

                break;

            case R.id.img_share:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, PreferenceUtils.getReferralShareText(mContext));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

        }

    }
}
