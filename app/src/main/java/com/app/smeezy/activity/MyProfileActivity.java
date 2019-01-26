package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static com.app.smeezy.utills.StaticData.CAMERA_IMAGE_REQUEST;
import static com.app.smeezy.utills.StaticData.GALLERY_IMAGE_REQUEST;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, IRequestView {

    public static final String EDIT_PROFILE_BASIC_METHOD = "edit_profile_basic";
    public static final String GET_PROFILE_DATA = "get_profile";
    public static final int WISHLIST_INTENT = 2050;

    @BindView(R.id.img_my_profile)
    ImageView img_my_profile;

    @BindView(R.id.et_myprofile_name)
    EditText et_name;

    @BindView(R.id.et_myprofile_dob)
    EditText et_dob;

    @BindView(R.id.et_myprofile_relationship_status)
    EditText et_relationship_status;

    @BindView(R.id.et_myprofile_phone_number)
    EditText et_phone_number;

    @BindView(R.id.et_myprofile_location)
    EditText et_location;

    @BindView(R.id.et_myprofile_gender)
    EditText et_gender;

    private String latitude = "", longitude = "";
    private String city = "", region = "", country = "", pinCode = "";
    private String dob = "";

    private ArrayList<Uri> filePathList = new ArrayList<>();

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;
    private User user;
    private ArrayList<String> wishList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        //fillForm();

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();

        et_name.setText(PreferenceUtils.getName(mContext));


        Glide.with(mContext)
                .load(PreferenceUtils.getProfilePicUrl(mContext) + StaticData.THUMB_100)
                .asBitmap()
                .placeholder(R.drawable.no_user_white)
                .into(img_my_profile);

        //getProfileData();

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

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_myprofile));
        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        img_logo.setVisibility(View.GONE);
    }

    private void getProfileData() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getProfileData(GET_PROFILE_DATA, PreferenceUtils.getId(mContext), "");
        } else {
            Utills.noInternetConnection(mContext);
        }
    }


    @OnClick({R.id.btn_myprofile_submit, R.id.et_myprofile_dob, R.id.et_myprofile_location, R.id.img_my_profile,
            R.id.et_myprofile_wishlist})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_my_profile:

                selectImagePopUp();

                break;

            case R.id.et_myprofile_dob:

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyProfileActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) - 13);
                dpd.setMaxDate(maxDate);
                dpd.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
                dpd.show(getFragmentManager(), "datePicker");

                break;

            case R.id.et_myprofile_location:

                checkLocationPermissions();

                break;

            case R.id.et_myprofile_wishlist:

                /*Intent wishlistIntent = new Intent(mContext, MyWishlistActivity.class);
                wishlistIntent.putExtra("wishList", user.getStufflist());
                startActivityForResult(wishlistIntent, WISHLIST_INTENT);*/

                break;

            case R.id.btn_myprofile_submit:

                String name = et_name.getText().toString().trim();
                String relationshipStatus = et_relationship_status.getText().toString().trim();
                String phoneNumber = et_phone_number.getText().toString().trim();
                String location = et_location.getText().toString().trim();

                String gender = et_gender.getText().toString().trim();

                if (name.isEmpty() && dob.isEmpty() && location.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.all_fields_required));
                } else if (name.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.name_required));
                } else if (dob.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.dob_required));
                } /*else if (relationshipStatus.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.relationship_status_required));
                }*/ else if (location.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.location_required));
                } /*else if (gender.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gender_required));
                }*/ /*else if (wishList.size() == 0) {
                    showAlert(getString(R.string.app_name), getString(R.string.wishlist_required));
                }*/ /*else if (filePathList.isEmpty() && PreferenceUtils.getProfilePicUrl(mContext).isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.profile_pic_required));
                }*/ else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.editProfileBasic(EDIT_PROFILE_BASIC_METHOD, PreferenceUtils.getId(mContext), name,
                                dob, "", "", location, "", latitude, longitude, city, region,
                                country, pinCode, filePathList);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CAMERA_IMAGE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    if (mCameraImageUri != null) {

                        try {
                            File file = new Compressor(this).setQuality(StaticData.UPLOAD_IMAGE_QUALITY)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(new File(mCameraImageUri.getPath()));

                            mCameraImageUri = Uri.fromFile(file);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        filePathList.clear();
                        filePathList.add(0, mCameraImageUri);

                        Glide.with(mContext)
                                .load(mCameraImageUri)
                                .asBitmap()
                                .placeholder(R.drawable.no_user_white)
                                .into(img_my_profile);

                    }
                }
                break;


            case GALLERY_IMAGE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    mCameraImageUri = data.getData();

                    try {
                        String path = Utills.getPath(mContext, mCameraImageUri);
                        if (path != null) {
                            File file = new Compressor(this).setQuality(StaticData.UPLOAD_IMAGE_QUALITY)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(new File(path));
                            mCameraImageUri = Uri.fromFile(file);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    filePathList.clear();
                    filePathList.add(0, mCameraImageUri);

                    Glide.with(mContext)
                            .load(mCameraImageUri)
                            .asBitmap()
                            .placeholder(R.drawable.no_user_white)
                            .into(img_my_profile);

                }
                break;


            case StaticData.PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);

                    String location = String.format("%s", place.getAddress());

                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                    city = region = country = pinCode = "";

                    try {
                        Geocoder geocoder = new Geocoder(mContext);
                        List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(latitude),
                                Double.parseDouble(longitude), 1);
                        if (addressList.size() > 0) {

                            Address address = addressList.get(0);

                            if (address.getLocality() != null) {
                                city = address.getLocality();
                            }

                            if (address.getAdminArea() != null) {
                                region = address.getAdminArea();
                            }

                            if (address.getCountryName() != null) {
                                country = address.getCountryName();
                            }

                            if (address.getPostalCode() != null) {
                                pinCode = address.getPostalCode();
                            }

                            Log.d("MyProfileActivity", city);
                            Log.d("MyProfileActivity", region);
                            Log.d("MyProfileActivity", country);
                            Log.d("MyProfileActivity", pinCode);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    et_location.setText(location);

                }

                break;

            case WISHLIST_INTENT:

                if (resultCode == Activity.RESULT_OK) {

                    String stufflist = "";
                    stufflist = data.getStringExtra("stufflist");

                    if (stufflist.length() > 0) {
                        String[] wishlistStringArray = stufflist.split(",");
                        wishList = new ArrayList<>(Arrays.asList(wishlistStringArray));
                    } else {
                        wishList = new ArrayList<>();
                    }

                    user.setStufflist(stufflist);

                }

                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        /*Calendar todayDate = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);

        if (Utills.getDiffYears(selectedDate, todayDate) >= 18){

        }else {
            showAlert(mContext.getString(R.string.app_name), mContext.getString(R.string.you_must_be_18_years_old));
        }*/

        dob = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

        String dateToShow = String.format("%02d/%02d/%04d", monthOfYear + 1, dayOfMonth, year);

        et_dob.setText(dateToShow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
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

        if (method.equals(EDIT_PROFILE_BASIC_METHOD)) {
            showAlert(getResources().getString(R.string.app_name), message);
        }

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case EDIT_PROFILE_BASIC_METHOD:

                PreferenceUtils.setName(mContext, et_name.getText().toString().trim());
                PreferenceUtils.setStepFirst(mContext, "Yes");
                Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;

            case GET_PROFILE_DATA:

                try {

                    JSONObject data = Data.getJSONObject("data");
                    String id = data.optString("id");
                    String stufflist = "";
                    stufflist = data.optString("stufflist");

                    user = new User().withId(id)
                            .withStufflist(stufflist);

                    if (stufflist.length() > 0) {
                        String[] wishlistStringArray = stufflist.split(",");
                        wishList = new ArrayList<>(Arrays.asList(wishlistStringArray));
                    } else {
                        wishList = new ArrayList<>();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void fillForm() {
        et_dob.setText("2015-05-25");
        et_relationship_status.setText("Single");
        et_phone_number.setText("9876543210");
    }

}
