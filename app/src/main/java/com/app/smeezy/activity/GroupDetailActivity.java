package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.GroupDetailViewPagerAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Group;
import com.app.smeezy.responsemodels.JoinRequestStatus;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.smeezy.utills.StaticData.EDIT_GROUP;

public class GroupDetailActivity extends BaseActivity implements IRequestView, View.OnClickListener {

    //private static final String GROUP_DETAIL = "group_detail";
    private static final String GROUP_DELETE = "group_delete";
    private static final String PUBLIC_JOIN = "public_join";
    private static final String PRIVATE_JOIN = "private_join";
    private static final String CANCEL_PRIVATE_JOIN_REQUEST = "cancel_private_join_request";
    private static final String LEAVE = "leave";

    @BindView(R.id.viewpager_group_detail)
    ViewPager viewPager;

    @BindView(R.id.tab_layout_group_detail)
    TabLayout tabLayout;

    @BindView(R.id.tv_group_detail_user_count)
    TextView tv_user_count;

    @BindView(R.id.tv_group_detail_like_count)
    TextView tv_like_count;

    @BindView(R.id.tv_group_detail_comment_count)
    TextView tv_comment_count;

    @BindView(R.id.img_group_detail)
    ImageView img_group;

    @BindView(R.id.tv_group_detail_name)
    TextView tv_name;

    @BindView(R.id.tv_group_detail_desc)
    TextView tv_desc;

    @BindView(R.id.ll_group_requests)
    LinearLayout ll_group_requests;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private Toolbar toolbar;

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        group = (Group) intent.getSerializableExtra("group");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setData();

        setUpToolbar();

        if (group.getType().equals("public") || !group.getGroupRole().isEmpty()) {
            setUpViewPager();
        }

        if (group.getGroupRole().equals("admin") && group.getType().equals("private")) {
            ll_group_requests.setVisibility(View.VISIBLE);
        } else {
            ll_group_requests.setVisibility(View.GONE);
        }

