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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Event;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static com.app.smeezy.utills.StaticData.CAMERA_IMAGE_REQUEST;
import static com.app.smeezy.utills.StaticData.GALLERY_IMAGE_REQUEST;

public class CreateEventActivity extends BaseActivity implements View.OnClickListener, IRequestView,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String CREATE_EVENT = "create_event";


    @BindView(R.id.et_create_event_name)
    EditText et_name;

    @BindView(R.id.et_create_event_location)
    EditText et_location;

    @BindView(R.id.et_create_event_date)
    EditText et_date;

    @BindView(R.id.et_create_event_time)
    EditText et_time;

    @BindView(R.id.et_create_event_desc)
    EditText et_desc;

    @BindView(R.id.cb_create_event_private)
    CheckBox cb_private;

    @BindView(R.id.img_create_event)
    ImageView img_event;

    @BindView(R.id.ll_create_event_add_image)
    LinearLayout ll_add_image;

    private String latitude = "", longitude = "";
    private String city = "", region = "", country = "", pinCode = "";
    private String date = "";

    private ArrayList<Uri> filePathList = new ArrayList<>();

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ButterKnife.bind(this);

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
        activity_title.setText(getResources().getString(R.string.title_activity_create_event));
    }

    @OnClick({R.id.btn_create_event, R.id.et_create_event_location,
            R.id.et_create_event_date, R.id.et_create_event_time, R.id.img_create_event_add_image,
            R.id.img_create_event})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.et_create_event_location:

                checkLocationPermissions();

                break;

            case R.id.et_create_event_date:

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEventActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(calendar);
                dpd.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
                dpd.show(getFragmentManager(), "datePicker");

                break;

            case R.id.et_create_event_time:

                Calendar calendar2 = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateEventActivity.this,
                        calendar2.get(Calendar.HOUR_OF_DAY),
                        calendar2.get(Calendar.MINUTE),
                        true
                );
                Timepoint timepoint = new Timepoint(calendar2.get(Calendar.HOUR_OF_DAY),
                        calendar2.get(Calendar.MINUTE));
                tpd.setMinTime(timepoint);
                tpd.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tpd.show(getFragmentManager(), "timePicker");

                break;

            case R.id.img_create_event_add_image:

                selectImagePopUp();

                break;

            case R.id.img_create_event:

                selectImagePopUp();

                break;

            /*case R.id.btn_create_event_cancel:

                finish();

                break;*/

            case R.id.btn_create_event:

                String title = et_name.getText().toString().trim();
                String location = et_location.getText().toString().trim();
                String description = et_desc.getText().toString().trim();
                String time = et_time.getText().toString().trim();
                String startDate = String.format("%s %s", date, time);
                String eventType = "public";

                if (cb_private.isChecked()){
                    eventType = "private";
                }

                if (title.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.event_name_required));
                }else if (location.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.event_location_required));
                }else if (date.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.event_date_required));
                }else if (time.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.event_time_required));
                }else if (description.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.event_description_required));
                }/*else if (filePathList.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.event_image_required));
                }*/else {

                    if (cd.isConnectingToInternet()){
                        requestPresenter.createEvent(CREATE_EVENT, PreferenceUtils.getId(mContext), title,
                                location, startDate, description, eventType, latitude, longitude, filePathList);
                    }else {
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

                        ll_add_image.setVisibility(View.GONE);
                        img_event.setVisibility(View.VISIBLE);

                        Glide.with(mContext)
                                .load(mCameraImageUri)
                                .asBitmap()
                                .placeholder(R.drawable.no_user_white)
                                .into(img_event);

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

                    ll_add_image.setVisibility(View.GONE);
                    img_event.setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(mCameraImageUri)
                            .asBitmap()
                            .placeholder(R.drawable.no_user_white)
                            .into(img_event);

                }
                break;


            case StaticData.PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);

                    String location = String.format("%s", place.getAddress());

                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                    city = region = country = pinCode = "";

                    /*try {
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
                    }*/

                    et_location.setText(location);

                }

                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

        String dateToShow = String.format("%02d/%02d/%04d", monthOfYear + 1, dayOfMonth, year);

        et_date.setText(dateToShow);


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        et_time.setText(String.format("%02d:%02d:%02d", hourOfDay, minute, second));

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

            case CREATE_EVENT:

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

            case CREATE_EVENT:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    Gson gson = new Gson();

                    Event event = gson.fromJson(data.toString(), Event.class);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("event", event);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }catch (JSONException e){
                    e.printStackTrace();
                }

                break;

        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
