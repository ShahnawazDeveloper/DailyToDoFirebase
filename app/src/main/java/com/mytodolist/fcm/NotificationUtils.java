package com.mytodolist.fcm;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static NotificationUtils mInstance = null;

    public static NotificationUtils getInstance() {
        if (mInstance == null) {
            mInstance = new NotificationUtils();
        }
        return mInstance;
    }

    public static int currentTimeMillis() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
