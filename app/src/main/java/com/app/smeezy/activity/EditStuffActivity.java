package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.PaymentOptionsAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.PaymentOption;
import com.app.smeezy.responsemodels.StuffCategory;
import com.app.smeezy.responsemodels.UserStuff;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static com.app.smeezy.utills.StaticData.CAMERA_IMAGE_REQUEST;
import static com.app.smeezy.utills.StaticData.GALLERY_IMAGE_REQUEST;

public class EditStuffActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String EDIT_STUFF = "edit_stuff";
    private static final String GET_CATEGORY_LIST = "get_category_list";
    private static final String GET_PAYMENT_OPTION_LIST = "get_payment_options";


    @BindView(R.id.et_edit_stuff_name)
    EditText et_stuff_name;

    @BindView(R.id.et_edit_stuff_desc)
    EditText et_desc;

    @BindView(R.id.cb_edit_stuff_trade)
    CheckBox cb_trade;

    @BindView(R.id.cb_edit_stuff_share)
    CheckBox cb_share;

    @BindView(R.id.img_edit_stuff)
    ImageView img_edit_stuff;

    @BindView(R.id.ll_edit_stuff_edit_image)
    LinearLayout ll_edit_image;

    @BindView(R.id.spinner_edit_stuff_category)
    Spinner spinner_category;

    @BindView(R.id.ll_buy)
    LinearLayout ll_buy;

    @BindView(R.id.ll_rent)
    LinearLayout ll_rent;

    @BindView(R.id.et_edit_stuff_buy_price)
    EditText et_buy_price;

    @BindView(R.id.et_edit_stuff_rent_price)
    EditText et_rent_price;

    @BindView(R.id.switch_edit_stuff_buy)
    SwitchCompat switch_buy;

    @BindView(R.id.switch_edit_stuff_rent)
    SwitchCompat switch_rent;

    @BindView(R.id.edit_stuff_payment_recycler_view)
    RecyclerView recycler_view_payment;

    @BindView(R.id.ll_edit_stuff_payment)
    LinearLayout ll_payment;

    private ArrayList<Uri> filePathList = new ArrayList<>();

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private boolean isManual;
    private ArrayList<StuffCategory> stuffCategoryList = new ArrayList<>();
    private UserStuff stuff;
    private ArrayList<PaymentOption> paymentOptionList = new ArrayList<>();
    private PaymentOptionsAdapter paymentOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stuff);

        ButterKnife.bind(this);

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        Intent intent = getIntent();

        stuff = (UserStuff) intent.getSerializableExtra("userStuff");

        if (stuff.getStuffId().equals("0")) {
            isManual = true;
        } else {
            isManual = false;
        }

        if (isManual) {

            spinner_category.setVisibility(View.VISIBLE);
            et_stuff_name.setFocusableInTouchMode(true);
            et_stuff_name.setLongClickable(true);
            getCategoryList();

        } else {

            spinner_category.setVisibility(View.GONE);
            et_stuff_name.setFocusableInTouchMode(false);
            et_stuff_name.setLongClickable(false);
        }

        List<String> optionsList = Arrays.asList(stuff.getOptions().split(","));

        for (String s : optionsList) {
            if (s.trim().equals("share")) {
                cb_share.setChecked(true);
            }

            if (s.trim().equals("trade")) {
                cb_trade.setChecked(true);
            }
        }

        et_stuff_name.setText(stuff.getStuffText());
        et_desc.setText(stuff.getDescription());

        if (isManual) {
            et_stuff_name.setSelection(et_stuff_name.getText().toString().length());
        } else {
            et_desc.setSelection(et_desc.getText().toString().length());
        }

        if (!stuff.getImage().isEmpty()) {
            ll_edit_image.setVisibility(View.GONE);
            img_edit_stuff.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(stuff.getImage() + StaticData.THUMB_300)
                    .asBitmap()
                    .into(img_edit_stuff);
        }

        switch_buy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_buy.setActivated(true);
                    et_buy_price.setEnabled(true);
                    ll_payment.setVisibility(View.VISIBLE);
                } else {
                    ll_buy.setActivated(false);
                    et_buy_price.setEnabled(false);
                    et_buy_price.setText("");
                    if (!switch_rent.isChecked()) {
                        ll_payment.setVisibility(View.GONE);
                    }
                }
            }
        });

        switch_rent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_rent.setActivated(true);
                    et_rent_price.setEnabled(true);
                    ll_payment.setVisibility(View.VISIBLE);
                } else {
                    ll_rent.setActivated(false);
                    et_rent_price.setEnabled(false);
                    et_rent_price.setText("");
                    if (!switch_buy.isChecked()) {
                        ll_payment.setVisibility(View.GONE);
                    }
                }
            }
        });


        if (stuff.getBuy().equals("Yes")) {
            switch_buy.setChecked(true);
            et_buy_price.setText(stuff.getBuyPrice());
        } else {
            switch_buy.setChecked(false);
        }

        if (stuff.getRent().equals("Yes")) {
            switch_rent.setChecked(true);
            et_rent_price.setText(stuff.getRentPrice());
        } else {
            switch_rent.setChecked(false);
        }

        setUpToolbar();

        paymentOptionsAdapter = new PaymentOptionsAdapter(mContext, paymentOptionList);
        recycler_view_payment.setLayoutManager(new LinearLayoutManager(mContext));
        recycler_view_payment.setAdapter(paymentOptionsAdapter);

        getPaymentOptions();

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
        activity_title.setText(getResources().getString(R.string.title_activity_edit_stuff));
    }

    private void getCategoryList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getStuffList(GET_CATEGORY_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    private void getPaymentOptions() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getPaymentOptionsList(GET_PAYMENT_OPTION_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    private void setPaymentOptions() {

        if (stuff.getPaymentAccepted() != null && !stuff.getPaymentAccepted().isEmpty()) {

            List<String> paymentIds = Arrays.asList(stuff.getPaymentAccepted().split(","));

            for (String paymentId : paymentIds) {
                for (int i = 0; i < paymentOptionList.size(); i++) {
                    if (paymentOptionList.get(i).getId().equals(paymentId)) {
                        paymentOptionList.get(i).setIsSelected(true);
                        break;
                    }
                }
            }

            paymentOptionsAdapter.notifyDataSetChanged();
        }
    }


    @OnClick({R.id.btn_edit_stuff_save, R.id.img_edit_stuff_edit_image,
            R.id.img_edit_stuff})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.img_edit_stuff_edit_image:

                selectImagePopUp();

                break;

            case R.id.img_edit_stuff:

                selectImagePopUp();

                break;

            /*case R.id.btn_edit_stuff_cancel:

                finish();

                break;*/

            case R.id.btn_edit_stuff_save:

                String stuffText = et_stuff_name.getText().toString().trim();
                String description = et_desc.getText().toString().trim();
                String options = "";


                if (cb_trade.isChecked() && cb_share.isChecked()) {
                    options = "trade,share";
                } else if (cb_trade.isChecked()) {
                    options = "trade";
                } else if (cb_share.isChecked()) {
                    options = "share";
                }

                String buy = "", buyPrice = "", rent = "", rentPrice = "";

                if (switch_buy.isChecked()) {
                    buy = "Yes";
                    buyPrice = et_buy_price.getText().toString().trim();
                }

                if (switch_rent.isChecked()) {
                    rent = "Yes";
                    rentPrice = et_rent_price.getText().toString().trim();
                }

                String paymentIds = "";

                if (switch_buy.isChecked() || switch_rent.isChecked()) {
                    StringBuilder paymentIdString = new StringBuilder();

                    for (int i = 0; i < paymentOptionList.size(); i++) {
                        if (paymentOptionList.get(i).getIsSelected()) {
                            paymentIdString.append(paymentOptionList.get(i).getId());
                            paymentIdString.append(",");
                        }
                    }

                    paymentIds = paymentIdString.toString().trim();

                    if (paymentIds.length() > 1) {
                        paymentIds = paymentIds.substring(0, paymentIds.length() - 1);
                    }
                }

                if (isManual) {

                    StuffCategory stuffCategory = (StuffCategory) spinner_category.getSelectedItem();

                    if (stuffCategory.getId().equals("-1")) {
                        showAlert(getString(R.string.app_name), getString(R.string.category_required));
                    } else if (stuffText.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.item_name_required));
                    } else if (options.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.item_option_required));
                    } else if (switch_buy.isChecked() && buyPrice.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.buy_price_required));
                    } else if (switch_buy.isChecked() && Integer.parseInt(buyPrice) <= 0) {
                        showAlert(getString(R.string.app_name), getString(R.string.buy_price_must_be_greater_than_0));
                    } else if (switch_rent.isChecked() && rentPrice.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.rent_price_required));
                    } else if (switch_rent.isChecked() && Integer.parseInt(rentPrice) <= 0) {
                        showAlert(getString(R.string.app_name), getString(R.string.rent_price_must_be_greater_than_0));
                    } else if ((switch_buy.isChecked() || switch_rent.isChecked()) && paymentIds.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.payment_type_required));
                    } else {

                        if (cd.isConnectingToInternet()) {

                            requestPresenter.manageStuffList(EDIT_STUFF, PreferenceUtils.getId(mContext),
                                    "edit", stuffText, description, options, stuff.getId(), "",
                                    stuffCategory.getId(), buy, buyPrice, rent, rentPrice, paymentIds, filePathList);

                        } else {
                            Utills.noInternetConnection(mContext);
                        }

                    }

                } else {
                    if (options.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.item_option_required));
                    } else if (switch_buy.isChecked() && buyPrice.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.buy_price_required));
                    } else if (switch_buy.isChecked() && Integer.parseInt(buyPrice) <= 0) {
                        showAlert(getString(R.string.app_name), getString(R.string.buy_price_must_be_greater_than_0));
                    } else if (switch_rent.isChecked() && rentPrice.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.rent_price_required));
                    } else if (switch_rent.isChecked() && Integer.parseInt(rentPrice) <= 0) {
                        showAlert(getString(R.string.app_name), getString(R.string.rent_price_must_be_greater_than_0));
                    } else if ((switch_buy.isChecked() || switch_rent.isChecked()) && paymentIds.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.payment_type_required));
                    } else {
                        if (cd.isConnectingToInternet()) {
                            requestPresenter.manageStuffList(EDIT_STUFF, PreferenceUtils.getId(mContext),
                                    "edit", "", description, options, stuff.getId(), stuff.getStuffId(),
                                    stuff.getCategoryId(), buy, buyPrice, rent, rentPrice, paymentIds, filePathList);
                        } else {
                            Utills.noInternetConnection(mContext);
                        }
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        filePathList.clear();
                        filePathList.add(0, mCameraImageUri);

                        ll_edit_image.setVisibility(View.GONE);
                        img_edit_stuff.setVisibility(View.VISIBLE);

                        Glide.with(mContext)
                                .load(mCameraImageUri)
                                .asBitmap()
                                .into(img_edit_stuff);

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    filePathList.clear();
                    filePathList.add(0, mCameraImageUri);

                    ll_edit_image.setVisibility(View.GONE);
                    img_edit_stuff.setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(mCameraImageUri)
                            .asBitmap()
                            .into(img_edit_stuff);

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

            case EDIT_STUFF:

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

            case GET_CATEGORY_LIST:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    JSONArray stuffCategoryArray = data.optJSONArray("category_list");

                    int length = stuffCategoryArray.length();

                    stuffCategoryList.add(new StuffCategory().withId("-1")
                            .withTitle(getString(R.string.select_category))
                            .withSlug(""));

                    int selection = 0;

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        StuffCategory stuffCategory = gson.fromJson(
                                stuffCategoryArray.getJSONObject(i).toString(), StuffCategory.class);

                        if (stuffCategory.getId().equals(stuff.getCategoryId())) {
                            selection = i + 1;
                        }

                        stuffCategoryList.add(stuffCategory);

                    }

                    ArrayAdapter<StuffCategory> adapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_list_item_1, stuffCategoryList);

                    spinner_category.setAdapter(adapter);

                    spinner_category.setSelection(selection);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case GET_PAYMENT_OPTION_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    Gson gson = new Gson();

                    for (int i = 0; i < length; i++) {

                        PaymentOption paymentOption = gson.fromJson(data.getJSONObject(i).toString(), PaymentOption.class);
                        paymentOption.setIsSelected(false);
                        paymentOptionList.add(paymentOption);

                    }

                    paymentOptionsAdapter.notifyDataSetChanged();

                    setPaymentOptions();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case EDIT_STUFF:

                sendBroadcast(new Intent(StaticData.STUFF_REFRESH_BROADCAST));
                finish();

                break;

        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
