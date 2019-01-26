package com.app.smeezy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.app.smeezy.R;
import com.app.smeezy.adapter.PostImageAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.PostImageClickListener;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Group;
import com.app.smeezy.responsemodels.Info;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static com.app.smeezy.utills.StaticData.CAMERA_IMAGE_REQUEST;
import static com.app.smeezy.utills.StaticData.CAMERA_VIDEO_REQUEST;
import static com.app.smeezy.utills.StaticData.GALLERY_IMAGE_REQUEST;
import static com.app.smeezy.utills.StaticData.GALLERY_VIDEO_REQUEST;

public class PostActivity extends BaseActivity implements View.OnClickListener, IRequestView, EasyVideoCallback,
        PostImageClickListener {

    private static final String POST_TEXT = "post_text";
    private static final String POST_WITH_IMAGE = "post_with_image";
    private static final String POST_WITH_VIDEO_UPLOAD = "post_with_video_upload";
    private static final String GET_VIDEO_URL_DETAILS = "get_video_url_details";
    private static final String POST_WITH_VIDEO_URL = "post_with_video_url";
    private static final String GET_LINK_DETAILS = "get_link_details";
    private static final String POST_WITH_LINK = "post_with_link";

    private static final String TYPE_TEXT = "text";
    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_VIDEO = "video";
    private static final String TYPE_LINK = "link";

    private static final String VIDEO_SUB_TYPE_UPLOAD = "upload";
    private static final String VIDEO_SUB_TYPE_LINK = "link";


    @BindView(R.id.tv_post_name)
    TextView tv_name;

    @BindView(R.id.tv_post_date)
    TextView tv_date;

    @BindView(R.id.img_post_user)
    ImageView img_post_user;

    @BindView(R.id.et_post_message)
    EditText et_message;

    @BindView(R.id.vid_post)
    EasyVideoPlayer vid_post;

    @BindView(R.id.post_img_recycler_view)
    RecyclerView imgRecyclerView;

    @BindView(R.id.rel_post_video_link)
    RelativeLayout rel_video_link;

    @BindView(R.id.img_post_video_link)
    ImageView img_video_link;

    @BindView(R.id.img_post_video_link_play)
    ImageView img_video_link_play;

    @BindView(R.id.ll_post_link)
    LinearLayout ll_post_link;

    @BindView(R.id.tv_post_link_url)
    TextView tv_post_link_url;

    @BindView(R.id.tv_post_link_url_desc)
    TextView tv_post_link_url_desc;

    @BindView(R.id.img_post_link)
    ImageView img_post_link;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private Dialog videoUrlDialog;
    private Dialog linkDialog;
    private ArrayList<Uri> imageUriList = new ArrayList<>();
    private ArrayList<Uri> videoUriList = new ArrayList<>();

    private PostImageAdapter postImageAdapter;


    private String postType = TYPE_TEXT;
    private String videoSubType = VIDEO_SUB_TYPE_UPLOAD;
    private Info videoInfo;
    private Info linkInfo;

    private String postPrivacy;
    private boolean groupPost;
    private Group group;
    private String groupId;
    private String previousTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        groupPost = intent.getBooleanExtra("groupPost", false);

        if (groupPost) {
            group = (Group) intent.getSerializableExtra("group");
            groupId = group.getId();
        }

        if (intent.getStringExtra("postPrivacy") != null) {
            postPrivacy = intent.getStringExtra("postPrivacy");
            if (postPrivacy.equals("community")) {
                et_message.setHint(getString(R.string.whats_happening_in_community));
            } else if (postPrivacy.equals("friends")) {
                et_message.setHint(getString(R.string.post_things_here_just_for_fun));
            }
        } else {
            et_message.setHint(getString(R.string.whats_happening_in_community));
        }


        previousTag = getIntent().getStringExtra(getString(R.string.activity_tag));

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        postImageAdapter = new PostImageAdapter(mContext, imageUriList, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        imgRecyclerView.setLayoutManager(gridLayoutManager);
        imgRecyclerView.setAdapter(postImageAdapter);

        vid_post.setCallback(this);
        vid_post.setHideControlsOnPlay(true);

        tv_name.setText(PreferenceUtils.getName(mContext));

        Glide.with(mContext)
                .load(PreferenceUtils.getProfilePicUrl(mContext) + StaticData.THUMB_100)
                .asBitmap()
                .placeholder(R.drawable.no_user_blue)
                .into(img_post_user);

        Date currentDate = new Date();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd");
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");
        String outputDate = outputDateFormat.format(currentDate);
        String outputTime = outputTimeFormat.format(currentDate);
        String outputDateTime = String.format("%s, at %s", outputDate, outputTime);

        tv_date.setText(outputDateTime);

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
        activity_title.setText(getResources().getString(R.string.title_activity_post));
    }

    @OnClick({R.id.tv_post_image, R.id.tv_post_video, R.id.tv_post_link, R.id.img_post_video_link_play})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_post_image:

                if (imageUriList.size() < 4) {
                    selectImagePopUp();
                } else {
                    showAlert(getString(R.string.app_name), getString(R.string.max_four_images_allowed));
                }

                break;

            case R.id.tv_post_video:

                showVideoTypeDialog();

                break;

            case R.id.tv_post_link:

                showLinkDialog();

                break;

            case R.id.img_post_video_link_play:


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(videoInfo.getUrl()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
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

                        showImageRecyclerView();
                        videoUriList.clear();

                        try {
                            File file = new Compressor(this).setQuality(StaticData.UPLOAD_IMAGE_QUALITY)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .compressToFile(new File(mCameraImageUri.getPath()));

                            mCameraImageUri = Uri.fromFile(file);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        imageUriList.add(0, mCameraImageUri);

                        postImageAdapter.notifyDataSetChanged();

                        postType = TYPE_IMAGE;

                    }
                }
                break;

            case GALLERY_IMAGE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    mCameraImageUri = data.getData();

                    showImageRecyclerView();
                    videoUriList.clear();

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

                    imageUriList.add(0, mCameraImageUri);

                    postImageAdapter.notifyDataSetChanged();

                    postType = TYPE_IMAGE;

                }
                break;

            case CAMERA_VIDEO_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    if (mCameraVideoUri != null) {


                        showVideoView();
                        imageUriList.clear();
                        videoUriList.clear();
                        videoUriList.add(0, mCameraVideoUri);

                        vid_post.setSource(mCameraVideoUri);

                        /*img_post_play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mCameraVideoUri != null) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(mCameraVideoUri.getPath()), "video/mp4");
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                }
                            }
                        });*/

                        postType = TYPE_VIDEO;
                        videoSubType = VIDEO_SUB_TYPE_UPLOAD;

                    }
                }

                break;

            case GALLERY_VIDEO_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    mCameraVideoUri = data.getData();

                    showVideoView();
                    imageUriList.clear();
                    videoUriList.clear();
                    videoUriList.add(0, mCameraVideoUri);

                    vid_post.setSource(mCameraVideoUri);

                    /*img_post_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mCameraVideoUri != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(mCameraVideoUri, "video/mp4");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            }
                        }
                    });*/

                }
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vid_post.isPlaying()) {
            vid_post.pause();
        }
    }

    @Override
    public void onAddImageListener(int position) {
        selectImagePopUp();
    }

    @Override
    public void onImageRemoveClickListener(int position) {

        imageUriList.remove(position);
        postImageAdapter.notifyDataSetChanged();
        if (imageUriList.isEmpty()) {
            imgRecyclerView.setVisibility(View.GONE);
            postType = TYPE_TEXT;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_post_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;

            case R.id.post:

                String description = et_message.getText().toString().trim();

                if (description.isEmpty()) {

                    showAlert(getString(R.string.app_name), getString(R.string.description_required));

                } else if (cd.isConnectingToInternet()) {
                    if (postType.equals(TYPE_TEXT)) {
                        requestPresenter.postText(POST_TEXT, PreferenceUtils.getId(mContext), description,
                                postPrivacy, groupId);
                    } else if (postType.equals(TYPE_IMAGE)) {
                        requestPresenter.postWithImage(POST_WITH_IMAGE, PreferenceUtils.getId(mContext),
                                description, imageUriList, postPrivacy, groupId);
                    } else if (postType.equals(TYPE_VIDEO) && videoSubType.equals(VIDEO_SUB_TYPE_UPLOAD)) {
                        requestPresenter.postWithVideoUpload(POST_WITH_VIDEO_UPLOAD, PreferenceUtils.getId(mContext),
                                description, videoUriList, postPrivacy, groupId);
                    } else if (postType.equals(TYPE_VIDEO) && videoSubType.equals(VIDEO_SUB_TYPE_LINK)) {
                        requestPresenter.postWithVideoUrl(POST_WITH_VIDEO_URL, PreferenceUtils.getId(mContext),
                                description, videoInfo.getUrl(), videoInfo, postPrivacy, groupId);
                    } else if (postType.equals(TYPE_LINK)) {
                        requestPresenter.postWithLink(POST_WITH_LINK, PreferenceUtils.getId(mContext),
                                description, linkInfo.getUrl(), linkInfo, postPrivacy, groupId);
                    }

                } else {
                    Utills.noInternetConnection(mContext);
                }


                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showVideoTypeDialog() {

        final Dialog videoTypeDialog = new Dialog(mContext);
        videoTypeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        videoTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        videoTypeDialog.setCanceledOnTouchOutside(false);
        videoTypeDialog.setContentView(R.layout.custom_video_type_dialog);
        videoTypeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) videoTypeDialog.findViewById(R.id.btn_video_type_submit);
        Button btn_cancel = (Button) videoTypeDialog.findViewById(R.id.btn_video_type_cancel);

        RadioGroup rg_video_type = (RadioGroup) videoTypeDialog.findViewById(R.id.rg_video_type);
        final RadioButton rb_type_upload = (RadioButton) videoTypeDialog.findViewById(R.id.rb_video_type_upload);
        RadioButton rb_type_url = (RadioButton) videoTypeDialog.findViewById(R.id.rb_video_type_url);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoTypeDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_type_upload.isChecked()) {
                    videoTypeDialog.dismiss();
                    selectVideoPopUp();
                } else {
                    videoTypeDialog.dismiss();
                    showVideoUrlDialog();
                }
            }
        });

        videoTypeDialog.show();

    }

    private void showVideoUrlDialog() {

        videoUrlDialog = new Dialog(mContext);
        videoUrlDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        videoUrlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        videoUrlDialog.setCanceledOnTouchOutside(false);
        videoUrlDialog.setContentView(R.layout.custom_video_url_dialog);
        videoUrlDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_add = (Button) videoUrlDialog.findViewById(R.id.btn_video_url_add);
        Button btn_cancel = (Button) videoUrlDialog.findViewById(R.id.btn_video_url_cancel);

        final EditText et_url = (EditText) videoUrlDialog.findViewById(R.id.et_video_url);

        et_url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    videoUrlDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        et_url.requestFocus();


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utills.hideSoftKeyboard(PostActivity.this);
                videoUrlDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String videoUrl = et_url.getText().toString().trim();

                if (videoUrl.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.url_required));
                } else {
                    if (cd.isConnectingToInternet()) {
                        Utills.hideSoftKeyboard(PostActivity.this);
                        requestPresenter.getUrlDetails(GET_VIDEO_URL_DETAILS, PreferenceUtils.getId(mContext), videoUrl);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        videoUrlDialog.show();

    }

    private void showLinkDialog() {

        linkDialog = new Dialog(mContext);
        linkDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        linkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        linkDialog.setCanceledOnTouchOutside(false);
        linkDialog.setContentView(R.layout.custom_link_dialog);
        linkDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_add = (Button) linkDialog.findViewById(R.id.btn_link_add);
        Button btn_cancel = (Button) linkDialog.findViewById(R.id.btn_link_cancel);

        final EditText et_url = (EditText) linkDialog.findViewById(R.id.et_link);

        et_url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    linkDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        et_url.requestFocus();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utills.hideSoftKeyboard(PostActivity.this);
                linkDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String link = et_url.getText().toString().trim();

                if (link.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.url_required));
                } else {
                    if (cd.isConnectingToInternet()) {
                        Utills.hideSoftKeyboard(PostActivity.this);
                        requestPresenter.getUrlDetails(GET_LINK_DETAILS, PreferenceUtils.getId(mContext), link);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        linkDialog.show();

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

            case POST_TEXT:

                showAlert(getString(R.string.app_name), message);

                break;

            case POST_WITH_IMAGE:

                showAlert(getString(R.string.app_name), message);

                break;

            case POST_WITH_VIDEO_UPLOAD:

                showAlert(getString(R.string.app_name), message);

                break;

            case GET_VIDEO_URL_DETAILS:

                showAlert(getString(R.string.app_name), message);

                break;

            case POST_WITH_VIDEO_URL:

                showAlert(getString(R.string.app_name), message);

                break;


            case GET_LINK_DETAILS:

                showAlert(getString(R.string.app_name), message);

                break;

            case POST_WITH_LINK:

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

            case POST_TEXT:

                onPostSuccess();

                break;

            case POST_WITH_IMAGE:

                onPostSuccess();

                break;


            case POST_WITH_VIDEO_UPLOAD:

                onPostSuccess();

                break;


            case GET_VIDEO_URL_DETAILS:


                try {

                    JSONObject data = Data.getJSONObject("data");
                    String siteName = data.optString("site_name");
                    String url = data.optString("url");
                    String title = data.optString("title");
                    String image = data.optString("image");
                    String description = data.optString("description");
                    String type = data.optString("type");
                    String videoUrl = data.optString("video:url");
                    String videoSecureUrl = data.optString("video:secure_url");
                    String videoType = data.optString("video:type");
                    String videoWidth = data.optString("video:width");
                    String videoHeight = data.optString("video:height");
                    String videoTag = data.optString("video:tag");
                    String host = data.optString("host");

                    videoInfo = new Info().withSiteName(siteName)
                            .withUrl(url)
                            .withTitle(title)
                            .withImage(image)
                            .withDescription(description)
                            .withType(type)
                            .withVideoUrl(videoUrl)
                            .withVideoSecureUrl(videoSecureUrl)
                            .withVideoType(videoType)
                            .withVideoWidth(videoWidth)
                            .withVideoHeight(videoHeight)
                            .withVideoTag(videoTag)
                            .withHost(host);

                    postType = TYPE_VIDEO;
                    videoSubType = VIDEO_SUB_TYPE_LINK;

                    showVideoLinkView();

                    Glide.with(mContext)
                            .load(image)
                            .asBitmap()
                            .into(img_video_link);

                    imageUriList.clear();

                    if (videoUrlDialog != null && videoUrlDialog.isShowing()) {
                        videoUrlDialog.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case POST_WITH_VIDEO_URL:

                onPostSuccess();

                break;

            case GET_LINK_DETAILS:

                try {

                    TextView et_link = linkDialog.findViewById(R.id.et_link);

                    JSONObject data = Data.getJSONObject("data");
                    String siteName = data.optString("site_name");
                    String title = data.optString("title");
                    String image = data.optString("image");
                    String description = data.optString("description");
                    String host = data.optString("host");

                    linkInfo = new Info().withSiteName(siteName)
                            .withUrl(et_link.getText().toString().trim())
                            .withTitle(title)
                            .withImage(image)
                            .withDescription(description)
                            .withHost(host);

                    postType = TYPE_LINK;
                    imageUriList.clear();
                    videoUriList.clear();

                    tv_post_link_url.setText(linkInfo.getUrl());
                    tv_post_link_url_desc.setText(linkInfo.getDescription());

                    Glide.with(mContext)
                            .load(linkInfo.getImage())
                            .asBitmap()
                            .into(img_post_link);

                    showLinkView();

                    if (linkDialog != null && linkDialog.isShowing()) {
                        linkDialog.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case POST_WITH_LINK:

                onPostSuccess();

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void onPostSuccess() {

        if (groupPost) {

            Intent intent = new Intent(PostActivity.this, GroupDetailActivity.class);
            intent.putExtra("group", group);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(PostActivity.this, HomeActivity.class);
            if (previousTag.equals(getString(R.string.fragment_home_tag))) {
                intent.putExtra(getString(R.string.activity_tag), getString(R.string.fragment_home_tag));
            } else if (previousTag.equals(getString(R.string.fragment_profile_tag))) {
                intent.putExtra(getString(R.string.activity_tag), getString(R.string.fragment_profile_tag));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void showVideoView() {
        imgRecyclerView.setVisibility(View.GONE);
        ll_post_link.setVisibility(View.GONE);
        rel_video_link.setVisibility(View.GONE);
        vid_post.setVisibility(View.VISIBLE);
    }

    private void showImageRecyclerView() {
        vid_post.setVisibility(View.GONE);
        ll_post_link.setVisibility(View.GONE);
        rel_video_link.setVisibility(View.GONE);
        imgRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLinkView() {
        vid_post.setVisibility(View.GONE);
        imgRecyclerView.setVisibility(View.GONE);
        rel_video_link.setVisibility(View.GONE);
        ll_post_link.setVisibility(View.VISIBLE);
    }

    private void showVideoLinkView() {
        vid_post.setVisibility(View.GONE);
        imgRecyclerView.setVisibility(View.GONE);
        ll_post_link.setVisibility(View.GONE);
        rel_video_link.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

        if (vid_post.getDuration() > 120000) {
            Toast.makeText(mContext, getString(R.string.video_length_limit_error), Toast.LENGTH_LONG).show();
            vid_post.reset();
            videoUriList.clear();
            mCameraVideoUri = null;
            vid_post.setVisibility(View.GONE);
        } else {
            postType = TYPE_VIDEO;
            videoSubType = VIDEO_SUB_TYPE_UPLOAD;
        }


    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
