package com.app.smeezy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.ProfileViewPagerAdapter;
import com.app.smeezy.fragment.MyEventFragment;
import com.app.smeezy.fragment.MyGroupFragment;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ConnectionRecord;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.MySpannable;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.smeezy.utills.Utills.collapse;
import static com.app.smeezy.utills.Utills.expand;

public class ProfileActivity extends BaseActivity implements IRequestView, View.OnClickListener {

    private static final String GET_PROFILE_DATA = "get_profile";
    private static final String MESSAGE = "message";
    private static final String SEND_FRIEND_REQUEST = "send_friend_request";
    private static final String ACCEPT_FRIEND_REQUEST = "accept_friend_request";
    private static final String REJECT_FRIEND_REQUEST = "reject_friend_request";
    private static final String BLOCK = "block";
    private static final String UNBLOCK = "unblock";
    private static final String FOLLOW_USER = "follow_user";
    private static final String UNFOLLOW_USER = "unfollow_user";

    @BindView(R.id.cl_profile_main)
    CoordinatorLayout cl_main;

    @BindView(R.id.img_profile_cover)
    ImageView img_cover;

    @BindView(R.id.progress_bar_profile_cover)
    ProgressBar progress_bar_cover;

    @BindView(R.id.img_profile_user)
    ImageView img_user;

    @BindView(R.id.progress_bar_profile_user)
    ProgressBar progress_bar_user;

    @BindView(R.id.btn_profile_edit_cover)
    Button btn_edit_cover;

    @BindView(R.id.img_profile_edit_profile_image)
    ImageView img_edit_user;

    @BindView(R.id.tv_profile_user_name)
    TextView tv_name;

    @BindView(R.id.tv_profile_work_designation)
    TextView tv_work_designation;

    @BindView(R.id.tv_profile_user_city_country)
    TextView tv_user_city_country;

    @BindView(R.id.ll_accept_reject)
    LinearLayout ll_accept_reject;

    @BindView(R.id.btn_profile_accept_request)
    Button btn_accept_request;

    @BindView(R.id.btn_profile_reject_request)
    Button btn_reject_request;

    @BindView(R.id.btn_profile_add_friend)
    Button btn_add_friend;

    @BindView(R.id.btn_profile_message)
    Button btn_message;

    @BindView(R.id.tv_profile_about_me_desc)
    TextView tv_about_me_desc;

    @BindView(R.id.tv_profile_email)
    TextView tv_email;

    @BindView(R.id.tv_profile_phone)
    TextView tv_phone;

    @BindView(R.id.tv_profile_dob)
    TextView tv_dob;

    @BindView(R.id.tv_profile_relationship_status)
    TextView tv_relationship_status;

    @BindView(R.id.tv_profile_gender)
    TextView tv_gender;

    @BindView(R.id.tv_profile_location_address)
    TextView tv_location_address;

    @BindView(R.id.tv_profile_fb_url)
    TextView tv_fb_url;

    @BindView(R.id.tv_profile_twitter_url)
    TextView tv_twitter_url;

    @BindView(R.id.tv_profile_linkedin_url)
    TextView tv_linkedin_url;

    @BindView(R.id.tv_profile_religious_views)
    TextView tv_religious_views;

    @BindView(R.id.tv_profile_political_views)
    TextView tv_political_views;

    @BindView(R.id.tv_profile_work)
    TextView tv_work;

