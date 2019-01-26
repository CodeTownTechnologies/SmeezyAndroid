package com.app.smeezy.settingstructure;


import com.app.smeezy.requestmodels.MethodRequest;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {


    /* common */
    @POST(".")
    @FormUrlEncoded
    Call<ResponseBody> common(@Field("request") JSONObject jsonObject);

    /* edit profile */
    /*@POST(".")
    Call<ResponseBody> editProfileBasic(@Body JSONObject jsonObject, @Body MultipartBody requestBody);*/

    /* edit profile with image*/
    @Multipart
    @POST(".")
    Call<ResponseBody> editProfileBasicWithImage(@Part("request") MethodRequest jsonObject,
                                                 @Part MultipartBody.Part image);

    /* edit profile */
    @Multipart
    @POST(".")
    Call<ResponseBody> editProfileBasic(@Part("request") MethodRequest jsonObject);

    /* post with media*/
    @Multipart
    @POST(".")
    Call<ResponseBody> postWithMedia(@Part("request") MethodRequest jsonObject,
                                                 @Part MultipartBody.Part image);

    /* post with multiple images*/
    @Multipart
    @POST(".")
    Call<ResponseBody> postWithMultipleImages(@Part("request") MethodRequest jsonObject,
                                     @Part MultipartBody.Part[] image);


    /* post without media */
    @Multipart
    @POST(".")
    Call<ResponseBody> post(@Part("request") MethodRequest jsonObject);


}
