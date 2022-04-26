package com.kissanfactory.groceryuserapplication.fcm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.kissanfactory.groceryuserapplication.Activities.MainActivity;
import com.kissanfactory.groceryuserapplication.Activities.MyApplication;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.session_manager.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by android_studio on 17/2/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private String vNotificationType = "0", vMessage = "", vREQ_ID = "", vAgent_Id = "", vTitle = "", vNoti_Type_String = "";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private SessionManager sessionManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sessionManager = new SessionManager(this);

        if (remoteMessage.getData().size() > 0) {
            String data = "";
            try {
                //       sessionManager = new SessionManager(this);
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Log.w("fcm", "received notification");
                Log.e("dataChat", remoteMessage.getData().toString());


                if (remoteMessage.getData().size() > 0) {
                    Map<String, String> params = remoteMessage.getData();
                    JSONObject object = new JSONObject(params);
                    System.out.println("notification_data1..." + object.toString());
                    NotificationDTO notificationDTO = new NotificationDTO();
                    notificationDTO.setTypeid(object.has("typeid") ? object.getString("typeid") : "");
                    notificationDTO.setId(object.has("id") ? object.getString("id") : "");
                    notificationDTO.setTitle(object.has("title") ? object.getString("title") : "");
                    notificationDTO.setMessage(object.has("message") ? object.getString("message") : "");
//
                    vNotificationType = !notificationDTO.getTypeid().equals("") ? notificationDTO.getTypeid() : "";
                    vREQ_ID = !notificationDTO.getId().equals("") ? notificationDTO.getId() : "";
                    vMessage = !notificationDTO.getMessage().equals("") ? notificationDTO.getMessage() : "";
                    vTitle = !notificationDTO.getTitle().equals("") ? notificationDTO.getTitle() : "";
                }
            } catch (Exception e) {

            }
            if (sessionManager.isLogin()) {
                sendNotification();
            }
        }
    }

    private void sendNotification() {
        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // foreground case.... or  screen visible
            if (vNotificationType.trim().equalsIgnoreCase("1")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("NOTIFICATION_TYPE", vNotificationType);
//                intent.putExtra("REQ_ID", vREQ_ID);
//                intent.putExtra("TITLE", vTitle);
//                intent.putExtra("MSG_BODY", vMessage);
//                intent.putExtra("AGENT_ID", vAgent_Id);
                noti(vMessage);
                //   NotificationUtils.startActivityFromBackground(getApplicationContext(), vNotificationType, vREQ_ID, "", "", vAgent_Id);
            } else if (vNotificationType.trim().equalsIgnoreCase("2")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("NOTIFICATION_TYPE", vNotificationType);
//                intent.putExtra("REQ_ID", vREQ_ID);
//                intent.putExtra("TITLE", vTitle);
//                intent.putExtra("MSG_BODY", vMessage);
//                intent.putExtra("AGENT_ID", vAgent_Id);
                noti(vMessage);
                //  NotificationUtils.startActivityFromBackground(getApplicationContext(), vNotificationType, vREQ_ID, vTitle, vMessage, vAgent_Id);
            } else {
                noti(vMessage);
            }
        } else {
            if (MyApplication.isActivityVisible()) {
//////// bakground mode //////
                if (vNotificationType.trim().equalsIgnoreCase("1")) {
//                    Intent intent = new Intent(this, MainActivity.class);
//                    intent.putExtra("NOTIFICATION_TYPE", vNotificationType);
//                    intent.putExtra("REQ_ID", vREQ_ID);
//                    intent.putExtra("TITLE", vTitle);
//                    intent.putExtra("MSG_BODY", vMessage);
//                    intent.putExtra("AGENT_ID", vAgent_Id);
                    noti(vMessage);
                    //     NotificationUtils.startActivityFromBackground(getApplicationContext(), vNotificationType, vREQ_ID, vTitle, vMessage, vAgent_Id);
                    return;
                } else if (vNotificationType.equalsIgnoreCase("2")) {
//                    Intent intent = new Intent(this, MainActivity.class);
//                    intent.putExtra("NOTIFICATION_TYPE", vNotificationType);
//                    intent.putExtra("REQ_ID", vREQ_ID);
//                    intent.putExtra("TITLE", vTitle);
//                    intent.putExtra("MSG_BODY", vMessage);
//                    intent.putExtra("AGENT_ID", vAgent_Id);
                    noti(vMessage);
                    //   NotificationUtils.startActivityFromBackground(getApplicationContext(), vNotificationType, vREQ_ID, vTitle, vMessage, vAgent_Id);
                    return;
                } else {
                    noti(vMessage);
                }
            }////////////////////app not in background//////////
            else if (vNotificationType.trim().equalsIgnoreCase("1")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra("NOTIFICATION_TYPE", vNotificationType);
//                intent.putExtra("REQ_ID", vREQ_ID);
//                intent.putExtra("TITLE", vTitle);
//                intent.putExtra("MSG_BODY", vMessage);
//                intent.putExtra("AGENT_ID", vAgent_Id);
                noti(vMessage);

            } else if (vNotificationType.trim().equalsIgnoreCase("2")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra("NOTIFICATION_TYPE", vNotificationType);
//                intent.putExtra("REQ_ID", vREQ_ID);
//                intent.putExtra("TITLE", vTitle);
//                intent.putExtra("MSG_BODY", vMessage);
//                intent.putExtra("AGENT_ID", vAgent_Id);
                noti(vMessage);
            } else {
                noti(vMessage);
            }

        }
    }


    public void noti(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        //  Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.splash_image)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                //   .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        defaults = defaults | Notification.FLAG_AUTO_CANCEL;
        notificationBuilder.setDefaults(defaults);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /////////////////for oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void notif(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(vMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        NotificationUtils.startActivityFromBackground(getApplicationContext(), vNotificationType, vREQ_ID, vTitle, vMessage, vAgent_Id);
    }
}


