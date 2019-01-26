package com.app.smeezy.utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class PreferenceUtils {

    public static final String PREF_LOGO_URL = "logo_url";
    public static final String PREF_REFERRAL_SHARE_TEXT = "referral_share_text";
    public static final String PREF_CONTACT_EMAIL = "contact_email";
    public static final String PREF_DEVICE_GCM_ID = "device_gcm_id";
    public static final String PREF_IS_FIRST_TIME = "first_time";
    public static final String PREF_AUTH_TOKEN = "auth_token";
    public static final String PREF_IS_LOGIN = "is_login";
    public static final String PREF_STEP_FIRST = "step_first";
    public static final String PREF_ID = "id";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_NAME = "name";
    public static final String PREF_ADDRESS = "address";
    public static final String PREF_MOBILE_NO = "mobile_no";
    public static final String PREF_PROFILE_PIC_URL = "profile_pic";
    public static final String PREF_COMMUNITY_SEARCH_DISTANCE = "search_distance";
    public static final String PREF_NOTIFICATION_STATUS = "notification_status";
    public static final String PREF_STUFF_PRIVACY_STATUS = "stuff_privacy_status";
    public static final String PREF_ITEM_SEARCH_DISTANCE = "item_search_distance";


   /* public static String getLogoUrl(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_LOGO_URL, "");
    }

    public static void setLogoUrl(Context context, String logoUrl) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_LOGO_URL, logoUrl);
        editor.commit();
    }*/

    public static int getLogoUrl(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_LOGO_URL, 0);
    }

    public static void setLogoUrl(Context context, int logoUrl) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(PREF_LOGO_URL, logoUrl);
        editor.commit();
    }

    public static String getReferralShareText(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_REFERRAL_SHARE_TEXT, "");
    }

    public static void setReferralShareText(Context context, String referralShareText) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_REFERRAL_SHARE_TEXT, referralShareText);
        editor.commit();
    }

    public static String getContactEmail(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_CONTACT_EMAIL, "");
    }

    public static void setContactEmail(Context context, String email) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_CONTACT_EMAIL, email);
        editor.commit();
    }


    public static String getDeviceGcmId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_DEVICE_GCM_ID, "");
    }

    public static void setDeviceGcmId(Context context, String deviceToken) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_DEVICE_GCM_ID, deviceToken);
        editor.commit();
    }

    public static boolean getIsFirstTime(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_IS_FIRST_TIME, true);
    }

    public static void setIsFirstTime(Context context, boolean isFirstTime) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(PREF_IS_FIRST_TIME, isFirstTime);
        editor.commit();
    }


    public static String getAuthToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_AUTH_TOKEN, "");
    }

    public static void setAuthToken(Context context, String authtoken) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_AUTH_TOKEN, authtoken);
        editor.commit();
    }

    public static String getId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_ID, "");
    }

    public static void setId(Context context, String id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_ID, id);
        editor.commit();
    }

    public static boolean getIsLogin(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_IS_LOGIN, false);
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(PREF_IS_LOGIN, isLogin);
        editor.commit();
    }

    public static String getStepFirst(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_STEP_FIRST, "No");
    }

    public static void setStepFirst(Context context, String stepFirst) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_STEP_FIRST, stepFirst);
        editor.commit();
    }

    public static String getEmail(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_EMAIL, "");
    }

    public static void setEmail(Context context, String email) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_EMAIL, email);
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_NAME, "");
    }

    public static void setName(Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_NAME, name);
        editor.commit();
    }

    public static String getAddress(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_ADDRESS, "");
    }

    public static void setAddress(Context context, String address) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_ADDRESS, address);
        editor.commit();
    }



    public static String getMobileNo(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_MOBILE_NO, "");
    }

    public static void setMobileNo(Context context, String mobileNo) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_MOBILE_NO, mobileNo);
        editor.commit();
    }

    public static String getProfilePicUrl(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_PROFILE_PIC_URL, "");
    }

    public static void setProfilePicUrl(Context context, String profilePicUrl) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_PROFILE_PIC_URL, profilePicUrl);
        editor.commit();
    }

    public static int getCommunitySearchDistance(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_COMMUNITY_SEARCH_DISTANCE, 27);
    }

    public static void setCommunitySearchDistance(Context context, int distance) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(PREF_COMMUNITY_SEARCH_DISTANCE, distance);
        editor.commit();
    }

    public static String getNotificationStatus(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_NOTIFICATION_STATUS, "0");
    }

    public static void setNotificationStatus(Context context, String notificationStatus) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_NOTIFICATION_STATUS, notificationStatus);
        editor.commit();
    }

    public static String getStuffPrivacyStatus(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_STUFF_PRIVACY_STATUS, "private");
    }

    public static void setStuffPrivacyStatus(Context context, String privacyStatus) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(PREF_STUFF_PRIVACY_STATUS, privacyStatus);
        editor.commit();
    }

    public static int getItemSearchDistance(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_ITEM_SEARCH_DISTANCE, 27);
    }

    public static void setItemSearchDistance(Context context, int distance) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(PREF_ITEM_SEARCH_DISTANCE, distance);
        editor.commit();
    }


}