        //getGroupDetail();

    }

    /*private void getGroupDetail() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getGroupDetail(GROUP_DETAIL, PreferenceUtils.getId(mContext), group.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }

    }*/

    private void setData() {

        tv_user_count.setText(group.getTotalMember());
        tv_like_count.setText(String.valueOf(group.getTotalLikes()));
        tv_comment_count.setText(String.valueOf(group.getTotalComments()));

        tv_name.setText(group.getTitle());
        tv_desc.setText(group.getDescription());

        Glide.with(mContext)
                .load(group.getImage() + StaticData.THUMB_300)
                .asBitmap()
                .into(img_group);

    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        activity_title.setText(getResources().getString(R.string.title_activity_group_detail));
    }

    private void setUpViewPager() {

        tabLayout.setupWithViewPager(viewPager);

        GroupDetailViewPagerAdapter viewPagerAdapter = new GroupDetailViewPagerAdapter(mContext,
                getSupportFragmentManager(), group);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

    }

    @OnClick({R.id.img_group_detail_users, R.id.img_group_detail_requests, R.id.img_group_detail})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_group_detail_users:

                Intent intent = new Intent(mContext, GroupMemberListActivity.class);
                intent.putExtra("groupId", group.getId());
                startActivity(intent);

                break;

            case R.id.img_group_detail_requests:

                if (group.getGroupRole().equals("admin")) {
                    Intent intent2 = new Intent(mContext, GroupRequestsActivity.class);
                    intent2.putExtra("groupId", group.getId());
                    startActivity(intent2);
                }

                break;

            case R.id.img_group_detail:

                Intent imageIntent = new Intent(mContext, ImageActivity.class);
                ArrayList<String> imageUrlList = new ArrayList<>();
                imageUrlList.add(group.getImage());
                imageIntent.putStringArrayListExtra("imageUrlList", imageUrlList);
                imageIntent.putExtra("baseUrl", "");
                startActivity(imageIntent);

                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (group.getGroupRole().equals("admin")) {
            getMenuInflater().inflate(R.menu.activity_group_detail_own, menu);
        } else if (group.getGroupRole().isEmpty()) {
            if (group.getType().equals("private")) {
                getMenuInflater().inflate(R.menu.activity_group_detail_private, menu);
                if (group.getJoinRequestStatus() == null) {
                    menu.removeItem(R.id.cancel_join_request);
                } else if (group.getJoinRequestStatus().getRequestStatus().equals("Reject")){
                    menu.removeItem(R.id.cancel_join_request);
                    menu.removeItem(R.id.join_private_group);
                } else {
                    menu.removeItem(R.id.join_private_group);
                }
            } else {
                getMenuInflater().inflate(R.menu.activity_group_detail_public, menu);
                menu.removeItem(R.id.leave_group);
            }
        } else {
            getMenuInflater().inflate(R.menu.activity_group_detail_public, menu);
            menu.removeItem(R.id.join_group);
        }

        /*else if (group.getType().equals("private")) {
            if (group.getGroupRole().isEmpty()) {
                getMenuInflater().inflate(R.menu.activity_group_detail_private, menu);
                if (group.getJoinRequestStatus() == null ||
                        group.getJoinRequestStatus().equals("reject")) {
                    menu.removeItem(R.id.cancel_join_request);
                } else {
                    menu.removeItem(R.id.join_group_request);
                }
            } else {
                getMenuInflater().inflate(R.menu.activity_group_detail_public, menu);
                menu.removeItem(R.id.join_group);
            }
        } else {
            getMenuInflater().inflate(R.menu.activity_group_detail_public, menu);
            if (group.getGroupRole().isEmpty()) {
                menu.removeItem(R.id.leave_group);
            } else {
                menu.removeItem(R.id.join_group);
            }
        }*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete", false);
                resultIntent.putExtra("group", group);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                break;

            case R.id.edit_group:

                Intent intent = new Intent(mContext, EditGroupActivity.class);
                intent.putExtra("group", group);
                startActivityForResult(intent, StaticData.EDIT_GROUP);

                break;


            case R.id.delete_group:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.deleteGroup(GROUP_DELETE, PreferenceUtils.getId(mContext), group.getId());
                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;

            case R.id.join_group:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.performGroupAction(PUBLIC_JOIN, PreferenceUtils.getId(mContext),
                            group.getId(), "join");
                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;

            case R.id.leave_group:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.performGroupAction(LEAVE, PreferenceUtils.getId(mContext),
                            group.getId(), "leave");
                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;

            case R.id.join_private_group:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.performGroupAction(PRIVATE_JOIN, PreferenceUtils.getId(mContext),
                            group.getId(), "join-request");
                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;

            case R.id.cancel_join_request:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.performGroupAction(CANCEL_PRIVATE_JOIN_REQUEST, PreferenceUtils.getId(mContext),
                            group.getId(), "cancel-request");
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
        resultIntent.putExtra("group", group);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case EDIT_GROUP:

                if (resultCode == Activity.RESULT_OK) {

                    if (data.getSerializableExtra("group") != null) {
                        group = (Group) data.getSerializableExtra("group");

                        setData();

                        /*List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                        for (Fragment fragment : fragmentList) {

                            if (fragment instanceof GroupAboutFragment) {
                                ((GroupAboutFragment) fragment).setData(group);
                            }

                        }*/
                    }
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

            case GROUP_DELETE:

                showAlert(getString(R.string.app_name), message);

                break;

            case PUBLIC_JOIN:

                showAlert(getString(R.string.app_name), message);

                break;


            case LEAVE:

                showAlert(getString(R.string.app_name), message);

                break;

            case PRIVATE_JOIN:

                showAlert(getString(R.string.app_name), message);

                break;

            case CANCEL_PRIVATE_JOIN_REQUEST:

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

            /*case GROUP_DETAIL:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    Gson gson = new Gson();

                    group = gson.fromJson(data.toString(), Group.class);

                    setData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;*/


            case GROUP_DELETE:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete", true);
                resultIntent.putExtra("group", group);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                break;

            case PUBLIC_JOIN:

                group.setGroupRole("member");
                toolbar.getMenu().removeItem(R.id.join_group);
                getMenuInflater().inflate(R.menu.activity_group_detail_public, toolbar.getMenu());
                toolbar.getMenu().removeItem(R.id.join_group);

                break;

            case LEAVE:

                group.setGroupRole("");
                toolbar.getMenu().removeItem(R.id.leave_group);
                getMenuInflater().inflate(R.menu.activity_group_detail_public, toolbar.getMenu());
                toolbar.getMenu().removeItem(R.id.leave_group);

                break;

            case PRIVATE_JOIN:


                group.setJoinRequestStatus(new JoinRequestStatus().withRequestStatus("pending"));

                toolbar.getMenu().removeItem(R.id.join_private_group);
                getMenuInflater().inflate(R.menu.activity_group_detail_private, toolbar.getMenu());
                toolbar.getMenu().removeItem(R.id.join_private_group);

                break;

            case CANCEL_PRIVATE_JOIN_REQUEST:

                group.setJoinRequestStatus(null);
                toolbar.getMenu().removeItem(R.id.cancel_join_request);
                getMenuInflater().inflate(R.menu.activity_group_detail_private, toolbar.getMenu());
                toolbar.getMenu().removeItem(R.id.cancel_join_request);

                break;


        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
