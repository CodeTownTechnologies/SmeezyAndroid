package com.app.smeezy.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.app.smeezy.BuildConfig;
import com.app.smeezy.R;
import com.app.smeezy.utills.StaticData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by kipl146 on 7/19/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MYFIREbase";

    public static int NOTIFICATION_ID;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("remote message==" + remoteMessage);

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        String notification = remoteMessage.getData().toString();
        Log.e("NotificationData", notification);

        if (remoteMessage != null) {
            if (BuildConfig.DEBUG) {
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Log.d(TAG, "key, " + key + " value " + value);
                }
            }

            if (remoteMessage.getData() != null) {
                Log.d(TAG, "Remote Message " + remoteMessage.getData().toString());
                if (remoteMessage.getData().size() > 0) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                    //sendNotification(remoteMessage.getData());
                    Toast.makeText(this, remoteMessage.getData().toString(), Toast.LENGTH_LONG).show();
                }
            }


            if (remoteMessage.getNotification() != null) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Message type: " + remoteMessage.getMessageType());
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().toString());

                getApplicationContext().sendBroadcast(new Intent(StaticData.CHAT_REFRESH_BROADCAST));
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @paramremoteMessageData FCM message body received.
     */
    /*private void sendNotification(Map<String, String> remoteMessageData) {


        Log.d(TAG, "Preparing to send notification...: " + remoteMessageData.get("alert"));

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NOTIFICATION_ID = (int) (Math.random() * 100);

        PendingIntent contentIntent=null;

        try {
                contentIntent = PendingIntent.getActivity(
                        this,
                        NOTIFICATION_ID,
                        new Intent(this, InstituteDetailActivity.class)
                                .putExtra("institute_id", remoteMessageData.get("institute_id")),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        } catch (NullPointerException e) {
            // TODO: handle exception
            System.out.println("exception" + e);

        }

        try {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessageData.get("alert").toString()))
                    .setContentText(remoteMessageData.get("alert").toString())
                    .setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);

            mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            Log.d(TAG, "Notification sent successfully.");

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }*/

//        String messageBody = "";
//        int _type = -1;


//        if (remoteMessageData.containsKey("alert")) {
//            messageBody = remoteMessageData.get("alert");
//        }


    //    generateNotification(intent, _type, messageBody, NOTIFICATION_ID_FOR_LIKE);

    //    Intent intent = null;
    //   if (remoteMessageData.containsKey("activity")) {
            /* LIKE notification received */
//            if (remoteMessageData.get("activity").equals("like")) {
//                _type = StaticData.NOTIFICATION_TYPE.LIKE.ordinal();
    //            intent = new Intent(this, InstituteDetailActivity.class);
    //     }
            /* A new MATCH has been formed */
////            else if (remoteMessageData.get("activity").equals("match")) {
////                _type = StaticData.NOTIFICATION_TYPE.MATCH.ordinal();
////                intent = new Intent(this, HomeActivity.class);
////                if (remoteMessageData.containsKey("viewer_id")) {
    //                   intent.putExtra("institute_id", remoteMessageData.get("institute_id"));
////                }
////                if (remoteMessageData.containsKey("doer_id")) {
////                    intent.putExtra("doer_id", remoteMessageData.get("doer_id"));
////                }
////                if (remoteMessageData.containsKey("matcher_id")) {
////                    intent.putExtra("matcher_id", remoteMessageData.get("matcher_id"));
////                }
////                if (remoteMessageData.containsKey("profile_image")) {
////                    intent.putExtra("profile_image", remoteMessageData.get("profile_image"));
////                }
////                if (remoteMessageData.containsKey("name")) {
////                    intent.putExtra("name", remoteMessageData.get("name"));
////                }
////
////            }
//            /* MESSAGE notification received */
////            else if (remoteMessageData.get("activity").equals("message")) {
////                _type = StaticData.NOTIFICATION_TYPE.MESSAGE.ordinal();
////                intent = new Intent(this, HomeActivity.class);
////            }
////            /* Someone has FLIRTED you */
////            else if (remoteMessageData.get("activity").equals("flirt")) {
////                _type = StaticData.NOTIFICATION_TYPE.FLIRT.ordinal();
////                intent = new Intent(this, HomeActivity.class);
////            }
////            /* Your LAST NAME has been REQUESTED */
////            else if (remoteMessageData.get("activity").equals("lastname_request")) {
////                _type = StaticData.NOTIFICATION_TYPE.LAST_NAME_REQUESTED.ordinal();
////                intent = new Intent(this, HomeActivity.class);
////            }
////            /* Some friend has uploaded a NEW PICTURE */
////            else if (remoteMessageData.get("activity").equals("new_picture")) {
////                _type = StaticData.NOTIFICATION_TYPE.NEW_PICTURE.ordinal();
////                intent = new Intent(this, HomeActivity.class);
////            }
    //       }


