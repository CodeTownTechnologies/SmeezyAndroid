package com.app.smeezy.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.app.smeezy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kipl146 on 4/19/2016.
 */
public class CustomActionDialog extends DialogFragment {

    @BindView(R.id.txt_title)
    public TextView mTxtTitle;

    @BindView(R.id.txt_message)
    public TextView mTxtMessage;

    @BindView(R.id.txt_action1)
    public TextView mTxtAction1;

    @BindView(R.id.txt_action2)
    public TextView mTxtAction2;

    @BindView(R.id.txt_action3)
    public TextView mTxtAction3;

    private IActionSelectionListener iActionSelectionListener;

    public interface IActionSelectionListener {
        public void onActionSelected(int actionType);
    }

    public void setIActionSelectionListener(IActionSelectionListener iActionSelectionListener) {
        this.iActionSelectionListener = iActionSelectionListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_app_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        ButterKnife.bind(this, view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            String action1 = "";
            String action2 = "";
            String action3 = "";

            if (getArguments() != null) {
                if (getArguments().containsKey("title")) {
                    mTxtTitle.setText(getArguments().getString("title"));
                    mTxtTitle.setVisibility(View.VISIBLE);
                } else {
                    mTxtTitle.setVisibility(View.GONE);
                }
                if (getArguments().containsKey("message")) {
                    mTxtMessage.setText(getArguments().getString("message"));
                }
                if (getArguments().containsKey("action1")) {
                    action1 = getArguments().getString("action1");
                    mTxtAction1.setText(action1);
                }
                if (getArguments().containsKey("action2")) {
                    action2 = getArguments().getString("action2");
                    mTxtAction2.setText(action2);
                }
                if (getArguments().containsKey("action3")) {
                    action3 = getArguments().getString("action3");
                }
            }

            mTxtAction1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActionSelected(1);
                    if (iActionSelectionListener != null) {
                        iActionSelectionListener.onActionSelected(1);
                    }
                    dismiss();
                }
            });

            mTxtAction2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActionSelected(2);
                    if (iActionSelectionListener != null) {
                        iActionSelectionListener.onActionSelected(2);
                    }
                    dismiss();
                }
            });

            mTxtAction3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setActionSelected(3);
                    if (iActionSelectionListener != null) {
                        iActionSelectionListener.onActionSelected(3);
                    }
                    dismiss();
                }
            });

            if (!TextUtils.isEmpty(action3)) {
                mTxtAction3.setText(action3);
                mTxtAction3.setVisibility(View.VISIBLE);
                mTxtAction1.setVisibility(View.GONE);
                mTxtAction2.setVisibility(View.GONE);
            } else {
                mTxtAction3.setVisibility(View.GONE);
                mTxtAction1.setVisibility(View.VISIBLE);
                mTxtAction2.setVisibility(View.VISIBLE);
                setActionSelected(2);
            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

    private void setActionSelected(int action) {

        switch (action) {
            case 1:
                mTxtAction2.setSelected(false);
                mTxtAction1.setSelected(true);
                break;

            case 2:
                mTxtAction1.setSelected(false);
                mTxtAction2.setSelected(true);
                break;
        }

    }

}
