package com.app.smeezy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.EditProfileDetailsActivity;
import com.app.smeezy.activity.EditProfileOtherDetailsActivity;
import com.app.smeezy.activity.ImageActivity;
import com.app.smeezy.adapter.ProfileViewPagerAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.MySpannable;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.smeezy.utills.Utills.collapse;
import static com.app.smeezy.utills.Utills.expand;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements IRequestView, View.OnClickListener {


    private static final String GET_PROFILE_DATA = "get_profile";
    private static final String UPDATE_COVER_PIC = "update_cover_pic";
    private static final String UPDATE_PROFILE_PIC = "update_profile_pic";
    private static final String EDIT_FB_URL = "edit_fb_url";
    private static final String EDIT_TWITTER_URL = "edit_twitter_url";
    private static final String EDIT_LINKEDIN_URL = "edit_linkedin_url";
    private static final String EDIT_ABOUT_INFO = "edit_about";


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

    @BindView(R.id.tv_profile_about_me_desc)
    TextView tv_about_me_desc;

    @BindView(R.id.et_profile_about_me_desc)
    EditText et_about_me_desc;

    @BindView(R.id.img_profile_edit_about_me)
    ImageView img_edit_about_me;

    @BindView(R.id.img_profile_correct_about_me)
    ImageView img_correct_about_me;

    @BindView(R.id.img_profile_cross_about_me)
    ImageView img_cross_about_me;

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

    @BindView(R.id.et_profile_fb_url)
    EditText et_fb_url;

    @BindView(R.id.img_profile_edit_fb_url)
    ImageView img_edit_fb_url;

    @BindView(R.id.img_profile_correct_fb_url)
    ImageView img_correct_fb_url;

    @BindView(R.id.img_profile_cross_fb_url)
    ImageView img_cross_fb_url;

    @BindView(R.id.tv_profile_twitter_url)
    TextView tv_twitter_url;

    @BindView(R.id.et_profile_twitter_url)
    EditText et_twitter_url;

    @BindView(R.id.img_profile_edit_twitter_url)
    ImageView img_edit_twitter_url;

    @BindView(R.id.img_profile_correct_twitter_url)
    ImageView img_correct_twitter_url;

    @BindView(R.id.img_profile_cross_twitter_url)
    ImageView img_cross_twitter_url;

    @BindView(R.id.tv_profile_linkedin_url)
    TextView tv_linkedin_url;

    @BindView(R.id.et_profile_linkedin_url)
    EditText et_linkedin_url;

    @BindView(R.id.img_profile_edit_linkedin_url)
    ImageView img_edit_linkedin_url;

    @BindView(R.id.img_profile_correct_linkedin_url)
    ImageView img_correct_linkedin_url;

    @BindView(R.id.img_profile_cross_linkedin_url)
    ImageView img_cross_linkedin_url;

    @BindView(R.id.tv_profile_religious_views)
    TextView tv_religious_views;

    @BindView(R.id.tv_profile_political_views)
    TextView tv_political_views;

    @BindView(R.id.tv_profile_work)
    TextView tv_work;

    @BindView(R.id.tv_profile_school)
    TextView tv_school;

    @BindView(R.id.tv_profile_edit_other_settings)
    TextView tv_edit_other_settings;

    @BindView(R.id.img_profile_collapse)
    ImageView img_profile_collapse;

    @BindView(R.id.ll_profile_extra)
    LinearLayout ll_profile_extra;

    @BindView(R.id.ll_profile_relationship_status)
    LinearLayout ll_relationship_status;

    @BindView(R.id.ll_profile_gender)
    LinearLayout ll_gender;

    @BindView(R.id.ll_profile_phone)
    LinearLayout ll_phone;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;
    private ArrayList<Uri> filePathList = new ArrayList<>();

    private ViewPager viewPager;

    private User user = new User();

    private boolean changingCover = false;

    private ProfileViewPagerAdapter viewPagerAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        cl_main.setVisibility(View.GONE);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        getProfileData();


        return view;
    }

    private void setUpViewPager(View view) {

        viewPager = view.findViewById(R.id.viewpager_profile);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_profile);
        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ProfileViewPagerAdapter(mContext, getChildFragmentManager(),
                PreferenceUtils.getId(mContext), true, user);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(6);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utills.hideSoftKeyboard((Activity) mContext);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getProfileData() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getProfileData(GET_PROFILE_DATA, PreferenceUtils.getId(mContext), "");
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

        if (user.getPhoneNumber().isEmpty()) {
            ll_phone.setVisibility(View.GONE);
        } else {
            ll_phone.setVisibility(View.VISIBLE);
            tv_phone.setText(user.getPhoneNumber());
        }

        if (user.getGender().isEmpty()) {
            ll_gender.setVisibility(View.GONE);
        } else {
            ll_gender.setVisibility(View.VISIBLE);
            tv_gender.setText(user.getGender());
        }

        if (user.getRelationshipStatus().isEmpty()) {
            ll_relationship_status.setVisibility(View.GONE);
        } else {
            ll_relationship_status.setVisibility(View.VISIBLE);
            tv_relationship_status.setText(user.getRelationshipStatus());
        }

        tv_about_me_desc.setText(user.getSummary());
        tv_email.setText(user.getEmail());
        tv_dob.setText(Utills.getDateInMDYFormat(user.getDob()));

        tv_location_address.setText(user.getLocationAddress());
        tv_fb_url.setText(user.getFbUrl());
        tv_twitter_url.setText(user.getTwitterUrl());
        tv_linkedin_url.setText(user.getLinkedinUrl());
        tv_religious_views.setText(user.getReligiousView());
        tv_political_views.setText(user.getPoliticalView());
        tv_work.setText(user.getWork());
        tv_school.setText(user.getSchool());

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

        /*List<Fragment> fragmentList = getChildFragmentManager().getFragments();

        for (Fragment fragment : fragmentList){
            if (fragment instanceof MyStuffFragment){
                ((MyStuffFragment) fragment).checkPrivacy(user.getStufflistPrivacy());

            }

        }*/

        setUpViewPager(view);


    }

    @OnClick({R.id.img_profile_edit_profile_image, R.id.btn_profile_edit_cover, R.id.img_profile_edit_about_me,
            R.id.img_profile_correct_about_me, R.id.img_profile_cross_about_me, R.id.img_profile_edit_fb_url,
            R.id.img_profile_correct_fb_url, R.id.img_profile_cross_fb_url, R.id.img_profile_edit_twitter_url,
            R.id.img_profile_correct_twitter_url, R.id.img_profile_cross_twitter_url, R.id.img_profile_edit_linkedin_url,
            R.id.img_profile_correct_linkedin_url, R.id.img_profile_cross_linkedin_url, R.id.tv_profile_edit_details,
            R.id.tv_profile_edit_other_settings, R.id.img_profile_collapse, R.id.img_profile_user,
            R.id.img_profile_cover, R.id.tv_profile_fb_url, R.id.tv_profile_twitter_url,
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

            case R.id.img_profile_edit_profile_image:

                changingCover = false;
                //((HomeActivity) mContext).selectImagePopUp();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);

                break;

            case R.id.btn_profile_edit_cover:

                changingCover = true;
                //((HomeActivity) mContext).selectImagePopUp();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);

                break;

            case R.id.img_profile_edit_about_me:

                et_about_me_desc.setText(user.getSummary());
                tv_about_me_desc.setVisibility(View.GONE);
                et_about_me_desc.setVisibility(View.VISIBLE);
                img_edit_about_me.setVisibility(View.GONE);
                img_correct_about_me.setVisibility(View.VISIBLE);
                img_cross_about_me.setVisibility(View.VISIBLE);
                et_about_me_desc.requestFocus();

                Utills.showSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_correct_about_me:

                Utills.hideSoftKeyboard(getActivity());

                String summary = et_about_me_desc.getText().toString().trim();

                if (summary.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.summary_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.editAboutInfo(EDIT_ABOUT_INFO, PreferenceUtils.getId(mContext), summary);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

            case R.id.img_profile_cross_about_me:

                et_about_me_desc.setVisibility(View.GONE);
                tv_about_me_desc.setVisibility(View.VISIBLE);
                img_edit_about_me.setVisibility(View.VISIBLE);
                img_correct_about_me.setVisibility(View.GONE);
                img_cross_about_me.setVisibility(View.GONE);
                Utills.hideSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_edit_fb_url:

                et_fb_url.setText(tv_fb_url.getText().toString());
                tv_fb_url.setVisibility(View.GONE);
                et_fb_url.setVisibility(View.VISIBLE);
                img_edit_fb_url.setVisibility(View.GONE);
                img_correct_fb_url.setVisibility(View.VISIBLE);
                img_cross_fb_url.setVisibility(View.VISIBLE);
                et_fb_url.requestFocus();
                Utills.showSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_correct_fb_url:

                Utills.hideSoftKeyboard(getActivity());

                String fbUrl = et_fb_url.getText().toString().trim();

                if (fbUrl.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.url_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.editSocialLinks(EDIT_FB_URL, PreferenceUtils.getId(mContext), "facebook", fbUrl);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

            case R.id.img_profile_cross_fb_url:

                et_fb_url.setVisibility(View.GONE);
                tv_fb_url.setVisibility(View.VISIBLE);
                img_edit_fb_url.setVisibility(View.VISIBLE);
                img_correct_fb_url.setVisibility(View.GONE);
                img_cross_fb_url.setVisibility(View.GONE);
                Utills.hideSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_edit_twitter_url:

                et_twitter_url.setText(tv_twitter_url.getText().toString());
                tv_twitter_url.setVisibility(View.GONE);
                et_twitter_url.setVisibility(View.VISIBLE);
                img_edit_twitter_url.setVisibility(View.GONE);
                img_correct_twitter_url.setVisibility(View.VISIBLE);
                img_cross_twitter_url.setVisibility(View.VISIBLE);
                et_twitter_url.requestFocus();
                Utills.showSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_correct_twitter_url:

                Utills.hideSoftKeyboard(getActivity());

                String twitterUrl = et_twitter_url.getText().toString().trim();

                if (twitterUrl.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.url_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.editSocialLinks(EDIT_TWITTER_URL, PreferenceUtils.getId(mContext), "twitter", twitterUrl);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

            case R.id.img_profile_cross_twitter_url:

                et_twitter_url.setVisibility(View.GONE);
                tv_twitter_url.setVisibility(View.VISIBLE);
                img_edit_twitter_url.setVisibility(View.VISIBLE);
                img_correct_twitter_url.setVisibility(View.GONE);
                img_cross_twitter_url.setVisibility(View.GONE);
                Utills.hideSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_edit_linkedin_url:

                et_linkedin_url.setText(tv_linkedin_url.getText().toString());
                tv_linkedin_url.setVisibility(View.GONE);
                et_linkedin_url.setVisibility(View.VISIBLE);
                img_edit_linkedin_url.setVisibility(View.GONE);
                img_correct_linkedin_url.setVisibility(View.VISIBLE);
                img_cross_linkedin_url.setVisibility(View.VISIBLE);
                et_linkedin_url.requestFocus();
                Utills.showSoftKeyboard(getActivity());

                break;

            case R.id.img_profile_correct_linkedin_url:

                Utills.hideSoftKeyboard(getActivity());

                String linkedinUrl = et_linkedin_url.getText().toString().trim();

                if (linkedinUrl.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.url_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.editSocialLinks(EDIT_LINKEDIN_URL, PreferenceUtils.getId(mContext), "linkedin", linkedinUrl);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

            case R.id.img_profile_cross_linkedin_url:

                et_linkedin_url.setVisibility(View.GONE);
                tv_linkedin_url.setVisibility(View.VISIBLE);
                img_edit_linkedin_url.setVisibility(View.VISIBLE);
                img_correct_linkedin_url.setVisibility(View.GONE);
                img_cross_linkedin_url.setVisibility(View.GONE);
                Utills.hideSoftKeyboard(getActivity());

                break;

            case R.id.tv_profile_edit_details:

                Intent intent = new Intent(mContext, EditProfileDetailsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);

                break;

            case R.id.tv_profile_edit_other_settings:

                Intent intent1 = new Intent(mContext, EditProfileOtherDetailsActivity.class);
                intent1.putExtra("user", user);
                startActivity(intent1);

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

           /* case R.id.tv_profile_my_interest:

                Intent myInterestIntent = new Intent(mContext, MyWishlistActivity.class);
                myInterestIntent.putExtra("wishList", user.getStufflist());
                startActivityForResult(myInterestIntent, StaticData.MY_WISHLIST);

                break;*/

            case R.id.tv_profile_fb_url:

                if (!user.getFbUrl().isEmpty()) {
                    Intent intent3 = new Intent(Intent.ACTION_VIEW);
                    intent3.setData(Uri.parse(user.getFbUrl()));
                    if (intent3.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent3);
                    }
                }

                /*if (!user.getFbUrl().isEmpty()) {
                    Intent intent3 = Utills.newFacebookIntent(mContext.getPackageManager(), user.getFbUrl());
                    if (intent3.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent3);
                    }

                }*/

                break;

            case R.id.tv_profile_twitter_url:


                if (!user.getTwitterUrl().isEmpty()) {
                    Intent intent3 = new Intent(Intent.ACTION_VIEW);
                    intent3.setData(Uri.parse(user.getTwitterUrl()));
                    if (intent3.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent3);
                    }
                }

                break;

            case R.id.tv_profile_linkedin_url:


                if (!user.getLinkedinUrl().isEmpty()) {
                    Intent intent3 = new Intent(Intent.ACTION_VIEW);
                    intent3.setData(Uri.parse(user.getLinkedinUrl()));
                    if (intent3.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent3);
                    }
                }

                break;
        }

    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_profile_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.my_feedback:

                Intent intent = new Intent(mContext, FeedbackActivity.class);
                intent.putExtra("memberId", PreferenceUtils.getId(mContext));
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    Uri imageUri = result.getUri();
                    if (changingCover) {

                        if (imageUri != null) {

                            filePathList.clear();
                            filePathList.add(0, imageUri);

                            if (cd.isConnectingToInternet()) {
                                requestPresenter.editCoverImage(UPDATE_COVER_PIC, PreferenceUtils.getId(mContext), filePathList);
                            } else {
                                Utills.noInternetConnection(mContext);
                            }

                        }


                    } else {

                        if (imageUri != null) {

                            filePathList.clear();
                            filePathList.add(0, imageUri);

                            if (cd.isConnectingToInternet()) {
                                requestPresenter.editProfileImage(UPDATE_PROFILE_PIC, PreferenceUtils.getId(mContext), filePathList);
                            } else {
                                Utills.noInternetConnection(mContext);
                            }

                        }
                    }
                }

                break;

            /*case StaticData.CAMERA_IMAGE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    Uri cameraImageUri = ((HomeActivity) mContext).mCameraImageUri;
                    if (changingCover) {

                        if (cameraImageUri != null) {

                            filePathList.clear();
                            filePathList.add(0, cameraImageUri);

                            if (cd.isConnectingToInternet()) {
                                requestPresenter.editCoverImage(UPDATE_COVER_PIC, PreferenceUtils.getId(mContext), filePathList);
                            } else {
                                Utills.noInternetConnection(mContext);
                            }

                        }


                    } else {

                        if (cameraImageUri != null) {

                            filePathList.clear();
                            filePathList.add(0, cameraImageUri);

                            if (cd.isConnectingToInternet()) {
                                requestPresenter.editProfileImage(UPDATE_PROFILE_PIC, PreferenceUtils.getId(mContext), filePathList);
                            } else {
                                Utills.noInternetConnection(mContext);
                            }

                        }
                    }
                }

                break;*/

            /*case StaticData.GALLERY_IMAGE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    Uri cameraImageUri = data.getData();
                    if (changingCover) {

                        cameraImageUri = data.getData();

                        filePathList.clear();
                        filePathList.add(0, cameraImageUri);

                        if (cd.isConnectingToInternet()) {
                            requestPresenter.editCoverImage(UPDATE_COVER_PIC, PreferenceUtils.getId(mContext), filePathList);
                        } else {
                            Utills.noInternetConnection(mContext);
                        }

                    } else {

                        cameraImageUri = data.getData();

                        filePathList.clear();
                        filePathList.add(0, cameraImageUri);

                        if (cd.isConnectingToInternet()) {
                            requestPresenter.editProfileImage(UPDATE_PROFILE_PIC, PreferenceUtils.getId(mContext), filePathList);
                        } else {
                            Utills.noInternetConnection(mContext);
                        }

                    }
                }

                break;*/

            case StaticData.EVENT_DETAIL:


            case StaticData.CREATE_EVENT:

                List<Fragment> fragmentList = getChildFragmentManager().getFragments();

                for (Fragment fragment : fragmentList) {

                    if (fragment instanceof MyEventFragment) {

                        ((MyEventFragment) fragment).onActivityResult(requestCode, resultCode, data);

                    }
                }

                break;

            case StaticData.GROUP_DETAIL:


            case StaticData.CREATE_GROUP:

                List<Fragment> fragmentList1 = getChildFragmentManager().getFragments();

                for (Fragment fragment : fragmentList1) {

                    if (fragment instanceof MyGroupFragment) {

                        ((MyGroupFragment) fragment).onActivityResult(requestCode, resultCode, data);

                    }
                }

                break;

            case StaticData.ADD_STUFF:

                List<Fragment> fragmentList2 = getChildFragmentManager().getFragments();

                for (Fragment fragment : fragmentList2) {

                    if (fragment instanceof MyGroupFragment) {

                        ((MyGroupFragment) fragment).onActivityResult(requestCode, resultCode, data);

                    }
                }

                break;

            case StaticData.MY_WISHLIST:

                if (resultCode == Activity.RESULT_OK) {

                    String stufflistString = data.getStringExtra("stufflist");
                    user.setStufflist(stufflistString);

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

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case UPDATE_COVER_PIC:

                showAlert(mContext, getString(R.string.app_name), message);

                break;


            case UPDATE_PROFILE_PIC:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case EDIT_FB_URL:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case EDIT_TWITTER_URL:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case EDIT_LINKEDIN_URL:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case EDIT_ABOUT_INFO:

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

            case GET_PROFILE_DATA:

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
                    String customStufflist = data.optString("custom_stufflist");
                    String stufflist = data.optString("stufflist");
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
                            .withCustomStufflist(customStufflist)
                            .withStufflist(stufflist)
                            .withStuffPrivacy(stuffPrivacy);

                    PreferenceUtils.setName(mContext, name);
                    PreferenceUtils.setProfilePicUrl(mContext, profileImage);

                    setData();

                    cl_main.setVisibility(View.VISIBLE);

                    if (tv_about_me_desc.getLineCount() > 2) {
                        makeTextViewResizable(tv_about_me_desc, 2, "more..", true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case UPDATE_COVER_PIC:

                try {

                    String fileUrl = Data.getString("AttachFileURL");

                    Glide.with(mContext)
                            .load(fileUrl + StaticData.THUMB_300)
                            .asBitmap()
                            .into(img_cover);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case UPDATE_PROFILE_PIC:

                try {

                    String profileImageUrl = Data.getString("AttachFileURL");
                    PreferenceUtils.setProfilePicUrl(mContext, profileImageUrl);

                    Glide.with(mContext)
                            .load(profileImageUrl + StaticData.THUMB_100)
                            .asBitmap()
                            .into(img_user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case EDIT_FB_URL:

                try {

                    JSONObject request = Data.getJSONObject("req");
                    String fbUrl = request.optString("url");
                    user.setFbUrl(fbUrl);
                    tv_fb_url.setText(fbUrl);
                    et_fb_url.setVisibility(View.GONE);
                    img_cross_fb_url.setVisibility(View.GONE);
                    img_correct_fb_url.setVisibility(View.GONE);
                    img_edit_fb_url.setVisibility(View.VISIBLE);
                    tv_fb_url.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case EDIT_TWITTER_URL:

                try {

                    JSONObject request = Data.getJSONObject("req");
                    String twitterUrl = request.optString("url");
                    user.setTwitterUrl(twitterUrl);
                    tv_twitter_url.setText(twitterUrl);
                    et_twitter_url.setVisibility(View.GONE);
                    img_cross_twitter_url.setVisibility(View.GONE);
                    img_correct_twitter_url.setVisibility(View.GONE);
                    img_edit_twitter_url.setVisibility(View.VISIBLE);
                    tv_twitter_url.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case EDIT_LINKEDIN_URL:


                try {

                    JSONObject request = Data.getJSONObject("req");
                    String linkedinUrl = request.optString("url");
                    user.setLinkedinUrl(linkedinUrl);
                    tv_linkedin_url.setText(linkedinUrl);
                    et_linkedin_url.setVisibility(View.GONE);
                    img_cross_linkedin_url.setVisibility(View.GONE);
                    img_correct_linkedin_url.setVisibility(View.GONE);
                    img_edit_linkedin_url.setVisibility(View.VISIBLE);
                    tv_linkedin_url.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case EDIT_ABOUT_INFO:

                try {

                    JSONObject request = Data.getJSONObject("req");
                    String summary = request.optString("summary");
                    user.setSummary(summary);
                    tv_about_me_desc.setText(summary);
                    et_about_me_desc.setVisibility(View.GONE);
                    img_cross_about_me.setVisibility(View.GONE);
                    img_correct_about_me.setVisibility(View.GONE);
                    img_edit_about_me.setVisibility(View.VISIBLE);
                    tv_about_me_desc.setVisibility(View.VISIBLE);
                    if (tv_about_me_desc.getLayout().getLineCount() > 2) {
                        makeTextViewResizable(tv_about_me_desc, 2, "more...", true);
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