//        if(remoteMessageData.containsKey("viewer_id"))
//        {
//            // check if user is logged in or not
//            if (PreferenceUtils.isGameSettingUpdated(this))
//            {
//                Type type = new TypeToken<UserData>() {
//                }.getType();
//                UserData userDetails = new Gson().fromJson(PreferenceUtils.getLoginUserData(this), type);
//
//                // check if notification is for logged In user
//                if(remoteMessageData.get("viewer_id").equalsIgnoreCase(userDetails.getId()))
//                {
//
//                    if(_type == StaticData.NOTIFICATION_TYPE.MATCH.ordinal())
//                    {
//                        sendBroadCast(StaticData.NOTIFICATION, intent, _type);
//                        //insertNotificationIntoDatabase
//                        String senderId = remoteMessageData.get("matcher_id");
//                        String receiverId = remoteMessageData.get("viewer_id");
//                        String senderName = remoteMessageData.get("name");
//                        String senderImage = remoteMessageData.get("profile_image");
//                        MyDataBase.getInstance(this).insertNotificationIntoDatabase(senderId, receiverId,senderName, senderImage, MyDataBase.NOTIFICATION_TYPE_NEW_MATCH);
//
//
//                        if(ifAppIsOpen() == false)
//                        {
//                            int unreadNewMatchNotificationCount =  MyDataBase.getInstance(this).getAllUnreadNotificationCount(userDetails.getId(), MyDataBase.NOTIFICATION_TYPE_NEW_MATCH);
//                            if(unreadNewMatchNotificationCount>1)
//                            messageBody = getResources().getString(R.string.you_have)+" "+unreadNewMatchNotificationCount+" "+getResources().getString(R.string.new_matches);
//
//                            if(userDetails.getPushNotification())
//                            generateNotification(intent, _type, messageBody, NOTIFICATION_ID_FOR_NEW_MATCH);
//                        }
////
//
//                    }
//
//
//                    if(_type == StaticData.NOTIFICATION_TYPE.LIKE.ordinal())
//                    {
//                        //insertNotificationIntoDatabase
//                        String senderId = remoteMessageData.get("doer_id");
//                        String receiverId = remoteMessageData.get("viewer_id");
//                        MyDataBase.getInstance(this).insertNotificationIntoDatabase(senderId, receiverId, null, null, MyDataBase.NOTIFICATION_TYPE_LIKE);
//
//
//                        if(ifAppIsOpen() == false)
//                        {
//                            int unreadLikeNotificationCount =  MyDataBase.getInstance(this).getAllUnreadNotificationCount(userDetails.getId(), MyDataBase.NOTIFICATION_TYPE_LIKE);
//                            if(unreadLikeNotificationCount>1)
//                                messageBody = getResources().getString(R.string.you_have)+" "+unreadLikeNotificationCount+" "+getResources().getString(R.string.likes);
//
//                            if(userDetails.getPushNotification())
    //                              generateNotification(intent, _type, messageBody, NOTIFICATION_ID_FOR_LIKE);
//                        }
//
//
//                    }
//
//
//                }else
//                {
//                    return;
//                }
//
//            }else
//            {
//                return;
//            }
//
//
//        }else
//        {
//            return;
//        }
//
//
//    }


    public void generateNotification(Intent intent, String messageBody, int notificationId) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private boolean ifAppIsOpen() {
        boolean mainScreenOpen = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo != null) {
            if (componentInfo.getPackageName().equalsIgnoreCase(getApplicationContext().getPackageName())) {
                mainScreenOpen = true;
            }
        }
        return mainScreenOpen;
    }

    private void sendBroadCast(String broadCastKey, Intent intent, int notificationType) {
        Intent myIntent = new Intent(broadCastKey);
        myIntent.putExtra("notify_event", notificationType);
        myIntent.putExtra("matcher_id", intent.getStringExtra("matcher_id"));
        myIntent.putExtra("profile_image", intent.getStringExtra("profile_image"));
        myIntent.putExtra("name", intent.getStringExtra("name"));
        sendBroadcast(myIntent);
    }
}