    @BindView(R.id.tv_profile_school)
    TextView tv_school;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_title)
    TextView activity_title;

    @BindView(R.id.ll_profile_email)
    LinearLayout ll_email;

    @BindView(R.id.ll_profile_phone)
    LinearLayout ll_phone;

    @BindView(R.id.ll_profile_dob)
    LinearLayout ll_dob;

    @BindView(R.id.ll_profile_relationship_status)
    LinearLayout ll_relationship_status;

    @BindView(R.id.ll_profile_gender)
    LinearLayout ll_gender;

    @BindView(R.id.ll_profile_location)
    LinearLayout ll_location;

    @BindView(R.id.img_profile_call)
    ImageView img_call;

    @BindView(R.id.img_profile_email)
    ImageView img_email;

    @BindView(R.id.img_profile_collapse)
    ImageView img_profile_collapse;

    @BindView(R.id.ll_profile_extra)
    LinearLayout ll_profile_extra;

    @BindView(R.id.btn_profile_follow)
    Button btn_profile_follow;

    @BindView(R.id.btn_profile_unfollow)
    Button btn_profile_unfollow;

    @BindView(R.id.tv_social_connection)
    TextView tv_social_connection;

    @BindView(R.id.ll_fb)
    LinearLayout ll_fb;

    @BindView(R.id.ll_twitter)
    LinearLayout ll_twitter;

    @BindView(R.id.ll_linkedin)
    LinearLayout ll_linkdedin;

    @BindView(R.id.tv_other_setting)
    TextView tv_other_setting;

    @BindView(R.id.ll_religious)
    LinearLayout ll_religious;

    @BindView(R.id.ll_political)
    LinearLayout ll_political;

    @BindView(R.id.ll_work)
    LinearLayout ll_work;

    @BindView(R.id.ll_school)
    LinearLayout ll_school;

    @BindView(R.id.ll_about_me)
    LinearLayout ll_about_me;

    @BindView(R.id.ll_profile_social_connection)
    LinearLayout ll_social_connection;

    @BindView(R.id.tv_profile_mutual_friends)
    TextView tv_mutual_friends;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private Dialog messageDialog;

    private ViewPager viewPager;

    private User user = new User();
    private String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        cl_main.setVisibility(View.GONE);

        Intent intent = getIntent();
        memberId = intent.getStringExtra("memberId");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);


        setUpToolbar();
        //setUpViewPager();

        getProfileData(memberId);

    }

    private void setUpToolbar() {

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

        activity_title.setText(getString(R.string.title_activity_profile));
    }

    private void setUpViewPager() {

        viewPager = findViewById(R.id.viewpager_profile);
        TabLayout tabLayout = findViewById(R.id.tab_layout_profile);
        tabLayout.setupWithViewPager(viewPager);


        ProfileViewPagerAdapter viewPagerAdapter = new ProfileViewPagerAdapter(mContext, getSupportFragmentManager(),
                memberId, false, user);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(6);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utills.hideSoftKeyboard(ProfileActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getProfileData(String memberId) {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getProfileData(GET_PROFILE_DATA, PreferenceUtils.getId(mContext), memberId);
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    private void setData() {

        tv_name.setText(user.getName());

        if (user.getCity().isEmpty()) {
            tv_user_city_country.setVisibility(View.GONE);
        } else {
            tv_user_city_country.setVisibility(View.VISIBLE);
            tv_user_city_country.setText(user.getCity());
        }

        if (user.getWork().isEmpty()) {
            tv_work_designation.setVisibility(View.GONE);
        } else {
            tv_work_designation.setVisibility(View.VISIBLE);
            tv_work_designation.setText(user.getWork());
        }

        if (user.getSummary().isEmpty()) {
            ll_about_me.setVisibility(View.GONE);
        } else {
            ll_about_me.setVisibility(View.VISIBLE);
            tv_about_me_desc.setText(user.getSummary());
        }

        if (user.getEmailPrivacy().equals("private") || user.getEmail().isEmpty()) {
            ll_email.setVisibility(View.GONE);
            img_email.setVisibility(View.GONE);
        } else {
            ll_email.setVisibility(View.VISIBLE);
            tv_email.setText(user.getEmail());
            //img_email.setVisibility(View.VISIBLE);
        }

        if (user.getPhonePrivacy().equals("private") || user.getPhoneNumber().isEmpty()) {
            ll_phone.setVisibility(View.GONE);
            img_call.setVisibility(View.GONE);
        } else {
            ll_phone.setVisibility(View.VISIBLE);
            img_call.setVisibility(View.VISIBLE);
            tv_phone.setText(user.getPhoneNumber());
        }

        if (user.getDobPrivacy().equals("private") || user.getDob().isEmpty()) {
            ll_dob.setVisibility(View.GONE);
        } else {
            ll_dob.setVisibility(View.VISIBLE);
            tv_dob.setText(Utills.getDateInMDYFormat(user.getDob()));
        }

        if (user.getRelationshipPrivacy().equals("private") || user.getRelationshipStatus().isEmpty()) {
            ll_relationship_status.setVisibility(View.GONE);
        } else {
            ll_relationship_status.setVisibility(View.VISIBLE);
            tv_relationship_status.setText(user.getRelationshipStatus());
        }

        if (user.getGender().isEmpty()){
            ll_gender.setVisibility(View.GONE);
        }else {
            ll_gender.setVisibility(View.VISIBLE);
            tv_gender.setText(user.getGender());

        }

        if (user.getLocationPrivacy().equals("private") || user.getLocationAddress().isEmpty()) {
            ll_location.setVisibility(View.GONE);
        } else {
            ll_location.setVisibility(View.VISIBLE);
            tv_location_address.setText(user.getLocationAddress());
        }

        if (user.getSocialConnectionPrivacy().equals("private")) {
            ll_social_connection.setVisibility(View.GONE);
        }else {
            ll_social_connection.setVisibility(View.VISIBLE);
            if (user.getFbUrl().isEmpty()) {
                ll_fb.setVisibility(View.GONE);
            } else {
                ll_fb.setVisibility(View.VISIBLE);
            }
            if (user.getTwitterUrl().isEmpty()) {
                ll_twitter.setVisibility(View.GONE);
            } else {
                ll_twitter.setVisibility(View.VISIBLE);
            }
            if (user.getLinkedinUrl().isEmpty()) {
                ll_linkdedin.setVisibility(View.GONE);
            } else {
                ll_linkdedin.setVisibility(View.VISIBLE);
            }

            if (user.getFbUrl().isEmpty() && user.getTwitterUrl().isEmpty() && user.getLinkedinUrl().isEmpty()) {
                tv_social_connection.setVisibility(View.GONE);
            } else {
                tv_social_connection.setVisibility(View.VISIBLE);
            }
        }

        if (user.getReligiousView().isEmpty()) {
            ll_religious.setVisibility(View.GONE);
        } else {
            ll_religious.setVisibility(View.VISIBLE);
        }

        if (user.getPoliticalView().isEmpty()) {
            ll_political.setVisibility(View.GONE);
        } else {
            ll_political.setVisibility(View.VISIBLE);
        }

        if (user.getWork().isEmpty()) {
            ll_work.setVisibility(View.GONE);
        } else {
            ll_work.setVisibility(View.VISIBLE);
        }

        if (user.getSchool().isEmpty()) {
            ll_school.setVisibility(View.GONE);
        } else {
            ll_school.setVisibility(View.VISIBLE);
        }

        if (user.getReligiousView().isEmpty() && user.getPoliticalView().isEmpty() &&
                user.getWork().isEmpty() && user.getSchool().isEmpty()) {
            tv_other_setting.setVisibility(View.GONE);
        } else {
            tv_other_setting.setVisibility(View.VISIBLE);
        }

        if (user.getMutualFriends() == 0){
            tv_mutual_friends.setVisibility(View.GONE);
        }else {
            tv_mutual_friends.setVisibility(View.VISIBLE);
            tv_mutual_friends.setText(getResources().getQuantityString(R.plurals.mutual_friends,
                    user.getMutualFriends(), user.getMutualFriends()));
        }







        tv_fb_url.setText(user.getFbUrl());
        tv_twitter_url.setText(user.getTwitterUrl());
        tv_linkedin_url.setText(user.getLinkedinUrl());
        tv_religious_views.setText(user.getReligiousView());
        tv_political_views.setText(user.getPoliticalView());
        tv_work.setText(user.getWork());
        tv_school.setText(user.getSchool());

        if (user.getIsFriend().equals("1")) {
            btn_add_friend.setVisibility(View.GONE);
            btn_profile_follow.setVisibility(View.VISIBLE);
        } else if (user.getIsRequestSent().equals("1")) {

            if (user.getConnectionRecord().getFromMemberId().equals(PreferenceUtils.getId(mContext))) {
                btn_add_friend.setText(getString(R.string.request_sent));
            } else if (user.getConnectionRecord().getFromMemberId().equals(user.getId())) {

                btn_add_friend.setVisibility(View.GONE);
                ll_accept_reject.setVisibility(View.VISIBLE);

                btn_accept_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cd.isConnectingToInternet()) {
                            requestPresenter.manageFriendRequest(ACCEPT_FRIEND_REQUEST, PreferenceUtils.getId(mContext), user.getId(), "accept");
                        } else {
                            Utills.noInternetConnection(mContext);
                        }
                    }
                });

                btn_reject_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cd.isConnectingToInternet()) {
                            requestPresenter.manageFriendRequest(REJECT_FRIEND_REQUEST, PreferenceUtils.getId(mContext), user.getId(), "reject");
                        } else {
                            Utills.noInternetConnection(mContext);
                        }
                    }
                });
            }
        } else {
            btn_add_friend.setText(getString(R.string.add_friend));
            btn_add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cd.isConnectingToInternet()) {
                        requestPresenter.sendFriendRequest(SEND_FRIEND_REQUEST, PreferenceUtils.getId(mContext), user.getId());
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }
            });

        }

        if (user.getIsUnfollowUser().equals("1")) {
            btn_profile_follow.setVisibility(View.VISIBLE);
            btn_profile_unfollow.setVisibility(View.GONE);
        } else {
            //btn_profile_unfollow.setVisibility(View.VISIBLE);
            btn_profile_unfollow.setVisibility(View.GONE);
            btn_profile_follow.setVisibility(View.GONE);
        }

        progress_bar_user.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(user.getProfileImage() + StaticData.THUMB_100)
                .asBitmap()
                .placeholder(R.drawable.no_user_blue)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        progress_bar_user.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress_bar_user.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img_user);

        progress_bar_cover.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(user.getCoverImage() + StaticData.THUMB_300)
                .asBitmap()
                .placeholder(R.drawable.hand_icon)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        progress_bar_cover.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress_bar_cover.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img_cover);

        /*List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragmentList){
            if (fragment instanceof MyStuffFragment){
                ((MyStuffFragment) fragment).checkPrivacy(user.getStufflistPrivacy());
            }

        }*/

        if (user.getBlockStatus().equals("1")) {
            toolbar.getMenu().removeItem(R.id.block);
        } else {
            toolbar.getMenu().removeItem(R.id.unblock);
        }

        setUpViewPager();

    }

    @OnClick({R.id.img_profile_collapse, R.id.btn_profile_message, R.id.img_profile_call, R.id.img_profile_email,
            R.id.img_profile_user, R.id.img_profile_cover, R.id.btn_profile_follow, R.id.btn_profile_unfollow,
            R.id.tv_profile_mutual_friends, R.id.tv_profile_fb_url, R.id.tv_profile_twitter_url,
            R.id.tv_profile_linkedin_url})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_profile_collapse:

                if (ll_profile_extra.getVisibility() == View.VISIBLE) {
                    collapse(ll_profile_extra);
                    img_profile_collapse.setImageResource(R.drawable.expand_arrow);
                } else if (ll_profile_extra.getVisibility() == View.GONE) {
                    expand(ll_profile_extra);
                    img_profile_collapse.setImageResource(R.drawable.collapse_arrow);
                }

                break;

            case R.id.btn_profile_message:

                showMessageDialog();

                break;

            case R.id.img_profile_call:

                Utills.show_alert_for_dial_no(mContext, user.getPhoneNumber());

                break;

            case R.id.img_profile_email:

                Utills.show_alert_for_mail(mContext, user.getEmail());

                break;

            case R.id.img_profile_user:

                Intent imageIntent = new Intent(mContext, ImageActivity.class);
                ArrayList<String> imageUrlList = new ArrayList<>();
                imageUrlList.add(user.getProfileImage());
                imageIntent.putStringArrayListExtra("imageUrlList", imageUrlList);
                imageIntent.putExtra("baseUrl", "");
                startActivity(imageIntent);

                break;

            case R.id.img_profile_cover:

                Intent imageIntent1 = new Intent(mContext, ImageActivity.class);
                ArrayList<String> imageUrlList1 = new ArrayList<>();
                imageUrlList1.add(user.getCoverImage());
                imageIntent1.putStringArrayListExtra("imageUrlList", imageUrlList1);
                imageIntent1.putExtra("baseUrl", "");
                startActivity(imageIntent1);

                break;

            case R.id.btn_profile_follow:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.followUnfollowUser(FOLLOW_USER, PreferenceUtils.getId(mContext),
                            user.getId());
                } else {
                    Utills.noInternetConnection(mContext);
                }

                break;

            case R.id.btn_profile_unfollow:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.followUnfollowUser(UNFOLLOW_USER, PreferenceUtils.getId(mContext),
                            user.getId());
                } else {
                    Utills.noInternetConnection(mContext);
                }
                break;

            case R.id.tv_profile_mutual_friends:

                Intent mutualFriendsIntent = new Intent(mContext, MutualFriendsActivity.class);
                mutualFriendsIntent.putExtra("friendId", user.getId());
                startActivity(mutualFriendsIntent);

                break;

            case R.id.tv_profile_fb_url:

                if (!user.getFbUrl().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(user.getFbUrl()));
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }

                /*if (!user.getFbUrl().isEmpty()) {
                    Intent intent = Utills.newFacebookIntent(mContext.getPackageManager(), user.getFbUrl());
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }*/



                break;

            case R.id.tv_profile_twitter_url:


                if (!user.getTwitterUrl().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(user.getTwitterUrl()));
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }

                break;

            case R.id.tv_profile_linkedin_url:


                if (!user.getLinkedinUrl().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(user.getLinkedinUrl()));
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_profile_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                backAction();

                break;

            case R.id.block:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.manageBlock(BLOCK, PreferenceUtils.getId(mContext),
                            user.getId(), "block");
                } else {
                    Utills.noInternetConnection(mContext);
                }

                break;


            case R.id.unblock:

                if (cd.isConnectingToInternet()) {
                    requestPresenter.manageBlock(UNBLOCK, PreferenceUtils.getId(mContext),
                            user.getId(), "unblock");
                } else {
                    Utills.noInternetConnection(mContext);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    private void backAction() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("userId", user.getId());
        resultIntent.putExtra("isUnfollowUser", user.getIsUnfollowUser());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    private void showMessageDialog() {

        messageDialog = new Dialog(mContext);
        messageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setContentView(R.layout.custom_message_dialog);
        messageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_send = (Button) messageDialog.findViewById(R.id.btn_message_send);
        Button btn_cancel = (Button) messageDialog.findViewById(R.id.btn_message_cancel);

        final EditText et_message = (EditText) messageDialog.findViewById(R.id.et_message_text);

        et_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    messageDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        et_message.requestFocus();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utills.hideSoftKeyboard(ProfileActivity.this);
                messageDialog.dismiss();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = et_message.getText().toString().trim();

                if (message.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.message_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        Utills.hideSoftKeyboard(ProfileActivity.this);
                        requestPresenter.composeMessage(MESSAGE, PreferenceUtils.getId(mContext), memberId, message);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        messageDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case StaticData.EVENT_DETAIL:

            case StaticData.CREATE_EVENT:

                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragmentList) {

                    if (fragment instanceof MyEventFragment) {

                        ((MyEventFragment) fragment).onActivityResult(requestCode, resultCode, data);

                    }
                }

                break;

            case StaticData.GROUP_DETAIL:

            case StaticData.CREATE_GROUP:

                List<Fragment> fragmentList1 = getSupportFragmentManager().getFragments();

                for (Fragment fragment : fragmentList1) {

                    if (fragment instanceof MyGroupFragment) {

                        ((MyGroupFragment) fragment).onActivityResult(requestCode, resultCode, data);

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

            case GET_PROFILE_DATA:

                showAlert(getString(R.string.app_name), message);

                break;

            case MESSAGE:

                showAlert(getString(R.string.app_name), message);

                break;

            case SEND_FRIEND_REQUEST:

                showAlert(getString(R.string.app_name), message);

                break;

            case ACCEPT_FRIEND_REQUEST:

                showAlert(getString(R.string.app_name), message);

                break;

            case REJECT_FRIEND_REQUEST:

                showAlert(getString(R.string.app_name), message);

                break;

            case BLOCK:

                showAlert(getString(R.string.app_name), message);

                break;

            case UNBLOCK:

                showAlert(getString(R.string.app_name), message);

                break;

            case FOLLOW_USER:

                showAlert(getString(R.string.app_name), message);

                break;

            case UNFOLLOW_USER:

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

            case GET_PROFILE_DATA:

                try {

                    JSONObject data = Data.getJSONObject("data");
                    String id = data.optString("id");
                    String email = data.optString("email");
                    String name = data.optString("name");
                    String gender = data.optString("gender");
                    String dob = data.optString("dob");
                    String address = data.optString("location_address");
                    String city = data.optString("city");
                    String region = data.optString("region");
                    String country = data.optString("country");
                    String pincode = data.optString("pincode");
                    String relationshipStatus = data.optString("relationship_status");
                    String summary = data.optString("summary");
                    String work = data.optString("work");
                    String school = data.optString("school");
                    String religiousView = data.optString("religious_view");
                    String politicalView = data.optString("political_view");
                    String phone = data.optString("phone_number");
                    String profileImage = data.optString("profile_image");
                    String coverImage = data.optString("cover_image");
                    String isFriend = data.optString("is_friend");
                    String isRequestSent = data.optString("is_request_sent");
                    String requestStatus = data.optString("request_status");
                    JSONObject connectionRecordObject = data.optJSONObject("connection_record");
                    String stufflistPrivacy = data.optString("stufflist_privacy");
                    String stufflistOption = data.optString("stufflist_option");
                    String emailPrivacy = data.optString("email_privacy");
                    String dobPrivacy = data.optString("dob_privacy");
                    String phonePrivacy = data.optString("phone_privacy");
                    String relationshipPrivacy = data.optString("relationship_privacy");
                    String locationPrivacy = data.optString("location_privacy");
                    String socialConnectionPrivacy = data.optString("social_connection_privacy");
                    String customStufflist = data.optString("custom_stufflist");
                    String blockStatus = data.optString("block_status");
                    String stufflist = data.optString("stufflist");
                    String stuffPrivacy = data.optString("stuff_privacy");
                    String isUnfollowUser = data.optString("is_unfollow_user");
                    int mutualFriends = data.optInt("mutual_friends");

                    boolean feedbackPosted = true;

                    if (data.optJSONObject("feedback") == null) {
                        feedbackPosted = false;
                    }

                    ConnectionRecord connectionRecord = new ConnectionRecord();

                    if (connectionRecordObject != null) {
                        connectionRecord.withCrqId(connectionRecordObject.optString("crq_id"))
                                .withFromMemberId(connectionRecordObject.optString("from_member_id"))
                                .withToMemberId(connectionRecordObject.optString("to_member_id"))
                                .withRequestStatus(connectionRecordObject.optString("request_status"));
                    }


                    String socialLinksString = data.optString("social_links");

                    String fbUrl = "", twitterUrl = "", linkedinUrl = "";

                    if (!socialLinksString.isEmpty()) {

                        JSONObject socialLinks = new JSONObject(socialLinksString);

                        fbUrl = socialLinks.optString("facebook");
                        twitterUrl = socialLinks.optString("twitter");
                        linkedinUrl = socialLinks.optString("linkedin");

                    }

                    user.withId(id)
                            .withEmail(email)
                            .withName(name)
                            .withGender(gender)
                            .withDob(dob)
                            .withLocationAddress(address)
                            .withCity(city)
                            .withRegion(region)
                            .withCountry(country)
                            .withPincode(pincode)
                            .withRelationshipStatus(relationshipStatus)
                            .withSummary(summary)
                            .withWork(work)
                            .withSchool(school)
                            .withReligiousView(religiousView)
                            .withPoliticalView(politicalView)
                            .withPhoneNumber(phone)
                            .withProfileImage(profileImage)
                            .withCoverImage(coverImage)
                            .withIsFriend(isFriend)
                            .withIsRequestSent(isRequestSent)
                            .withRequestStatus(requestStatus)
                            .withConnectionRecord(connectionRecord)
                            .withFbUrl(fbUrl)
                            .withTwitterUrl(twitterUrl)
                            .withLinkedinUrl(linkedinUrl)
                            .withStufflistPrivacy(stufflistPrivacy)
                            .withStufflistOption(stufflistOption)
                            .withEmailPrivacy(emailPrivacy)
                            .withDobPrivacy(dobPrivacy)
                            .withPhonePrivacy(phonePrivacy)
                            .withRelationshipPrivacy(relationshipPrivacy)
                            .withLocationPrivacy(locationPrivacy)
                            .withSocialConnectionPrivacy(socialConnectionPrivacy)
                            .withCustomStufflist(customStufflist)
                            .withBlockStatus(blockStatus)
                            .withFeedbackPosted(feedbackPosted)
                            .withStufflist(stufflist)
                            .withStuffPrivacy(stuffPrivacy)
                            .withIsUnfollowUser(isUnfollowUser)
                            .withMutualFriends(mutualFriends);

                    setData();

                    cl_main.setVisibility(View.VISIBLE);
                    if (!user.getSummary().isEmpty()) {
                        makeTextViewResizable(tv_about_me_desc, 2, "more...", true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case MESSAGE:

                if (messageDialog != null && messageDialog.isShowing()) {
                    messageDialog.dismiss();
                }

                break;

            case SEND_FRIEND_REQUEST:

                user.setIsRequestSent("1");
                user.getConnectionRecord().withFromMemberId(PreferenceUtils.getId(mContext))
                        .withToMemberId(user.getId());
                btn_add_friend.setText(getString(R.string.request_sent));

                break;

            case ACCEPT_FRIEND_REQUEST:

                user.setIsFriend("1");
                ll_accept_reject.setVisibility(View.GONE);

                break;

            case REJECT_FRIEND_REQUEST:

                user.setIsRequestSent("0");
                ll_accept_reject.setVisibility(View.GONE);
                btn_add_friend.setVisibility(View.VISIBLE);

                btn_add_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cd.isConnectingToInternet()) {
                            requestPresenter.sendFriendRequest(SEND_FRIEND_REQUEST, PreferenceUtils.getId(mContext), user.getId());
                        } else {
                            Utills.noInternetConnection(mContext);
                        }
                    }
                });

                break;

            case BLOCK:

                user.setBlockStatus("1");
                toolbar.getMenu().removeItem(R.id.block);
                getMenuInflater().inflate(R.menu.activity_profile_menu, toolbar.getMenu());
                toolbar.getMenu().removeItem(R.id.block);

                String message = Data.optString("replyMessage");

                final Dialog dialog = new Dialog(mContext);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_app_dialog);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
                TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
                TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
                TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
                TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
                txt_action1.setVisibility(View.GONE);
                txt_action2.setVisibility(View.GONE);
                txt_action3.setVisibility(View.VISIBLE);

                txt_action3.setText(getResources().getString(R.string.ok));
                txt_title.setText(getString(R.string.app_name));
                txt_message.setText(message);

                dialog.show();

                txt_action3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent i = new Intent(mContext, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                });

                break;

            case UNBLOCK:

                user.setBlockStatus("0");
                toolbar.getMenu().removeItem(R.id.unblock);
                getMenuInflater().inflate(R.menu.activity_profile_menu, toolbar.getMenu());
                toolbar.getMenu().removeItem(R.id.unblock);

                break;

            case FOLLOW_USER:

                user.setIsUnfollowUser("0");
                //btn_profile_unfollow.setVisibility(View.VISIBLE);
                btn_profile_unfollow.setVisibility(View.GONE);
                btn_profile_follow.setVisibility(View.GONE);

                break;

            case UNFOLLOW_USER:

                user.setIsUnfollowUser("1");

                Intent unfollowBroadcastIntent = new Intent(StaticData.UNFOLLOW_BROADCAST);
                unfollowBroadcastIntent.putExtra("userId", user.getId());

                mContext.sendBroadcast(unfollowBroadcastIntent);

                btn_profile_follow.setVisibility(View.VISIBLE);
                btn_profile_unfollow.setVisibility(View.GONE);

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 2, "more..", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
}

