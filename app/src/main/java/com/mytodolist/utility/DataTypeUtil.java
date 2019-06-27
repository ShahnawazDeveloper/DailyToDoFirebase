package com.mytodolist.utility;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.mytodolist.BuildConfig;

public class DataTypeUtil {

    private static DataTypeUtil mInstance = null;
    public String TAG = getClass().getName();
    private static Toast toast;

    public static DataTypeUtil getInstance() {
        if (mInstance == null) {
            mInstance = new DataTypeUtil();
        }
        return mInstance;
    }

    public String getAppVersion() {
        String appVersion = "";
        try {
            // with package manager
            //appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            appVersion = BuildConfig.VERSION_NAME;// with build config
        } catch (Exception e) {
            e.printStackTrace();
            return appVersion;
        }
        return appVersion;
    }

    public void showToast(Context context, String message) {// change it like awk
        if (message == null || message.isEmpty() || context == null)
            return;

        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showToastLengthLong(Context context, String message) {
        if (message == null || message.isEmpty() || context == null)
            return;

        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // get response status 0 true or 1 false
    public boolean isResponseSuccess(String value) {
        return value.equals("0");
    }

    public boolean intToBoolean(int value) {
        // Convert 0 to false else true.
        return value != 0;
    }

    public int booleanToInt(boolean value) {
        // Convert true to 1 and false to 0.
        return value ? 1 : 0;
    }

    public String intBooleanToString(int value) {
        // Convert 0 to false else true.
        return value != 0 ? "true" : "false";
    }

    public boolean stringToBoolean(String value) {
        // Convert "true" to true else false.
        return value.equals("true");
    }

    public int convertIsUploadedToUploadRequired(boolean value) {
        // Convert true to 0 and false to 1.
        return value ? 0 : 1;
    }


}