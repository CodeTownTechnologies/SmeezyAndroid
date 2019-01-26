package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.ReportReason;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String REPORT = "report";
    private static final String GET_REPORT_REASON_LIST = "get_report_reason";

    @BindView(R.id.ll_report_main)
    LinearLayout ll_main;

    @BindView(R.id.spinner_report)
    Spinner spinner_report;

    @BindView(R.id.et_report_description)
    EditText et_description;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<ReportReason> reasonArrayList = new ArrayList<>();

    private FeedItem feedItem;
    private String taskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        feedItem = (FeedItem) getIntent().getSerializableExtra("feedItem");
        taskType = getIntent().getStringExtra("taskType");


        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();

        getReasonList();
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
        activity_title.setText(getResources().getString(R.string.title_activity_report));
    }


    private void getReasonList(){
        if (cd.isConnectingToInternet()){
            requestPresenter.getReportReasonList(GET_REPORT_REASON_LIST, PreferenceUtils.getId(mContext));
        }else {
            Utills.noInternetConnection(mContext);
        }
    }


    @OnClick({R.id.btn_report_submit})
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_report_submit:

                String reportDescription = et_description.getText().toString().trim();
                ReportReason reportReason = (ReportReason) spinner_report.getSelectedItem();

                if (reportReason.getId().equals("-1")){
                    showAlert(getString(R.string.app_name), getString(R.string.reason_required));
                }else if (reportDescription.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.description_required));
                }else {
                    if (cd.isConnectingToInternet()){
                        requestPresenter.reportPost(REPORT, PreferenceUtils.getId(mContext), feedItem.getActivityId(),
                                feedItem.getId(), reportDescription, reportReason.getId(), taskType);
                    }else {
                        Utills.noInternetConnection(mContext);
                    }
                }



                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

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
        switch (method){

            case GET_REPORT_REASON_LIST:

                showAlert(getString(R.string.app_name), message);

                break;

            case REPORT:

                showAlert(getString(R.string.app_name), message);

                break;

        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {
        switch (method){

            case GET_REPORT_REASON_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    Gson gson = new Gson();

                    reasonArrayList.add(new ReportReason().withId("-1").withTitle(getString(R.string.select_reason)));

                    for (int i = 0; i < data.length(); i++){

                        ReportReason reportReason = gson.fromJson(data.getJSONObject(i).toString(), ReportReason.class);
                        reasonArrayList.add(reportReason);

                    }

                    ArrayAdapter<ReportReason> adapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_list_item_1, reasonArrayList);

                    spinner_report.setAdapter(adapter);

                    spinner_report.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (reasonArrayList.get(position).getId().equals("3")
                                    || reasonArrayList.get(position).getId().equals("4")
                                    || reasonArrayList.get(position).getId().equals("5")){
                                showAlert(getString(R.string.app_name),
                                        getString(R.string.if_there_is_immediate_danger));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    ll_main.setVisibility(View.VISIBLE);


                }catch (JSONException e){
                    e.printStackTrace();
                }


                break;



            case REPORT:

                Intent resultIntent = new Intent();
                resultIntent.putExtra("feedItem", feedItem);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                break;


        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
