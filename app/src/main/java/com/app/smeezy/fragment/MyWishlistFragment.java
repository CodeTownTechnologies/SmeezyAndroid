package com.app.smeezy.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.HomeActivity;
import com.app.smeezy.adapter.MyWishlistAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.MyWishlistListener;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Stuff;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishlistFragment extends BaseFragment implements View.OnClickListener, IRequestView,
        MyWishlistListener {

    private static final String ADD_STUFF = "add_stuff";
    private static final String ADD_CUSTOM_STUFF = "add_custom_stuff";
    private static final String GET_STUFF_LIST = "get_stuff";
    private static final String DELETE_STUFF = "delete_stuff";

    @BindView(R.id.my_wishlist_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.img_wishlist_add)
    ImageView img_wishlist_add;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<String> wishList;
    private ArrayList<Stuff> stuffList = new ArrayList<>();
    private MyWishlistAdapter mAdapter;
    private int addPosition, deletePosition;
    private Dialog enterItemDialog;
    private String memberId;
    private boolean ownProfile;
    private User user;


    public MyWishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        ButterKnife.bind(this, view);

        String wishlistString = "";

        if (getArguments() != null) {
            memberId = getArguments().getString("memberId");
            ownProfile = getArguments().getBoolean("ownProfile");
            user = (User) getArguments().getSerializable("user");
            wishlistString = user.getStufflist();
        }
        if (wishlistString.length() > 0) {
            String[] wishlistStringArray = wishlistString.split(",");
            wishList = new ArrayList<>(Arrays.asList(wishlistStringArray));
        } else {
            wishList = new ArrayList<>();
        }

        if (ownProfile) {
            img_wishlist_add.setVisibility(View.VISIBLE);
        } else {
            img_wishlist_add.setVisibility(View.GONE);
        }


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new MyWishlistAdapter(mContext, stuffList,
                this, ownProfile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


        if (ownProfile) {
            getStuffList();
        } else {
            for (String item : wishList) {

                stuffList.add(new Stuff().withTitle(item)
                        .withIsSelected(true));

            }

            if (stuffList.isEmpty()){
                showEmptyView();
            }else {
                hideEmptyView();
            }

            mAdapter.notifyDataSetChanged();
        }

        return view;
    }

    private void getStuffList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getStuffList(GET_STUFF_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    @OnClick({R.id.img_wishlist_add})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_wishlist_add:

                showEnterItemDialog();

                break;
        }

    }

    public void onAdd(int position) {
        StringBuilder stringBuilder = new StringBuilder();

        int length = stuffList.size();

        for (int i = 0; i < length; i++) {
            if (stuffList.get(i).getIsSelected()) {
                stringBuilder.append(stuffList.get(i).getTitle());
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(stuffList.get(position).getTitle());


        if (cd.isConnectingToInternet()) {
            addPosition = position;
            startProgressBar();
            requestPresenter.saveStuffList(ADD_STUFF, PreferenceUtils.getId(mContext),
                    stringBuilder.toString());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onRemove(int position) {

        StringBuilder stringBuilder = new StringBuilder();

        int length = stuffList.size();

        for (int i = 0; i < length; i++) {
            if (i != position && stuffList.get(i).getIsSelected()) {
                stringBuilder.append(stuffList.get(i).getTitle());
                stringBuilder.append(",");
            }
        }

        if (cd.isConnectingToInternet()) {
            deletePosition = position;
            startProgressBar();
            requestPresenter.saveStuffList(DELETE_STUFF, PreferenceUtils.getId(mContext),
                    stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1)
                            : stringBuilder.toString());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }


    @Override
    public void showLoadingProgressBar() {

    }

    @Override
    public void hideLoadingProgressBar() {
        dismissProgressBar();
    }

    @Override
    public void Failed(String method, String message) {
        switch (method) {

            case ADD_STUFF:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case ADD_CUSTOM_STUFF:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case DELETE_STUFF:

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

            case GET_STUFF_LIST:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    JSONArray stuffArray = data.optJSONArray("stuff_list");

                    int length = stuffArray.length();


                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        Stuff stuff = gson.fromJson(stuffArray.getJSONObject(i).toString(), Stuff.class);

                        stuff.setIsSelected(false);

                        for (String item : wishList) {

                            if (item.equals(stuff.getTitle())) {
                                stuff.setIsSelected(true);
                                break;
                            }
                        }

                        stuffList.add(stuff);

                    }

                    for (String item : wishList) {

                        boolean itemPresent = false;

                        for (Stuff stuff : stuffList) {
                            if (item.equals(stuff.getTitle())) {
                                itemPresent = true;
                                break;
                            }
                        }

                        if (!itemPresent) {
                            stuffList.add(new Stuff().withTitle(item)
                                    .withIsSelected(true));
                        }
                    }

                    sortStuffList();

                    final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(mAdapter);

                    recyclerView.addItemDecoration(headersDecoration);

                    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            headersDecoration.invalidateHeaders();
                        }
                    });

                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case ADD_STUFF:

                stuffList.get(addPosition).setIsSelected(true);
                sortStuffList();

                mAdapter.notifyDataSetChanged();

                break;

            case ADD_CUSTOM_STUFF:


                if (enterItemDialog != null && enterItemDialog.isShowing()) {
                    String itemName = ((TextView) enterItemDialog.findViewById(R.id.et_item)).getText().toString();
                    stuffList.add(new Stuff().withTitle(itemName)
                            .withIsSelected(true));

                    sortStuffList();

                    enterItemDialog.dismiss();

                    mAdapter.notifyDataSetChanged();
                }


                break;

            case DELETE_STUFF:

                if (stuffList.get(deletePosition).getId() == null) {
                    stuffList.remove(deletePosition);
                } else {
                    stuffList.get(deletePosition).setIsSelected(false);
                    sortStuffList();
                }

                mAdapter.notifyDataSetChanged();

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        tv_empty_view.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        tv_empty_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }



    private void sortStuffList() {
        Collections.sort(stuffList, new Comparator<Stuff>() {
            @Override
            public int compare(Stuff o1, Stuff o2) {

                int value = 0;

                if (o1.getIsSelected() && !o2.getIsSelected()) {
                    value = -1;
                } else if (!o1.getIsSelected() && o2.getIsSelected()) {
                    value = 1;
                }

                return value;
            }
        });

    }

    private void showEnterItemDialog() {

        enterItemDialog = new Dialog(mContext);
        enterItemDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        enterItemDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        enterItemDialog.setCanceledOnTouchOutside(false);
        enterItemDialog.setContentView(R.layout.custom_wishlist_item_dialog);
        enterItemDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_save = (Button) enterItemDialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) enterItemDialog.findViewById(R.id.btn_cancel);

        final EditText et_item = (EditText) enterItemDialog.findViewById(R.id.et_item);

        et_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    enterItemDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        et_item.requestFocus();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utills.hideSoftKeyboard((HomeActivity) mContext);
                enterItemDialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = et_item.getText().toString().trim();

                if (itemName.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.item_name_required));
                } else {

                    StringBuilder stringBuilder = new StringBuilder();

                    int length = stuffList.size();

                    for (int i = 0; i < length; i++) {
                        if (stuffList.get(i).getIsSelected()) {
                            stringBuilder.append(stuffList.get(i).getTitle());
                            stringBuilder.append(",");
                        }
                    }

                    stringBuilder.append(itemName);

                    if (cd.isConnectingToInternet()) {
                        Utills.hideSoftKeyboard((HomeActivity)mContext);
                        startProgressBar();
                        requestPresenter.saveStuffList(ADD_CUSTOM_STUFF, PreferenceUtils.getId(mContext),
                                stringBuilder.toString());
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        enterItemDialog.show();

    }


}
