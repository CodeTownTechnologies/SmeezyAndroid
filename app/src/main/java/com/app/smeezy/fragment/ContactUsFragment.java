package com.app.smeezy.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends BaseFragment implements View.OnClickListener, IRequestView {

    private static final String SEND = "send";


    @BindView(R.id.et_contact_us_name)
    EditText et_name;

    @BindView(R.id.et_contact_us_email)
    EditText et_email;

    @BindView(R.id.et_contact_us_subject)
    EditText et_subject;

    @BindView(R.id.et_contact_us_message)
    EditText et_message;

    @BindView(R.id.tv_contact_us_email)
    TextView tv_contact_us_email;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        tv_contact_us_email.setText(PreferenceUtils.getContactEmail(mContext));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.no_menu, menu);

    }

    @OnClick({R.id.btn_contact_us_send, R.id.tv_contact_us_email, R.id.img_contact_us_copy_email})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_contact_us_email:

                //Utills.show_alert_for_mail(mContext, PreferenceUtils.getContactEmail(mContext));
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + PreferenceUtils.getContactEmail(mContext)));

                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    startActivity(intent);
                }


                break;

            case R.id.btn_contact_us_send:

                String name = et_name.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String subject = et_subject.getText().toString().trim();
                String message = et_message.getText().toString().trim();

                if (name.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.name_required));
                } else if (email.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.email_required));
                } else if (subject.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.subject_required));
                } else if (message.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.message_required));
                }else if (!Utills.isEmailValid(email)) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.provide_a_valid_email_address));
                } else {
                    if (cd.isConnectingToInternet()) {
                        requestPresenter.contactUs(SEND, name, email, subject, message);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

            case R.id.img_contact_us_copy_email:

                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tv_contact_us_email.getText().toString().trim());
                if (clipboard != null){
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(mContext, "Email Copied", Toast.LENGTH_LONG).show();
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

            case SEND:

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

            case SEND:

                et_name.setText("");
                et_email.setText("");
                et_subject.setText("");
                et_message.setText("");

                String message = Data.optString("replyMessage");

                showAlert(mContext, getString(R.string.app_name), message);

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
