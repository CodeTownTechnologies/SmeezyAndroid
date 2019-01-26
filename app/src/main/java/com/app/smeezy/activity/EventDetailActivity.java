package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.EventDetailViewPagerAdapter;
import com.app.smeezy.fragment.EventAboutFragment;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Event;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.smeezy.utills.StaticData.EDIT_EVENT;

public class EventDetailActivity extends BaseActivity implements IRequestView, View.OnClickListener {

    private static final String EVENT_DELETE = "event_delete";
    private static final String EVENT_LIKE = "event_like";
    private static final int COMMENT_REQUEST = 228;

    @BindView(R.id.viewpager_event_detail)
    ViewPager viewPager;

    @BindView(R.id.tab_layout_event_detail)
    TabLayout tabLayout;

    @BindView(R.id.tv_event_detail_user_count)
    TextView tv_user_count;

    @BindView(R.id.tv_event_detail_like_count)
    TextView tv_like_count;

    @BindView(R.id.tv_event_detail_comment_count)
    TextView tv_comment_count;

    @BindView(R.id.img_event_detail)
    ImageView img_event;

    @BindView(R.id.img_event_detail_like)
    ImageView img_like;

    @BindView(R.id.img_event_detail_comment)
    ImageView img_comment;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        event = (Event) intent.getSerializableExtra("event");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setData();

        setUpToolbar();

        setUpViewPager();

    }

    private void setData() {

        tv_user_count.setText(event.getGuestComing());
        tv_like_count.setText(String.valueOf(event.getTotalLikes()));
        tv_comment_count.setText(String.valueOf(event.getTotalComments()));

        if (event.getIsLikes().equals("1")) {
            img_like.setActivated(true);
        } else {
            img_like.setActivated(false);
        }

        Glide.with(mContext)
                .load(event.getImage() + StaticData.THUMB_300)
                .asBitmap()
                .into(img_event);


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
        activity_title.setText(getResources().getString(R.string.title_activity_event_detail));
    }

    private void setUpViewPager() {

        tabLayout.setupWithViewPager(viewPager);

        EventDetailViewPagerAdapter viewPagerAdapter = new EventDetailViewPagerAdapter(mContext,
                getSupportFragmentManager(), event);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

    }

    @OnClick({R.id.img_event_detail_users, R.id.img_event_detail_like, R.id.img_event_detail_comment,
            R.id.img_event_detail})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_event_detail_users:

                Intent intent = new Intent(mContext, EventGuestListActivity.class);
                intent.putExtra("eventId", event.getId());
                startActivity(intent);

                break;

            case R.id.img_event_detail_like:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.likeUnlikePost(EVENT_LIKE, PreferenceUtils.getId(mContext),
                            event.getActivityId(), event.getId());
                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;

            case R.id.img_event_detail_comment:

                Intent intent1 = new Intent(mContext, CommentActivity.class);
                FeedItem feedItem = new FeedItem().withId(event.getId())
                        .withActivityId(event.getActivityId())
                        .withTotalComments(event.getTotalComments());
                intent1.putExtra("feedItem", feedItem);
                startActivityForResult(intent1, COMMENT_REQUEST);

                break;

            case R.id.img_event_detail:

                Intent imageIntent = new Intent(mContext, ImageActivity.class);
                ArrayList<String> imageUrlList = new ArrayList<>();
                imageUrlList.add(event.getImage());
                imageIntent.putStringArrayListExtra("imageUrlList", imageUrlList);
                imageIntent.putExtra("baseUrl", "");
                startActivity(imageIntent);

                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (event.getUserId().equals(PreferenceUtils.getId(mContext))) {
            getMenuInflater().inflate(R.menu.activity_event_detail, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete", false);
                resultIntent.putExtra("event", event);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                break;

            case R.id.edit_event:

                Intent intent = new Intent(mContext, EditEventActivity.class);
                intent.putExtra("event", event);
                startActivityForResult(intent, StaticData.EDIT_EVENT);

                break;

            case R.id.invite:

                Intent intent1 = new Intent(mContext, InviteActivity.class);
                intent1.putExtra("event", event);
                startActivity(intent1);

                break;


            case R.id.delete_event:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.deleteEvent(EVENT_DELETE, PreferenceUtils.getId(mContext), event.getId());
                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("delete", false);
        resultIntent.putExtra("event", event);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case EDIT_EVENT:

                if (resultCode == Activity.RESULT_OK) {

                    if (data.getSerializableExtra("event") != null) {
                        event = (Event) data.getSerializableExtra("event");

                        setData();

                        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                        for (Fragment fragment : fragmentList) {

                            if (fragment instanceof EventAboutFragment) {
                                ((EventAboutFragment) fragment).setData(event);
                            }

                        }
                    }
                }

                break;

            case COMMENT_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    int totalComments = data.getIntExtra("totalComments", event.getTotalComments());

                    event.setTotalComments(totalComments);
                    tv_comment_count.setText(String.valueOf(event.getTotalComments()));

                }


                break;

        }
    }

    @Override
    public void showLoadingProgressBar() {
        startProgressBar();
    }

    @Override
    public void hideLoadingProgressBar() {
        dismissProgressBar();
    }

    @Override
    public void Failed(String method, String message) {

        switch (method) {

            case EVENT_DELETE:

                showAlert(getString(R.string.app_name), message);

                break;

            case EVENT_LIKE:

                showAlert(getString(R.string.app_name), message);

                break;

        }

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case EVENT_DELETE:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete", true);
                resultIntent.putExtra("event", event);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                break;

            case EVENT_LIKE:

                if (event.getIsLikes().equals("1")) {
                    event.setIsLikes("0");
                    event.setTotalLikes(event.getTotalLikes() - 1);
                    img_like.setActivated(false);
                } else {
                    event.setIsLikes("1");
                    event.setTotalLikes(event.getTotalLikes() + 1);
                    img_like.setActivated(true);
                }

                tv_like_count.setText(String.valueOf(event.getTotalLikes()));


                break;


        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
