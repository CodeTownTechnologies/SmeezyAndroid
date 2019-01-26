package com.app.smeezy.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.app.smeezy.BuildConfig;
import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.requestmodels.MethodRequest;
import com.app.smeezy.requestmodels.Request;
import com.app.smeezy.responsemodels.Info;
import com.app.smeezy.settingstructure.ApiService;
import com.app.smeezy.settingstructure.Smeezy;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kipl145 on 11/22/2016.
 */
public class RequestPresenter extends BasePresenter {

    private IRequestView iRequestView;
    private String LOGTAG = RequestPresenter.class.getName();
    Gson gson;
    //JSONObject requestObj;
    private Context mContext;
    private Map<String, String> headerMap = new HashMap<>();
    private Map<String, String> headerMapWithAuthToken = new HashMap<>();
    private Map<String, String> headerMapWithMultipart = new HashMap<>();
    private String deviceType;
    private String deviceToken;
    private Call<ResponseBody> search;

    public RequestPresenter(Smeezy smeezy, IRequestView iRequestView) {
        super(smeezy);
        this.iRequestView = iRequestView;
        gson = new Gson();
        mContext = smeezy.getInstance();
        deviceType = "android";
        deviceToken = PreferenceUtils.getDeviceGcmId(mContext);

        headerMap.put("Content-Type", "application/json");
        headerMap.put("os", "android");
        headerMap.put("version", "1.0");

    }

    public void login(final String method, String email, String password) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceToken(deviceToken).withDeviceType(deviceType)
                .withMode(1)
                .withUserEmail(email)
                .withUserPassword(password);

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_login").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void socialLogin(final String method, String email, String facebookId, String googleId, String name, String imageUrl) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceToken(deviceToken).withDeviceType(deviceType)
                .withUserName(name)
                .withUserEmail(email)
                .withUserImageUrl(imageUrl);

        if (!facebookId.isEmpty()) {
            request.withMode(2)
                    .withUserFacebookId(facebookId);
        } else if (!googleId.isEmpty()) {
            request.withMode(3)
                    .withUserGoogleId(googleId);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("social_login").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void facebookLogin(final String method, String email, String password, String facebookId, String name, String imageUrl,
                              String referralCode) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceToken(deviceToken).withDeviceType(deviceType)
                .withMode(2)
                .withUserFacebookId(facebookId)
                .withUserName(name)
                .withUserEmail(email)
                .withUserPassword(password)
                .withUserImageUrl(imageUrl)
                .withReferralCode(referralCode);

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_login").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void googleLogin(final String method, String email, String password, String googleId, String name, String imageUrl,
                            String referralCode) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceToken(deviceToken).withDeviceType(deviceType)
                .withMode(3)
                .withUserGoogleId(googleId)
                .withUserName(name)
                .withUserEmail(email)
                .withUserPassword(password)
                .withUserImageUrl(imageUrl)
                .withReferralCode(referralCode);

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_login").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void signUp(final String method, String name, String email, String password, String referralCode) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceToken(deviceToken).withDeviceType(deviceType)
                .withUserName(name)
                .withUserEmail(email)
                .withUserPassword(password)
                .withReferralCode(referralCode);

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_signup").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);


                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void forgotPassword(final String method, String email) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request()
                .withUserEmail(email);

        MethodRequest methodRequest = new MethodRequest().withMethodName("reset_password").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editProfileBasic(final String method, String userId, String name, String dob, String relationshipStatus,
                                 String phoneNumber, String location, String gender, String latitude, String longitude,
                                 String city, String region, String country, String pinCode, ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("profile_image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withName(name)
                .withDob(dob)
                .withPhoneNumber(phoneNumber)
                .withRelationshipStatus(relationshipStatus)
                .withLocation(location)
                .withGender(gender)
                .withLocationLatitude(latitude)
                .withLocationLongitude(longitude)
                .withCity(city)
                .withRegion(region)
                .withCountry(country)
                .withPincode(pinCode);

        MethodRequest methodRequest = new MethodRequest().withMethodName("edit_profile_basic").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        final Call<ResponseBody> bidsRequest;


        if (part != null) {
            bidsRequest = apiService.editProfileBasicWithImage(methodRequest, part);
        } else {
            bidsRequest = apiService.editProfileBasic(methodRequest);
        }

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void logout(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_logout").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void changePassword(final String method, String userId, String oldPassword, String newPassword) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPassword(oldPassword)
                .withPasswordNew(newPassword);

        MethodRequest methodRequest = new MethodRequest().withMethodName("change_password").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getChatList(final String method, String userId, String type) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_message_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });
    }

