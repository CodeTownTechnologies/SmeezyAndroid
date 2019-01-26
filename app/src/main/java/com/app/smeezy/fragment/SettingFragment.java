package com.app.smeezy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.AccountActivity;
import com.app.smeezy.activity.BlockedListActivity;
import com.app.smeezy.activity.ChangePasswordActivity;
import com.app.smeezy.activity.ProfilePrivacyActivity;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener, IRequestView {

    private static final String SAVE_NOTIFICATION_SETTING = "save_notification";
    private static final String GET_PROFILE_INFO = "get_profile_info";

    public static final int PROFILE_PRIVACY = 235;

    @BindView(R.id.switch_setting_notification)
    SwitchCompat switch_notification;

    @BindView(R.id.switch_setting_stuff_privacy)
    SwitchCompat switch_stuff_privacy;

    @BindView(R.id.tv_setting_stuff_privacy)
    TextView tv_stuff_privacy;

    @BindView(R.id.ll_setting_main)
    LinearLayout ll_main;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private User user = new User();

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        ll_main.setVisibility(View.GONE);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        getProfileInfo();

        return view;
    }

    private void getProfileInfo() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getProfileData(GET_PROFILE_INFO, PreferenceUtils.getId(mContext), "");
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    private void setData() {

        PreferenceUtils.setNotificationStatus(mContext, user.getNotification());
        PreferenceUtils.setStuffPrivacyStatus(mContext, user.getStuffPrivacy());

        if (PreferenceUtils.getNotificationStatus(mContext).equals("1")) {
            switch_notification.setChecked(true);
        } else {
            switch_notification.setChecked(false);
        }

        if (PreferenceUtils.getStuffPrivacyStatus(mContext).equals("private")) {
            switch_stuff_privacy.setChecked(true);
            tv_stuff_privacy.setText(getString(R.string.trade_borrow_stuff_privacy_private));
        } else if (PreferenceUtils.getStuffPrivacyStatus(mContext).equals("public")) {
            switch_stuff_privacy.setChecked(false);
            tv_stuff_privacy.setText(getString(R.string.trade_borrow_stuff_privacy_public));
        }

        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String notification = "0";

                if (b) {
                    notification = "1";
                }

                if (cd.isConnectingToInternet()) {
                    requestPresenter.saveNotificationSetting(SAVE_NOTIFICATION_SETTING, PreferenceUtils.getId(mContext),
                            notification, PreferenceUtils.getStuffPrivacyStatus(mContext));
                } else {
                    Utills.noInternetConnection(mContext);
                }

            }
        });

        switch_stuff_privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String shareStuffPrivacy = "public";

                if (b) {
                    shareStuffPrivacy = "private";
                }

                if (cd.isConnectingToInternet()) {
                    requestPresenter.saveNotificationSetting(SAVE_NOTIFICATION_SETTING, PreferenceUtils.getId(mContext),
                            PreferenceUtils.getNotificationStatus(mContext), shareStuffPrivacy);
                } else {
                    Utills.noInternetConnection(mContext);
                }


            }
        });


    }

    @OnClick({R.id.tv_setting_change_password, R.id.tv_setting_profile_privacy, R.id.tv_setting_account,
            R.id.tv_interest_blocked_users})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_setting_change_password:

                Intent intent = new Intent(mContext, ChangePasswordActivity.class);
                startActivity(intent);

                break;

            case R.id.tv_setting_profile_privacy:

                Intent intent1 = new Intent(mContext, ProfilePrivacyActivity.class);
                intent1.putExtra("user", user);
                startActivityForResult(intent1, PROFILE_PRIVACY);

                break;

            case R.id.tv_setting_account:

                Intent accountIntent = new Intent(mContext, AccountActivity.class);
                startActivity(accountIntent);

                break;


            case R.id.tv_interest_blocked_users:

                Intent blockIntent = new Intent(mContext, BlockedListActivity.class);
                startActivity(blockIntent);

                break;

        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.no_menu, menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PROFILE_PRIVACY:

                if (resultCode == Activity.RESULT_OK) {

                    if (data != null) {
                        user = (User) data.getSerializableExtra("user");
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

            case SAVE_NOTIFICATION_SETTING:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case GET_PROFILE_INFO:

                try {

                    JSONObject data = Data.getJSONObject("data");
                    String id = data.optString("id");
                    String email = data.optString("email");
                    String name = data.optString("name");
                    String gender = data.optString("gender");
                    String dob = data.optString("dob");
                    String latitude = data.optString("location_latitude");
                    String longitude = data.optString("location_longitude");
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
                    String stufflistPrivacy = data.optString("stufflist_privacy");
                    String stufflistOption = data.optString("stufflist_option");
                    String emailPrivacy = data.optString("email_privacy");
                    String dobPrivacy = data.optString("dob_privacy");
                    String phonePrivacy = data.optString("phone_privacy");
                    String relationshipPrivacy = data.optString("relationship_privacy");
                    String locationPrivacy = data.optString("location_privacy");
                    String socialConnectionPrivacy = data.optString("social_connection_privacy");
                    String notification = data.optString("notification");
                    String stuffPrivacy = data.optString("stuff_privacy");

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
                            .withLocationLatitude(latitude)
                            .withLocationLongitude(longitude)
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
                            .withNotification(notification)
                            .withStuffPrivacy(stuffPrivacy);

                    ll_main.setVisibility(View.VISIBLE);

                    setData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case SAVE_NOTIFICATION_SETTING:

                try {
                    JSONObject request = Data.getJSONObject("req");

                    int notification = request.optInt("notification");
                    String shareStuffPrivacy = request.optString("stuff_privacy");
                    user.setNotification(String.valueOf(notification));
                    user.setStufflistPrivacy(shareStuffPrivacy);
                    PreferenceUtils.setNotificationStatus(mContext, String.valueOf(notification));
                    PreferenceUtils.setStuffPrivacyStatus(mContext, shareStuffPrivacy);

                    if (notification == 1) {
                        switch_notification.setChecked(true);
                    } else {
                        switch_notification.setChecked(false);
                    }

                    if (shareStuffPrivacy.equals("public")) {
                        switch_stuff_privacy.setChecked(false);
                        tv_stuff_privacy.setText(getString(R.string.trade_borrow_stuff_privacy_public));
                    } else if (shareStuffPrivacy.equals("private")) {
                        switch_stuff_privacy.setChecked(true);
                        tv_stuff_privacy.setText(getString(R.string.trade_borrow_stuff_privacy_private));
                    }


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
