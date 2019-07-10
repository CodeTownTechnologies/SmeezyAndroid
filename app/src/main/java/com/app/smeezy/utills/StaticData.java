package com.app.smeezy.utills;

import android.Manifest;

/**
 * Created by kipl145 on 11/22/2016.
 */
public class StaticData {

    public static int Success_Code=200;
    public static int Error_Code=203;

    public static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA};
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.MANAGE_DOCUMENTS};
    public static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int PERMISSIONS_REQUEST_CAMERA = 12;
    public static final int PERMISSIONS_REQUEST_STORAGE = 13;
    public static final int PERMISSIONS_REQUEST_LOCATION = 14;
    public static final int PERMISSIONS_REQUEST_CAMERA_VIDEO = 15;
    public static final int PERMISSIONS_REQUEST_STORAGE_VIDEO = 16;
    public static final int CAMERA_IMAGE_REQUEST = 2001;
    public static final int GALLERY_IMAGE_REQUEST = 2002;
    public static final int PLACE_PICKER_REQUEST = 2003;
    public static final int CAMERA_VIDEO_REQUEST = 2004;
    public static final int GALLERY_VIDEO_REQUEST = 2005;
    public static final String IMAGE_DIRECTORY_NAME = "Smeezy";

    public static String Success="success";
    public static String Error="error";
    public static String Successstr="success";

    public static final int EVENT_DETAIL = 45;
    public static final int CREATE_EVENT = 46;
    public static final int EDIT_EVENT = 47;

    public static final int GROUP_DETAIL = 48;
    public static final int CREATE_GROUP = 49;
    public static final int EDIT_GROUP = 50;

    public static final int ADD_STUFF = 51;
    public static final int MY_WISHLIST = 52;


    public static final int UPLOAD_IMAGE_QUALITY = 40;

    public static final String CHAT_REFRESH_BROADCAST = "chat_refresh";
    public static final String STUFF_REFRESH_BROADCAST = "stuff_refresh";
    public static final String CHAT_COUNT_REFRESH_BROADCAST = "chat_count_refresh";
    public static final String UNFOLLOW_BROADCAST = "unfollow_broadcast";
    public static final String STUFF_SEARCH_DISTANCE_CHANGED = "stuff_search_distance_changed";

    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_MEMBER = "member";

    /* APIs */
    /** Development Environment **/
    //public static final String BASE_URL = "https://demo.smeezy.com/api/";
    public static final String BASE_URL = "https://www.smeezy.com/api/";
    public static final String TERMS_URL = "https://www.smeezy.com/page/terms-and-condition/app";
    public static final String PRIVACY_POLICY_URL = "https://www.smeezy.com/page/privacy-policy/app";
    public static final String ABOUT_US_URL = "https://www.smeezy.com/page/about-us/app";
    public static final String HELP_URL = "https://www.smeezy.com/page/help/app";
    public static final String DONATE_URL = "https://www.gofundme.com/share-with-community-smeezy-share";


    /* Instagram ids */
    public static final String INSTAGRAM_CLIENT_ID = "7002388fec0e4c26a836b059300f42f9";
    public static final String INSTAGRAM_CLIENT_SECRET = "ff7d5a1fd16f46228f25753de2f466a6";

    public static final String INSTAGRAM_AUTHORIZATION_URL = "https://api.instagram.com/oauth/authorize/?" +
            "client_id="+ INSTAGRAM_CLIENT_ID +
            "&redirect_uri=https://www.smeezy.org/&response_type=token";


    public static final int BOOKMARK = 601;
    public static final int REMOVE_BOOKMARK = 602;
    public static final int CHANGE_CATEGORY = 603;
    public static final int REPORT = 604;
    public static final int UNFOLLOW_USER = 605;
    public static final int FOLLOW_POST = 606;
    public static final int UNFOLLOW_POST = 607;


    public static final String THUMB_100 = "?w=100&h=100";
    public static final String THUMB_200 = "?w=200&h=200";
    public static final String THUMB_300 = "?w=300&h=300";
}







