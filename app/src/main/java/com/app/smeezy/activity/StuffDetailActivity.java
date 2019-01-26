package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StuffDetailActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String COMPOSE_MESSAGE = "compose_message";
    private static final String SEND_STUFF_PAYMENT_REQUEST = "stuff_payment_request";
    private static final String GET_PAYMENT_OPTION_LIST = "get_payment_options";
    private static final String INCREASE_VIEW_COUNT = "increase_view_count";

    @BindView(R.id.tv_stuff_detail_item_name)
    TextView tv_stuff_item_name;

    @BindView(R.id.tv_stuff_detail_category)
    TextView tv_stuff_category;

    @BindView(R.id.tv_stuff_detail_desc)
    TextView tv_stuff_desc;

    @BindView(R.id.img_stuff_detail_item)
    ImageView img_stuff;

    @BindView(R.id.ll_trade_options)
    LinearLayout ll_trade_options;

    @BindView(R.id.ll_share_options)
    LinearLayout ll_share_options;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.img_stuff_detail_user)
    ImageView img_user;

    @BindView(R.id.tv_stuff_detail_user_name)
    TextView tv_user_name;

    @BindView(R.id.tv_stuff_detail_location)
    TextView tv_location;

    @BindView(R.id.tv_stuff_detail_view_location)
    TextView tv_view_location;

//    @BindView(R.id.stuff_detail_payment_recycler_view)
//    RecyclerView payment_recycler_view;

    @BindView(R.id.btn_stuff_detail_buy)
    Button btn_buy;

    @BindView(R.id.btn_stuff_detail_rent)
    Button btn_rent;

    @BindView(R.id.btn_stuff_detail_borrow)
    Button btn_borrow;

    @BindView(R.id.btn_stuff_detail_barter)
    Button btn_barter;

    @BindView(R.id.ll_stuff_detail_buy)
    LinearLayout ll_buy;

    @BindView(R.id.ll_stuff_detail_rent)
    LinearLayout ll_rent;

    @BindView(R.id.ll_stuff_detail_borrow)
    LinearLayout ll_borrow;

    @BindView(R.id.ll_stuff_detail_barter)
    LinearLayout ll_barter;

    @BindView(R.id.ll_stuff_detail_payment)
    LinearLayout ll_payment;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;
    private StuffFeedItem stuffFeedItem;
    private Dialog messageDialog;
    private String from;
//    private ArrayList<PaymentOption> paymentOptionList = new ArrayList<>();
//    private StuffDetailPaymentOptionsAdapter paymentOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();


        stuffFeedItem = (StuffFeedItem) intent.getSerializableExtra("stuffFeedItem");
        from = intent.getStringExtra("from");


        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();

//        paymentOptionsAdapter = new StuffDetailPaymentOptionsAdapter(mContext, paymentOptionList);
//        payment_recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
//        payment_recycler_view.setAdapter(paymentOptionsAdapter);