    public void getSingleChat(final String method, String userId, String friendId, int page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId)
                .withPage(String.valueOf(page));

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_message_single_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getUnreadChat(final String method, String userId, String friendId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_unread_messages").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getUnreadChatCount(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_unread_messages_count").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void composeMessage(final String method, String userId, String friendId, String message) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId)
                .withMessage(message);

        MethodRequest methodRequest = new MethodRequest().withMethodName("compose_message").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getProfileData(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_profile_data").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void editSocialLinks(final String method, String userId, String type, String socialLinkUrl) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withType(type)
                .withUrl(socialLinkUrl);

        MethodRequest methodRequest = new MethodRequest().withMethodName("edit_social_info").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void editAboutInfo(final String method, String userId, String summary) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withSummary(summary);

        MethodRequest methodRequest = new MethodRequest().withMethodName("edit_about_info").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void editOtherSettings(final String method, String userId, String religiousViews, String politicalViews,
                                  String work, String school) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withReligiousView(religiousViews)
                .withPoliticalView(politicalViews)
                .withWork(work)
                .withSchool(school);

        MethodRequest methodRequest = new MethodRequest().withMethodName("edit_profile_info").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void sendFriendRequest(final String method, String userId, String friendId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("send_friend_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void manageFriendRequest(final String method, String userId, String friendId, String action) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId)
                .withAction(action);

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_friend_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getFriendList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_friends_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getFeedList(final String method, String userId, String type, String page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_feed_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getCommunityFeedList(final String method, String userId, String type, String page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_community_feed_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getFriendFeedList(final String method, String userId, String type, String page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_friends_feed_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getCommentList(final String method, String userId, String activityId, String feedId, String page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page)
                .withActivityId(activityId)
                .withFeedId(feedId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_feed_comment_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void likeUnlikePost(final String method, String userId, String activityId, String feedId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withActivityId(activityId)
                .withFeedId(feedId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_like").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void comment(final String method, String userId, String activityId, String feedId, String parentId, String comment) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withActivityId(activityId)
                .withFeedId(feedId)
                .withParentId(parentId)
                .withComment(comment);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_comment").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getReportReasonList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_report_reason_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void reportPost(final String method, String userId, String activityId, String feedId, String comment,
                           String reasonId, String taskType) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withActivityId(activityId)
                .withFeedId(feedId)
                .withComment(comment)
                .withReason(reasonId)
                .withTastType(taskType);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_report").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void searchUsers(final String method, String userId, String keyword, String type, int page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withKeyword(keyword)
                .withType(type)
                .withPage(String.valueOf(page));

        MethodRequest methodRequest = new MethodRequest().withMethodName("search_users_list").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        search = apiService.common(requestObj);
        search.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                if (!call.isCanceled()) {
                    iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
                }
            }
        });

    }

    public void cancelSearch() {
        if (search != null) {
            search.cancel();
        }
    }

    public void postFeedback(final String method, String userId, String memberId, String rate, String comment) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId)
                .withRate(rate)
                .withComment(comment);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feedback_post").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getFeedbackList(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feedback_get_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveSearchDistance(final String method, String userId, int searchDistance) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withSearchDistance(searchDistance);

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_search_distance").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveNotificationSetting(final String method, String userId, String notification,
                                        String stuffPrivacy) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withNotification(Integer.parseInt(notification))
                .withStuffPrivacy(stuffPrivacy);

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_notification_setting").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void deletePost(final String method, String userId, String feedId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(feedId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_delete").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getFriendRequestList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_friends_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getNotificationCount(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_notification_count").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void deleteEvent(final String method, String userId, String eventId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(eventId)
                .withTask("delete");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_event").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getEventList(final String method, String userId, String type) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_event_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getMyEventList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_my_event_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void sendEventInvite(final String method, String userId, String eventId, String[] users) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(eventId)
                .withUsers(users);

        MethodRequest methodRequest = new MethodRequest().withMethodName("send_event_invite").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void deleteGroup(final String method, String userId, String groupId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId)
                .withTask("delete");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_group").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getGroupList(final String method, String userId, String type) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_group_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getGroupFeedList(final String method, String userId, String groupId, String type, String page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withGroupId(groupId)
                .withPage(page)
                .withType(type);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_group_feed_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getGroupDetail(final String method, String userId, String groupId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_group_detail").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void addGroupFriends(final String method, String userId, String groupId, String[] users) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId)
                .withUsers(users);

        MethodRequest methodRequest = new MethodRequest().withMethodName("group_add_friends").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void performGroupAction(final String method, String userId, String groupId, String action) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId)
                .withAction(action);

        MethodRequest methodRequest = new MethodRequest().withMethodName("group_perform_action").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getStuffList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveStuffList(final String method, String userId, String options) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        if (options.isEmpty()) {
            request.withOptions("");
        } else {
            request.withOptions(options);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getEventRequestList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_event_invite_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void manageEventRequest(final String method, String userId, String eventId, String decision) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(eventId)
                .withDecision(decision);

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_event_invite").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getBirthdayList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_birthday_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void contactUs(final String method, String name, String email, String subject,
                          String message) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withName(name)
                .withEmail(email)
                .withSubject(subject)
                .withMessage(message);


        MethodRequest methodRequest = new MethodRequest().withMethodName("contact_us").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getFaqList(final String method) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_faq_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getEventGuestList(final String method, String userId, String eventId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(eventId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_event_guest_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getGroupMemberList(final String method, String userId, String groupId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_group_members_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getGroupRequestList(final String method, String userId, String groupId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_group_join_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void manageGroupRequest(final String method, String userId, String groupId, String task) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(groupId)
                .withTask(task);

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_group_join_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void saveProfilePrivacy(final String method, String userId, String emailPrivacy, String dobPrivacy,
                                   String phonePrivacy, String relationshipPrivacy, String locationPrivacy,
                                   String socialConnectionPrivacy) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withEmailPrivacy(emailPrivacy)
                .withDobPrivacy(dobPrivacy)
                .withPhonePrivacy(phonePrivacy)
                .withRelationshipPrivacy(relationshipPrivacy)
                .withLocationPrivacy(locationPrivacy)
                .withSocialConnectionPrivacy(socialConnectionPrivacy);

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_profile_privacy").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void deleteAccount(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("delete_account").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveCustomStuffList(final String method, String userId, String options) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        if (options.isEmpty()) {
            request.withOptions("");
        } else {
            request.withOptions(options);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_custom_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void composePrivateMessage(final String method, String userId, String friendId, String message) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId)
                .withMessage(message);

        MethodRequest methodRequest = new MethodRequest().withMethodName("compose_private_message").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getPrivateChatList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_private_message_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getSinglePrivateChat(final String method, String userId, String friendId, int page) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId)
                .withPage(String.valueOf(page));

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_private_message_single_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveAllowUser(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_allow_user").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void removeAllowUser(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("remove_allow_user").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getConfig(final String method) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_config").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void manageBlock(final String method, String userId, String memberId, String action) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId)
                .withAction(action);

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_block_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getBlockList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_block_user_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveReferralCode(final String method, String userId, String referralCode) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withReferralCode(referralCode);

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_referral_code").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getUserStuffList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_user_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void getOtherUserStuffList(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_other_user_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void deleteStuff(final String method, String userId, String id) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(id)
                .withTask("delete");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getStuffFeedList(final String method, String userId, String type, String page,
                                 String category, String item, String distance, String sortBy,
                                 String options) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page)
                .withType(type)
                .withSortby(sortBy)
                .withOptions(options);

        if (!category.isEmpty()) {
            request.withCategory(category);
        }

        if (!item.isEmpty()) {
            request.withItem(item);
        }

        if (!distance.isEmpty()) {
            request.withDistance(distance);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_stuff_feed").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getStuffFeedListForSearch(final String method, String userId, String type, String page,
                                          String category, String item, String distance) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page)
                .withType(type)
                .withItem(item);

        if (!category.isEmpty()) {
            request.withCategory(category);
        }

        if (!distance.isEmpty()) {
            request.withDistance(distance);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_stuff_feed").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void composeMessageStuff(final String method, String userId, String friendId, String message,
                                    String type, String stuffId, String selectedStuff) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(friendId)
                .withMessage(message)
                .withType(type)
                .withStuffId(stuffId)
                .withSelectedStuff(selectedStuff);

        MethodRequest methodRequest = new MethodRequest().withMethodName("compose_message_stuff").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getUserTradeStuffList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_user_trade_stuff").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getMemberInterestList(final String method, String userId, String memberId) {

        //iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_user_interest_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void tradeOffer(final String method, String userId, String message, String type,
                           String stuffId, String action, String selectedStuff) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMessage(message)
                .withType(type)
                .withStuffId(stuffId)
                .withAction(action)
                .withSelectedStuff(selectedStuff);

        MethodRequest methodRequest = new MethodRequest().withMethodName("trade_offer").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void followUnfollowPost(final String method, String userId, String activityId, String feedId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withActivityId(activityId)
                .withFeedId(feedId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_follow_status_post").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void followUnfollowUser(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_follow_status_user").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void addRemoveBookmark(final String method, String userId, String activityId, String itemId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withActivityId(activityId)
                .withTaskId(itemId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_bookmark").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void changeFeedCategory(final String method, String userId, String activityId, String itemId,
                                   String categoryId, String message) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withActivityId(activityId)
                .withTaskId(itemId)
                .withCategoryId(categoryId)
                .withMessage(message);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_action_change_category").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void getBookmarkList(final String method, String userId, String page,
                                String category, String item, String distance) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withPage(page);

        if (!category.isEmpty()) {
            request.withCategory(category);
        }

        if (!item.isEmpty()) {
            request.withItem(item);
        }

        if (!distance.isEmpty()) {
            request.withDistance(distance);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_bookmark_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getPaymentOptionsList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_payment_option_list").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void sendStuffPaymentRequest(final String method, String userId, String stuffId,
                                        String paymentId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(stuffId)
                .withPaymentId(paymentId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("stuff_send_payment_request").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getSuggestedFriendsList(final String method, String userId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_suggested_friends").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void saveStuffSearchDistance(final String method, String userId, int distance) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withSearchDistance(distance);

        MethodRequest methodRequest = new MethodRequest().withMethodName("save_stuff_search_distance").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getMutualFriendList(final String method, String userId, String memberId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withMemberId(memberId);

        MethodRequest methodRequest = new MethodRequest().withMethodName("get_mutual_friends").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void increaseStuffViews(final String method, String userId, String id) {

        //iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(id);

        MethodRequest methodRequest = new MethodRequest().withMethodName("stuff_update_view_count").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                //iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void manageStuffList(final String method, String userId, String task, String stuffText,
                                String description, String options, String id, String stuffId, String categoryId,
                                String buy, String buyPrice, String rent, String rentPrice, String paymentAccepted,
                                ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withStuffId(stuffId)
                .withCategoryId(categoryId)
                .withOptions(options)
                .withStuffText(stuffText)
                .withDescription(description)
                .withTask(task)
                .withBuy(buy)
                .withBuyPrice(buyPrice)
                .withRent(rent)
                .withRentPrice(rentPrice)
                .withPaymentAccepted(paymentAccepted);

        if (task.equals("edit")) {
            request.withId(id);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_stuff_list").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createEvent(final String method, String userId, String title, String location, String startDate,
                            String description, String eventType, String latitude, String longitude,
                            ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withTitle(title)
                .withLocation(location)
                .withLocationLatitude(latitude)
                .withLocationLongitude(longitude)
                .withDescription(description)
                .withStartDate(startDate)
                .withEndDate("")
                .withEventType(eventType)
                .withTask("add");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_event").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editEvent(final String method, String userId, String eventId, String title, String location, String startDate,
                          String description, String eventType, String latitude, String longitude,
                          ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withId(eventId)
                .withTitle(title)
                .withLocation(location)
                .withLocationLatitude(latitude)
                .withLocationLongitude(longitude)
                .withDescription(description)
                .withStartDate(startDate)
                .withEndDate("")
                .withEventType(eventType)
                .withTask("edit");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_event").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createGroup(final String method, String userId, String title, String description,
                            String groupType, ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withTitle(title)
                .withDescription(description)
                .withType(groupType)
                .withTask("add");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_group").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editGroup(final String method, String userId, String groupId, String title, String description,
                          String groupType, ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withId(groupId)
                .withTitle(title)
                .withDescription(description)
                .withType(groupType)
                .withTask("edit");

        MethodRequest methodRequest = new MethodRequest().withMethodName("manage_group").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editProfileImage(final String method, String userId, ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withType("profile");

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_image_upload").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        final Call<ResponseBody> bidsRequest;


        if (part != null) {
            bidsRequest = apiService.editProfileBasicWithImage(methodRequest, part);
        } else {
            bidsRequest = apiService.editProfileBasic(methodRequest);
        }

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editCoverImage(final String method, String userId, ArrayList<Uri> filePathList) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("image", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withType("profile_cover");

        MethodRequest methodRequest = new MethodRequest().withMethodName("user_image_upload").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        final Call<ResponseBody> bidsRequest;


        if (part != null) {
            bidsRequest = apiService.editProfileBasicWithImage(methodRequest, part);
        } else {
            bidsRequest = apiService.editProfileBasic(methodRequest);
        }

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void postText(final String method, String userId, String description, String postPrivacy,
                         String groupId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withDescription(description)
                .withType("text");

        String methodName = "feed_save";

        if (groupId != null && !groupId.isEmpty()) {
            request.withGroupId(groupId);
            methodName = "group_feed_save";
        } else {
            request.withPostPrivacy(postPrivacy);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName(methodName).withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void editPostText(final String method, String userId, String feedId, String description, String postPrivacy) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(feedId)
                .withDescription(description)
                .withType("text")
                .withPostPrivacy(postPrivacy);


        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_edit").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void postWithImage(final String method, String userId, String description, ArrayList<Uri> filePathList,
                              String postPrivacy, String groupId) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part[] = new MultipartBody.Part[filePathList.size()];

        if (filePathList.size() > 0) {
            for (int i = 0; i < filePathList.size(); i++) {
                Uri uri = filePathList.get(i);
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part[i] = MultipartBody.Part.createFormData("image[]", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withDescription(description)
                .withType("image");

        String methodName = "feed_save";

        if (groupId != null && !groupId.isEmpty()) {
            request.withGroupId(groupId);
            methodName = "group_feed_save";
        } else {
            request.withPostPrivacy(postPrivacy);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName(methodName).withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMultipleImages(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPostWithImage(final String method, String userId, String feedId, ArrayList<String> oldImageList,
                                  String description, ArrayList<Uri> filePathList, String postPrivacy) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part[] = new MultipartBody.Part[filePathList.size()];

        if (filePathList.size() > 0) {
            for (int i = 0; i < filePathList.size(); i++) {
                Uri uri = filePathList.get(i);
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part[i] = MultipartBody.Part.createFormData("image[]", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }


            }

        }

        String[] oldImageArray = new String[oldImageList.size()];
        oldImageArray = oldImageList.toArray(oldImageArray);

        Request request = new Request()
                .withUserId(userId)
                .withId(feedId)
                .withOldImageArray(oldImageArray)
                .withDescription(description)
                .withType("image")
                .withPostPrivacy(postPrivacy);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_edit").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMultipleImages(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void getUrlDetails(final String method, String userId, String url) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withUrl(url);

        MethodRequest methodRequest = new MethodRequest().withMethodName("fetch_url_detail").withData(request);

        JSONObject requestObj = new JSONObject();


        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void postWithVideoUrl(final String method, String userId, String description, String videoUrl, Info info,
                                 String postPrivacy, String groupId) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withDescription(description)
                .withType("video")
                .withSubType("link")
                .withMedia(videoUrl)
                .withInfo(info);

        String methodName = "feed_save";

        if (groupId != null && !groupId.isEmpty()) {
            request.withGroupId(groupId);
            methodName = "group_feed_save";
        } else {
            request.withPostPrivacy(postPrivacy);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName(methodName).withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void editPostWithVideoUrl(final String method, String userId, String feedId, String description, String videoUrl, Info info,
                                     String postPrivacy) {

        iRequestView.showLoadingProgressBar();

        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(feedId)
                .withDescription(description)
                .withType("video")
                .withSubType("link")
                .withMedia(videoUrl)
                .withInfo(info)
                .withPostPrivacy(postPrivacy);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_edit").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    public void postWithLink(final String method, String userId, String description, String url, Info info,
                             String postPrivacy, String groupId) {

        iRequestView.showLoadingProgressBar();


        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withDescription(description)
                .withType("link")
                .withMedia(url)
                .withInfo(info);

        String methodName = "feed_save";

        if (groupId != null && !groupId.isEmpty()) {
            request.withGroupId(groupId);
            methodName = "group_feed_save";
        } else {
            request.withPostPrivacy(postPrivacy);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName(methodName).withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    public void editPostWithLink(final String method, String userId, String feedId, String description, String url, Info info,
                                 String postPrivacy) {

        iRequestView.showLoadingProgressBar();


        Request request = new Request().withDeviceType(deviceType)
                .withUserId(userId)
                .withId(feedId)
                .withDescription(description)
                .withType("link")
                .withMedia(url)
                .withInfo(info)
                .withPostPrivacy(postPrivacy);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_edit").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOGTAG, requestObj.toString());

        ApiService apiService = getApiService();
        Call<ResponseBody> login = apiService.common(requestObj);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();

                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (BuildConfig.DEBUG)
                    Log.d(LOGTAG, t.toString());
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void postWithVideoUpload(final String method, String userId, String description, ArrayList<Uri> filePathList,
                                    String postPrivacy, String groupId) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("media", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }

            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withDescription(description)
                .withType("video")
                .withSubType("upload");

        String methodName = "feed_save";

        if (groupId != null && !groupId.isEmpty()) {
            request.withGroupId(groupId);
            methodName = "group_feed_save";
        } else {
            request.withPostPrivacy(postPrivacy);
        }

        MethodRequest methodRequest = new MethodRequest().withMethodName(methodName).withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPostWithVideoUpload(final String method, String userId, String feedId, String description, ArrayList<Uri> filePathList,
                                        String postPrivacy) {

        iRequestView.showLoadingProgressBar();

        MultipartBody.Part part = null;

        if (filePathList.size() > 0) {
            for (Uri uri : filePathList) {
                byte[] content = getBytesFromUri(uri);
                if (content != null) {

                    String fileName = null;
                    String scheme = uri.getScheme();
                    if (scheme.equals("file")) {
                        fileName = uri.getLastPathSegment();
                    } else if (scheme.equals("content")) {
                        String[] proj = {MediaStore.Images.Media.TITLE};
                        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.getCount() != 0) {
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                            cursor.moveToFirst();
                            fileName = cursor.getString(columnIndex);

                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                            Cursor metaCursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                            if (metaCursor != null) {
                                try {
                                    if (metaCursor.moveToFirst()) {
                                        fileName = metaCursor.getString(0);
                                    }
                                } finally {
                                    metaCursor.close();
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    part = MultipartBody.Part.createFormData("media", fileName,
                            RequestBody.create(MediaType.parse(getMimeType(uri)), content));

                }

            }

        }


        Request request = new Request()
                .withUserId(userId)
                .withId(feedId)
                .withDescription(description)
                .withType("video")
                .withSubType("upload")
                .withPostPrivacy(postPrivacy);

        MethodRequest methodRequest = new MethodRequest().withMethodName("feed_edit").withData(request);

        JSONObject requestObj = new JSONObject();

        try {
            requestObj = new JSONObject(gson.toJson(methodRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService apiService = getApiService();
        Log.d(LOGTAG, requestObj.toString());

        Call<ResponseBody> bidsRequest = apiService.postWithMedia(methodRequest, part);

        bidsRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iRequestView.hideLoadingProgressBar();
                int statusCode = response.code();

                Log.d(LOGTAG, "statusCode : " + statusCode);

                if (response.isSuccessful()) {
                    parsedata(method, response.body().byteStream(), response.code());
                } else {
                    parseerrordata(method, response.errorBody().byteStream(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iRequestView.hideLoadingProgressBar();
                iRequestView.Failed(method, getAppContext().getResources().getString(R.string.network_failed));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] getBytesFromUri(Uri media) {
        byte[] content = null;

        /*OutputStream outStream;
        Uri tempUri = Utills.getOutputMediaFileUri();
        try {
            if (tempUri != null) {
                outStream = new FileOutputStream(tempUri.getPath());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), media);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, outStream);
                outStream.close();
                media = tempUri;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            if (media != null) {
                content = getBytesFromInputStream(mContext.getContentResolver().openInputStream(media));
            }
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        }
        return content;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] getBytesFromInputStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1; )
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getAppContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void parsedata(String method, InputStream inputStream, int statusCode) {
        try {
            InputStream is = inputStream;
            String text = Utills.convertStreamToString(is);
            Log.d("Result", "text = " + text);
            JSONObject json = new JSONObject(text);
            if (json.getString("replyStatus").equals("success")) {
                Log.d(LOGTAG, json.toString());
                iRequestView.Success(method, json);

            } else if (json.getString("replyStatus").equals("error")) {
                iRequestView.Failed(method, json.getString("replyMessage"));
                Log.d(LOGTAG, json.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /*private void parsedata1(InputStream inputStream, int statusCode) {
        try {
            InputStream is = inputStream;
            String text = Utills.convertStreamToString(is);
            Log.d("Result", "text = " + text);
            JSONObject json = new JSONObject(text);
            if (json.getBoolean("status")) {
                iRequestView.Success1(json);
            } else if (!json.getBoolean("status")) {
                iRequestView.Failed(json.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void parsedata2(InputStream inputStream, int statusCode) {
        try {
            InputStream is = inputStream;
            String text = Utills.convertStreamToString(is);
            Log.d("Result", "text = " + text);
            JSONObject json = new JSONObject(text);
            if (json.getBoolean("status")) {
                iRequestView.Success2(json);
            } else if (!json.getBoolean("status")) {
                iRequestView.Failed(json.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void parsedata3(InputStream inputStream, int statusCode) {
        try {
            InputStream is = inputStream;
            String text = Utills.convertStreamToString(is);
            Log.d("Result", "text = " + text);
            JSONObject json = new JSONObject(text);
            if (json.getBoolean("status")) {
                iRequestView.Success3(json);
            } else if (!json.getBoolean("status")) {
                iRequestView.Failed(json.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void parsedata4(InputStream inputStream, int statusCode) {
        try {
            InputStream is = inputStream;
            String text = Utills.convertStreamToString(is);
            Log.d("Result", "text = " + text);
            JSONObject json = new JSONObject(text);
            if (json.getBoolean("status")) {
                iRequestView.Success4(json);
            } else if (!json.getBoolean("status")) {
                iRequestView.Failed(json.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }*/


    private void parseerrordata(String method, InputStream inputStream, int statusCode) {
        try {
            InputStream is = inputStream;
            String text = Utills.convertStreamToString(is);

            Log.d("Result", "text = " + text);
            JSONObject json = new JSONObject(text);

            iRequestView.hideLoadingProgressBar();
            iRequestView.Failed(method, json.toString());
            //iRequestView.Failed(json.optString("message"));
            Log.d(LOGTAG, json.toString());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
