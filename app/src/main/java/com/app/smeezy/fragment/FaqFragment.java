package com.app.smeezy.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.FaqAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ChatListItem;
import com.app.smeezy.responsemodels.FaqItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaqFragment extends BaseFragment implements IRequestView{

    private static final String GET_FAQ = "get_faq";

    @BindView(R.id.faq_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.faq_empty_view)
    TextView empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<FaqItem> faqList = new ArrayList<>();
    private FaqAdapter mAdapter;

    public FaqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, view);


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FaqAdapter(mContext, faqList);
        recyclerView.setAdapter(mAdapter);

        getFaqList();

        return view;
    }

    private void getFaqList(){

        if(cd.isConnectingToInternet()){
            requestPresenter.getFaqList(GET_FAQ);
        }else {
            Utills.noInternetConnection(mContext);
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

        switch (method){

            case GET_FAQ:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

        }

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method){

            case GET_FAQ:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    Gson gson = new Gson();

                    for (int i = 0; i < length; i++){

                        FaqItem faqItem = gson.fromJson(data.optJSONObject(i).toString(), FaqItem.class);
                        faqList.add(faqItem);

                    }

                    mAdapter.notifyDataSetChanged();

                    if (faqList.isEmpty()){
                        showEmptyView();
                    }else {
                        hideEmptyView();
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }



                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void showEmptyView(){

        recyclerView.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView(){

        empty_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
