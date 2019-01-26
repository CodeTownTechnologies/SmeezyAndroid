package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileDetailsActivity extends BaseActivity implements IRequestView, View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private static final String UPDATE_PROFILE_DETAILS = "update_profile";

    @BindView(R.id.et_edit_profile_email)
    EditText et_email;

    @BindView(R.id.et_edit_profile_name)
    EditText et_name;

    @BindView(R.id.et_edit_profile_dob)
    EditText et_dob;

    @BindView(R.id.et_edit_profile_relationship_status)
    EditText et_relationship_status;

    @BindView(R.id.et_edit_profile_phone_number)
    EditText et_phone_number;

    @BindView(R.id.et_edit_profile_location)
    EditText et_location;


    @BindView(R.id.et_edit_profile_gender)
    EditText et_gender;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private String latitude = "", longitude = "";
    private String city = "", region = "", country = "", pinCode = "";
    private String name, phone, dob, relationshipStatus, gender, location;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_details);
        ButterKnife.bind(this);
        //fillForm();

        user = (User) getIntent().getSerializableExtra("user");

        latitude = user.getLocationLatitude();
        longitude = user.getLocationLongitude();
        city = user.getCity();
        region = user.getRegion();
        country = user.getCountry();
        pinCode = user.getPincode();

        et_email.setText(user.getEmail());
        et_name.setText(user.getName());

        et_name.setSelection(et_name.getText().toString().length());

        et_dob.setText(Utills.getDateInMDYFormat(user.getDob()));
        et_phone_number.setText(user.getPhoneNumber());
        et_location.setText(user.getLocationAddress());
        et_relationship_status.setText(user.getRelationshipStatus());
        et_gender.setText(user.getGender());



        dob = user.getDob();

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();
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
        activity_title.setText(getResources().getString(R.string.title_activity_edit_profile_details));
    }

    @OnClick({R.id.btn_edit_profile_update, R.id.et_edit_profile_dob, R.id.et_edit_profile_location})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.et_edit_profile_dob:

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditProfileDetailsActivity.this,
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

            case R.id.et_edit_profile_location:

                checkLocationPermissions();

                break;

            case R.id.btn_edit_profile_update:

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
                }*/ else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.editProfileBasic(UPDATE_PROFILE_DETAILS, PreferenceUtils.getId(mContext), name,
                                dob, relationshipStatus, phoneNumber, location, gender, latitude, longitude, city, region,
                                country, pinCode, new ArrayList<Uri>());
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

            /*case R.id.btn_edit_profile_cancel:

                finish();

                break;*/
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
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

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    et_location.setText(location);

                }

                break;
        }
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
        switch (method) {

            case UPDATE_PROFILE_DETAILS:

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

            case UPDATE_PROFILE_DETAILS:

                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra(getString(R.string.activity_tag), getString(R.string.activity_edit_profile_details_tag));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;

        }


    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
