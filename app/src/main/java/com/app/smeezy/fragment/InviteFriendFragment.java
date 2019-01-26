package com.app.smeezy.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smeezy.R;
import com.app.smeezy.adapter.FaqAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FaqItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriendFragment extends BaseFragment implements IRequestView {

    private static final String GET_REFERRAL_TEXT = "get_referral_text";

    @BindView(R.id.ll_invite_friend_main)
    LinearLayout ll_main;

    @BindView(R.id.img_invite_friend_copy_code)
    ImageView img_copy_code;

    @BindView(R.id.tv_invite_friend_code)
    TextView tv_code;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    public InviteFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite_friend, container, false);
        ButterKnife.bind(this, view);


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        ll_main.setVisibility(View.GONE);

        getReferralCode();

        return view;
    }

    private void getReferralCode() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getUnreadChatCount(GET_REFERRAL_TEXT, PreferenceUtils.getId(mContext));
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.no_menu, menu);

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

            case GET_REFERRAL_TEXT:

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

            case GET_REFERRAL_TEXT:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    String referralCode = data.optString("referral_code");
                    String referralLink = data.optString("referral_link");
                    String referralShareText = data.optString("referral_share_text");

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        referralShareText = Html.fromHtml(referralShareText).toString();
                    } else {
                        referralShareText = Html.fromHtml(referralShareText, Html.FROM_HTML_MODE_LEGACY).toString();
                    }

                    PreferenceUtils.setReferralShareText(mContext, referralShareText);

                    ll_main.setVisibility(View.VISIBLE);

                    tv_code.setText(referralCode);

                    tv_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, PreferenceUtils.getReferralShareText(mContext));
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        }
                    });

                    img_copy_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", tv_code.getText().toString().trim());
                            if (clipboard != null){
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(mContext, "Referral Code Copied", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

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
