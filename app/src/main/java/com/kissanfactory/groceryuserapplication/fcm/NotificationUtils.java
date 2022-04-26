package com.kissanfactory.groceryuserapplication.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;


import com.kissanfactory.groceryuserapplication.Activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();
    //    private static SessionManager sessionManager;
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
//        sessionManager = new SessionManager(mContext);
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
//    public static boolean appInForeground(@NonNull Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
//        if (runningAppProcesses == null) {
//            return false;
//        }
//
//        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
//            if (runningAppProcess.processName.equals(context.getPackageName()) &&
//                    runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                return true;
//            }
//        }
//        return false;
//    }
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = true;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = true;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // message action dialog for testing...
    public static void startActivityFromBackground(Context context, String vNotificationType, String vRequest, String vTitle, String vMessage, String vAgent_Id) {
        if (vNotificationType.trim().equals("1")) {
            context.startActivity(new Intent(context, MainActivity.class)
                    .putExtra("NOTIFICATION_TYPE", vNotificationType)
                    .putExtra("REQ_ID", vRequest)
                    .putExtra("TITLE", vTitle)
                    .putExtra("MSG_BODY", vMessage)
                    .putExtra("AGENT_ID", vAgent_Id)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (vNotificationType.trim().equals("2")) {
            context.startActivity(new Intent(context, MainActivity.class)
                    .putExtra("NOTIFICATION_TYPE", vNotificationType)
                    .putExtra("REQ_ID", vRequest)
                    .putExtra("TITLE", vTitle)
                    .putExtra("MSG_BODY", vMessage)
                    .putExtra("AGENT_ID", vAgent_Id)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            // context.startActivity(new Intent(context, NotificationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public static void startHomeScreenFromForeground(Context context) {
        context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}