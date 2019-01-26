package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.app.smeezy.responsemodels.Group;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static com.app.smeezy.utills.StaticData.CAMERA_IMAGE_REQUEST;
import static com.app.smeezy.utills.StaticData.GALLERY_IMAGE_REQUEST;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String CREATE_GROUP = "create_group";


    @BindView(R.id.et_create_group_name)
    EditText et_name;

    @BindView(R.id.et_create_group_desc)
    EditText et_desc;

    @BindView(R.id.cb_create_group_private)
    CheckBox cb_private;

    @BindView(R.id.img_create_group)
    ImageView img_group;

    @BindView(R.id.ll_create_group_add_image)
    LinearLayout ll_add_image;


    private ArrayList<Uri> filePathList = new ArrayList<>();

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

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
        activity_title.setText(getResources().getString(R.string.title_activity_create_group));
    }

    @OnClick({R.id.btn_create_group, R.id.img_create_group_add_image,
            R.id.img_create_group})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_create_group_add_image:

                selectImagePopUp();

                break;

            case R.id.img_create_group:

                selectImagePopUp();

                break;

            /*case R.id.btn_create_group_cancel:

                finish();

                break;*/

            case R.id.btn_create_group:

                String title = et_name.getText().toString().trim();
                String description = et_desc.getText().toString().trim();
                String groupType = "public";

                if (cb_private.isChecked()){
                    groupType = "private";
                }

                if (title.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.group_name_required));
                }else if (description.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.group_description_required));
                }/*else if (filePathList.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.group_image_required));
                }*/else {

                    if (cd.isConnectingToInternet()){
                        requestPresenter.createGroup(CREATE_GROUP, PreferenceUtils.getId(mContext), title,
                                description, groupType, filePathList);
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
                        img_group.setVisibility(View.VISIBLE);

                        Glide.with(mContext)
                                .load(mCameraImageUri)
                                .asBitmap()
                                .placeholder(R.drawable.no_user_white)
                                .into(img_group);

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
                    img_group.setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(mCameraImageUri)
                            .asBitmap()
                            .placeholder(R.drawable.no_user_white)
                            .into(img_group);

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

            case CREATE_GROUP:

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

            case CREATE_GROUP:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    Gson gson = new Gson();

                    Group group = gson.fromJson(data.toString(), Group.class);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("group", group);
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
