package com.app.smeezy.fcm;

import android.text.TextUtils;
import android.util.Log;


import com.app.smeezy.BuildConfig;
import com.app.smeezy.utills.PreferenceUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by kipl146 on 7/19/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (BuildConfig.DEBUG)
            Log.d(TAG, "Refreshed token: " + refreshedToken);
        if (!TextUtils.isEmpty(refreshedToken)) {
            PreferenceUtils.setDeviceGcmId(this, refreshedToken);
            sendRegistrationToServer(refreshedToken);
        }

    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