//        getPaymentOptions();

        setData();
        increaseViewCount();
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
        activity_title.setText(getResources().getString(R.string.title_activity_stuff_detail));
    }

    private void increaseViewCount(){
        if(cd.isConnectingToInternet()){
            requestPresenter.increaseStuffViews(INCREASE_VIEW_COUNT, PreferenceUtils.getId(mContext),
                    stuffFeedItem.getId());
        }
    }

    private void getPaymentOptions() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getPaymentOptionsList(GET_PAYMENT_OPTION_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    private void setData() {

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(stuffFeedItem.getImage() + StaticData.THUMB_300)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img_stuff);

        tv_stuff_item_name.setText(stuffFeedItem.getStuffText());
        tv_stuff_category.setText(stuffFeedItem.getCategoryName());
        tv_stuff_desc.setText(stuffFeedItem.getDescription());

        if (stuffFeedItem.getFriend() == 1 ||
                stuffFeedItem.getAllowed() == 1) {

            Glide.with(mContext)
                    .load(stuffFeedItem.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(img_user);

            tv_user_name.setText(stuffFeedItem.getName());

            img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", stuffFeedItem.getUserId());
                    startActivity(intent);
                }
            });

        } else {

            Glide.with(mContext)
                    .load(R.drawable.no_user_blue)
                    .asBitmap()
                    .into(img_user);

            tv_user_name.setText(getString(R.string.anonymous_user));
        }


        if (stuffFeedItem.getCity().isEmpty() && stuffFeedItem.getRegion().isEmpty()) {
            tv_location.setVisibility(View.GONE);
        } else if (!stuffFeedItem.getCity().isEmpty()) {
            tv_location.setText(String.format("%s", stuffFeedItem.getCity()));
        } else if (!stuffFeedItem.getRegion().isEmpty()) {
            tv_location.setText(String.format("%s", stuffFeedItem.getRegion()));
        } else {
            tv_location.setText(String.format("%s, %s", stuffFeedItem.getCity(), stuffFeedItem.getRegion()));
        }

       /* List<String> stuffOptionList = Arrays.asList(stuffFeedItem.getOptions().split(","));

        for (String s : stuffOptionList){
            if (s.equalsIgnoreCase("trade")){
                ll_trade_options.setVisibility(View.VISIBLE);
            }

            if (s.equalsIgnoreCase("share")){
                ll_share_options.setVisibility(View.VISIBLE);
            }
        }*/

        if (stuffFeedItem.getBuy().equals("Yes")) {
            ll_buy.setVisibility(View.VISIBLE);
            btn_buy.setText(String.format("%s %s", getString(R.string.buy),
                    Utills.moneyConvert(stuffFeedItem.getBuyPrice())));
        } else {
            ll_buy.setVisibility(View.GONE);
        }

        if (stuffFeedItem.getRent().equals("Yes")) {
            ll_rent.setVisibility(View.VISIBLE);
            btn_rent.setText(String.format("%s %s", getString(R.string.rent),
                    Utills.moneyConvert(stuffFeedItem.getRentPrice())));
        } else {
            ll_rent.setVisibility(View.GONE);
        }

        /*if (stuffFeedItem.getBuy().equals("Yes") || stuffFeedItem.getRent().equals("Yes")) {
            ll_payment.setVisibility(View.VISIBLE);
        } else {
            ll_payment.setVisibility(View.GONE);
        }*/

        if (stuffFeedItem.getOptions().contains("trade")) {
            ll_barter.setVisibility(View.VISIBLE);
        } else {
            ll_barter.setVisibility(View.GONE);
        }

        if (stuffFeedItem.getOptions().contains("share")) {
            ll_borrow.setVisibility(View.VISIBLE);
        } else {
            ll_borrow.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.tv_make_offer, R.id.tv_ask_owner, R.id.img_option, R.id.tv_stuff_detail_view_location,
            R.id.img_stuff_detail_item, R.id.btn_stuff_detail_buy, R.id.btn_stuff_detail_rent,
            R.id.btn_stuff_detail_borrow, R.id.btn_stuff_detail_barter})
    @Override
    public void onClick(View view) {

        Intent tradeBorrowIntent = new Intent(mContext, TradeBorrowActivity.class);
        tradeBorrowIntent.putExtra("stuffFeedItem", stuffFeedItem);
        switch (view.getId()) {

            case R.id.img_stuff_detail_item:

                Intent imageIntent = new Intent(mContext, ImageActivity.class);
                ArrayList<String> imageUrlList = new ArrayList<>();
                imageUrlList.add(stuffFeedItem.getImage());
                imageIntent.putStringArrayListExtra("imageUrlList", imageUrlList);
                imageIntent.putExtra("baseUrl", "");
                startActivity(imageIntent);

                break;

            case R.id.tv_make_offer:

                Intent intent = new Intent(mContext, TradeBorrowActivity.class);
                intent.putExtra("stuffFeedItem", stuffFeedItem);
                startActivity(intent);

                break;

            case R.id.tv_ask_owner:

                showMessageDialog("share", "ask_owner");

                break;


            case R.id.img_option:

                tradeBorrowIntent.putExtra("from", from);
                startActivity(tradeBorrowIntent);

                break;

            case R.id.tv_stuff_detail_view_location:

               /* String locationUrl = String.format("%s%s,%s", "https://www.google.com/maps/search/?api=1&query=",
                                stuffFeedItem.getLocationLatitude(), stuffFeedItem.getLocationLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }*/

                Intent mapIntent = new Intent(mContext, MapsActivity.class);
                mapIntent.putExtra("latitude", stuffFeedItem.getLocationLatitude());
                mapIntent.putExtra("longitude", stuffFeedItem.getLocationLongitude());
                mContext.startActivity(mapIntent);

                break;

            /*case R.id.btn_stuff_detail_request_buy_rent:

                String paymentId = "";

                for (int i = 0; i < paymentOptionList.size(); i++) {
                    if (paymentOptionList.get(i).getIsSelected()) {
                        paymentId = paymentOptionList.get(i).getId();
                    }
                }

                if (paymentId.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.payment_type_required));
                } else {
                    if (cd.isConnectingToInternet()) {
                        requestPresenter.sendStuffPaymentRequest(SEND_STUFF_PAYMENT_REQUEST,
                                PreferenceUtils.getId(mContext), stuffFeedItem.getId(), paymentId);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;*/

            case R.id.btn_stuff_detail_buy:

                tradeBorrowIntent.putExtra("from", "buy");
                startActivity(tradeBorrowIntent);

                break;

            case R.id.btn_stuff_detail_rent:

                tradeBorrowIntent.putExtra("from", "rent");
                startActivity(tradeBorrowIntent);

                break;

            case R.id.btn_stuff_detail_borrow:

                tradeBorrowIntent.putExtra("from", "share");
                startActivity(tradeBorrowIntent);

                break;

            case R.id.btn_stuff_detail_barter:

                tradeBorrowIntent.putExtra("from", "trade");
                startActivity(tradeBorrowIntent);

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

            case COMPOSE_MESSAGE:

                showAlert(getString(R.string.app_name), message);

                break;

            case SEND_STUFF_PAYMENT_REQUEST:

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
            /*case GET_PAYMENT_OPTION_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    Gson gson = new Gson();

                    List<String> paymentIds = Arrays.asList(stuffFeedItem.getPaymentAccepted().split(","));

                    for (int i = 0; i < length; i++) {

                        PaymentOption paymentOption = gson.fromJson(data.getJSONObject(i).toString(), PaymentOption.class);
                        paymentOption.setIsSelected(false);

                        for (String id : paymentIds) {
                            if (paymentOption.getId().equals(id.trim())) {
                                paymentOptionList.add(paymentOption);
                                break;
                            }
                        }


                    }

                    paymentOptionsAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;*/

            case COMPOSE_MESSAGE:

                if (messageDialog != null && messageDialog.isShowing()) {
                    messageDialog.dismiss();
                }

                break;

            case SEND_STUFF_PAYMENT_REQUEST:

                try {

                    String message = Data.getString("replyMessage");
                    showAlert(getString(R.string.app_name), message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }


    private void showMessageDialog(final String type, final String action) {

        messageDialog = new Dialog(mContext);
        messageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setContentView(R.layout.custom_message_dialog);
        messageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_send = (Button) messageDialog.findViewById(R.id.btn_message_send);
        Button btn_cancel = (Button) messageDialog.findViewById(R.id.btn_message_cancel);

        final EditText et_message = (EditText) messageDialog.findViewById(R.id.et_message_text);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        /*startProgressBar();
                        requestPresenter.composeMessageStuff(COMPOSE_MESSAGE, PreferenceUtils.getId(mContext),
                                stuffFeedItem.getUserId(), message, type,
                                stuffFeedItem.getId());*/
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        messageDialog.show();

    }
}
