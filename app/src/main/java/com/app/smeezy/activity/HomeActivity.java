package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.fragment.ChatListParentFragment;
import com.app.smeezy.fragment.ContactUsFragment;
import com.app.smeezy.fragment.FaqFragment;
import com.app.smeezy.fragment.HomeParentFragment;
import com.app.smeezy.fragment.InterestFragment;
import com.app.smeezy.fragment.InviteFriendFragment;
import com.app.smeezy.fragment.PrivacyPolicyFragment;
import com.app.smeezy.fragment.ProfileFragment;
import com.app.smeezy.fragment.SettingFragment;
import com.app.smeezy.fragment.SuggestedFriendsFragment;
import com.app.smeezy.fragment.TermsFragment;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IRequestView, View.OnClickListener {

    public static final String LOGOUT_METHOD = "onLogoutSuccess";
    public static final String GET_UNREAD_CHAT_COUNT = "get_unread_chat_count";

    @BindView(R.id.ll_home_bottom_bar)
    LinearLayout ll_home_bottom_bar;

    @BindView(R.id.img_home)
    ImageView img_home;

    @BindView(R.id.img_interest)
    ImageView img_interest;

    @BindView(R.id.img_chat)
    ImageView img_chat;

    @BindView(R.id.img_profile)
    ImageView img_profile;

    @BindView(R.id.tv_chat_count)
    TextView tv_chat_count;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private Toolbar toolbar;
    private TextView activity_title;
    private String messageCount = "0", privateMessageCount = "0";

    private String previousActivityTag;

    private BroadcastReceiver chatCountRefreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getUnreadChatCount();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        previousActivityTag = getIntent().getStringExtra(getString(R.string.activity_tag));

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);


        setUpToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Utills.hideSoftKeyboard(HomeActivity.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Utills.hideSoftKeyboard(HomeActivity.this);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationView();

        if (previousActivityTag != null &&
                (previousActivityTag.equals(getString(R.string.activity_edit_profile_details_tag)) ||
                previousActivityTag.equals(getString(R.string.fragment_profile_tag)))) {

            loadProfileFragment();

        } else if (previousActivityTag != null &&
                (previousActivityTag.equals(getString(R.string.fragment_private_chat_tag))
                ||  previousActivityTag.equals(getString(R.string.fragment_chat_tag)))){

            loadChatFragment();

        }else {

            loadHomeFragment();

        }
    }


    private void setUpToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        Glide.with(mContext)
                .load(PreferenceUtils.getLogoUrl(mContext))
                .asBitmap()
                .into(img_logo);

        activity_title = (TextView) findViewById(R.id.activity_title);
    }

    @Override
    public void onHomeClick(View view) {
        loadHomeFragment();
    }

    private void setUpNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        View navHeader = navigationView.getHeaderView(0);
        ImageView img_nav = (ImageView) navHeader.findViewById(R.id.img_nav);
        TextView tv_nav_email = (TextView) navHeader.findViewById(R.id.tv_nav_email);

        tv_nav_email.setText(PreferenceUtils.getName(mContext));

        Glide.with(mContext)
                .load(PreferenceUtils.getProfilePicUrl(mContext) + StaticData.THUMB_100)
                .asBitmap()
                .placeholder(R.drawable.no_user_white)
                .into(img_nav);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        img_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                loadProfileFragment();
            }
        });

        tv_nav_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                loadProfileFragment();
            }
        });
    }

    private void getUnreadChatCount() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getUnreadChatCount(GET_UNREAD_CHAT_COUNT, PreferenceUtils.getId(mContext));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        mContext.registerReceiver(chatCountRefreshBroadcast, new IntentFilter(StaticData.CHAT_REFRESH_BROADCAST));
        getUnreadChatCount();
        restoreNavigationViewItemSelection();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mContext.unregisterReceiver(chatCountRefreshBroadcast);

    }

    @OnClick({R.id.img_home, R.id.img_interest, R.id.img_chat, R.id.img_profile})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_home:

                loadHomeFragment();

                break;

            case R.id.img_interest:

                loadInterestFragment();

                break;

            case R.id.img_chat:

                loadChatFragment();

                break;

            case R.id.img_profile:

                loadProfileFragment();

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragments) {
            if (requestCode == SettingFragment.PROFILE_PRIVACY) {
                if (fragment instanceof SettingFragment) {
                    ((SettingFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            } else {
                if (fragment instanceof ProfileFragment) {
                    ((ProfileFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ChatListParentFragment) {
            loadHomeFragment();
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ProfileFragment) {
            loadHomeFragment();
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof InterestFragment) {
            loadHomeFragment();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                loadHomeFragment();

                break;

            /*case R.id.suggested_friends:
                activity_title.setText(getResources().getString(R.string.title_fragment_suggested_friends));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SuggestedFriendsFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SuggestedFriendsFragment())
                            .commit();
                }


                break;*/

            case R.id.setting:
                activity_title.setText(getResources().getString(R.string.title_fragment_setting));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SettingFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SettingFragment())
                            .commit();
                }


                break;

            case R.id.terms:
                activity_title.setText(getResources().getString(R.string.title_fragment_terms_and_conditions));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof TermsFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new TermsFragment())
                            .commit();
                }

                break;

            case R.id.privacy_policy:
                activity_title.setText(getResources().getString(R.string.title_fragment_privacy_policy));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PrivacyPolicyFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new PrivacyPolicyFragment())
                            .commit();
                }

                break;

            case R.id.contact_us:
                activity_title.setText(getResources().getString(R.string.title_fragment_contact_us));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ContactUsFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ContactUsFragment())
                            .commit();
                }

                break;

            case R.id.faq:
                activity_title.setText(getResources().getString(R.string.title_fragment_faq));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof FaqFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new FaqFragment())
                            .commit();
                }

                break;

            case R.id.invite_friend:

                activity_title.setText(getResources().getString(R.string.title_fragment_invite_friend));
                ll_home_bottom_bar.setVisibility(View.GONE);
                if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof InviteFriendFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new InviteFriendFragment())
                            .commit();
                }


               /* Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, PreferenceUtils.getReferralShareText(mContext));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/

                break;

            case R.id.logout:

                show_alert_for_logout(mContext);

                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void show_alert_for_logout(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_app_dialog);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action3.setVisibility(View.GONE);
        txt_action1.setVisibility(View.VISIBLE);
        txt_action2.setVisibility(View.VISIBLE);
        txt_action1.setText(context.getResources().getString(R.string.yes));
        txt_action2.setText(context.getResources().getString(R.string.no));
        txt_title.setText(context.getResources().getString(R.string.app_name));
        txt_message.setText(context.getResources().getString(R.string.do_you_want_to_logout));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (cd.isConnectingToInternet()) {
                    startProgressBar();
                    requestPresenter.logout(LOGOUT_METHOD, PreferenceUtils.getId(mContext));
                } else {
                    Utills.noInternetConnection(mContext);
                }

            }
        });

        txt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                restoreNavigationViewItemSelection();

            }
        });

    }

    private void onLogoutSuccess() {

        if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        if (GoogleSignIn.getLastSignedInAccount(mContext) != null) {

            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestId()
                    .requestEmail()
                    .requestProfile()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(mContext, googleSignInOptions);
            googleSignInClient.signOut();
        }

        PreferenceUtils.setIsLogin(mContext, false);
        PreferenceUtils.setIsFirstTime(mContext, true);
        PreferenceUtils.setId(mContext, "");
        PreferenceUtils.setProfilePicUrl(mContext, "");
        PreferenceUtils.setStepFirst(mContext, "No");
        PreferenceUtils.setItemSearchDistance(mContext, 27);
        PreferenceUtils.setCommunitySearchDistance(mContext, 27);


        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    private void restoreNavigationViewItemSelection() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        int i = 0;


        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeParentFragment ||
                getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof InterestFragment ||
                getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ChatListParentFragment ||
                getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ProfileFragment) {
            navigationView.getMenu().getItem(i).setChecked(true);
        } /*else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SuggestedFriendsFragment) {
            navigationView.getMenu().getItem(i + 1).setChecked(true);
        }*/ else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SettingFragment) {
            navigationView.getMenu().getItem(i + 1).setChecked(true);
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof TermsFragment) {
            navigationView.getMenu().getItem(i + 2).setChecked(true);
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PrivacyPolicyFragment) {
            navigationView.getMenu().getItem(i + 3).setChecked(true);
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ContactUsFragment) {
            navigationView.getMenu().getItem(i + 4).setChecked(true);
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof FaqFragment) {
            navigationView.getMenu().getItem(i + 5).setChecked(true);
        }else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof InviteFriendFragment) {
            navigationView.getMenu().getItem(i + 6).setChecked(true);
        }
    }

    public void loadHomeFragment() {

        img_home.setActivated(true);
        img_interest.setActivated(false);
        img_chat.setActivated(false);
        img_profile.setActivated(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        ll_home_bottom_bar.setVisibility(View.VISIBLE);

        activity_title.setText(getResources().getString(R.string.title_fragment_home));
        ll_home_bottom_bar.setVisibility(View.VISIBLE);
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeParentFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeParentFragment())
                    .commit();
        }
    }

    public void loadInterestFragment() {
        img_home.setActivated(false);
        img_interest.setActivated(true);
        img_chat.setActivated(false);
        img_profile.setActivated(false);

        ll_home_bottom_bar.setVisibility(View.VISIBLE);

        activity_title.setText(getResources().getString(R.string.title_fragment_interest));
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof InterestFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InterestFragment())
                    .commit();
        }
    }

    public void loadChatFragment() {
        img_home.setActivated(false);
        img_interest.setActivated(false);
        img_chat.setActivated(true);
        img_profile.setActivated(false);

        ll_home_bottom_bar.setVisibility(View.VISIBLE);

        activity_title.setText(getResources().getString(R.string.title_fragment_chat_list_parent));
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ChatListParentFragment)) {

            ChatListParentFragment chatListParentFragment = new ChatListParentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("messageCount", messageCount);
            bundle.putString("privateMessageCount", privateMessageCount);
            chatListParentFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, chatListParentFragment)
                    .commit();
        }

    }

    public void loadProfileFragment() {

        img_home.setActivated(false);
        img_interest.setActivated(false);
        img_chat.setActivated(false);
        img_profile.setActivated(true);

        ll_home_bottom_bar.setVisibility(View.VISIBLE);

        activity_title.setText(getResources().getString(R.string.title_fragment_profile));
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ProfileFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .commit();
        }
    }

    @Override
    public void showLoadingProgressBar() {

    }

    @Override
    public void hideLoadingProgressBar() {
        dismissProgressBar();
    }

    @Override
    public void Failed(String method, String message) {
        if (method.equals(LOGOUT_METHOD)) {
            showAlert(getResources().getString(R.string.app_name), message);
        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {
        switch (method) {

            case LOGOUT_METHOD:

                onLogoutSuccess();

                break;

            case GET_UNREAD_CHAT_COUNT:


                try {


                    JSONObject data = Data.getJSONObject("data");

                    String count = data.optString("count");
                    messageCount = data.optString("message");
                    privateMessageCount = data.optString("private_message");

                    if (count != null) {
                        if (count.equals("0")) {
                            tv_chat_count.setText("");
                            tv_chat_count.setVisibility(View.GONE);
                        } else {
                            tv_chat_count.setText(count);
                            tv_chat_count.setVisibility(View.VISIBLE);
                        }
                    }

                    Intent chatCountRefresh = new Intent(StaticData.CHAT_COUNT_REFRESH_BROADCAST);
                    chatCountRefresh.putExtra("messageCount", messageCount);
                    chatCountRefresh.putExtra("privateMessageCount", privateMessageCount);

                    sendBroadcast(chatCountRefresh);


                    String referralCode = data.optString("referral_code");
                    String referralLink = data.optString("referral_link");
                    String referralShareText = data.optString("referral_share_text");

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        referralShareText = Html.fromHtml(referralShareText).toString();
                    } else {
                        referralShareText = Html.fromHtml(referralShareText, Html.FROM_HTML_MODE_LEGACY).toString();
                    }

                    PreferenceUtils.setReferralShareText(mContext, referralShareText);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }


    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
