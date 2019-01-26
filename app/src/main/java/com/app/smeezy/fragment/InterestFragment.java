package com.app.smeezy.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.smeezy.R;
import com.app.smeezy.activity.BlockedListActivity;
import com.app.smeezy.activity.BookmarkListActivity;
import com.app.smeezy.activity.SuggestedFriendsActivity;
import com.app.smeezy.activity.WebViewActivity;
import com.app.smeezy.activity.EventsActivity;
import com.app.smeezy.activity.GroupsActivity;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterestFragment extends BaseFragment implements View.OnClickListener {


    private Context mContext;


    public InterestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interest, container, false);

        ButterKnife.bind(this, view);

        mContext = getActivity();

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.no_menu, menu);

    }

    @OnClick({R.id.tv_interest_my_events, R.id.tv_interest_my_stuff, R.id.tv_interest_my_groups,
            R.id.tv_interest_about_us, R.id.tv_interest_help, R.id.tv_interest_donate,
            R.id.tv_interest_bookmark, R.id.tv_interest_suggested_friends, R.id.tv_interest_invite_a_friend})
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_interest_my_events:

                Intent eventIntent = new Intent(mContext, EventsActivity.class);
                startActivity(eventIntent);

                break;

            case R.id.tv_interest_my_groups:

                Intent groupIntent = new Intent(mContext, GroupsActivity.class);
                startActivity(groupIntent);

                break;

            case R.id.tv_interest_about_us:

                Intent aboutUsIntent = new Intent(mContext, WebViewActivity.class);
                aboutUsIntent.putExtra("title", getString(R.string.about_smeezy));
                aboutUsIntent.putExtra("url", StaticData.ABOUT_US_URL);
                startActivity(aboutUsIntent);

                break;

            case R.id.tv_interest_help:

                Intent helpIntent = new Intent(mContext, WebViewActivity.class);
                helpIntent.putExtra("title", getString(R.string.help));
                helpIntent.putExtra("url", StaticData.HELP_URL);
                startActivity(helpIntent);

                break;

            case R.id.tv_interest_donate:

                Intent donateIntent = new Intent(mContext, WebViewActivity.class);
                donateIntent.putExtra("title", getString(R.string.donate));
                donateIntent.putExtra("url", StaticData.DONATE_URL);
                startActivity(donateIntent);

                break;

            case R.id.tv_interest_bookmark:

                Intent bookmarkIntent = new Intent(mContext, BookmarkListActivity.class);
                startActivity(bookmarkIntent);

                break;

            case R.id.tv_interest_suggested_friends:

                Intent suggestedFriendsIntent = new Intent(mContext, SuggestedFriendsActivity.class);
                startActivity(suggestedFriendsIntent);

                break;


            case R.id.tv_interest_invite_a_friend:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, PreferenceUtils.getReferralShareText(mContext));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

        }

    }
}
