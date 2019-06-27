package com.mytodolist.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mytodolist.models.UserModel;

import java.lang.reflect.Type;


public class SharedPreference {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final String MM_secrets = "secrets";

    private static SharedPreference mInstance = null;
    public String TAG = getClass().getName();

    public static SharedPreference getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPreference();
        }
        return mInstance;
    }

    // Shared Preferences start

    public SharedPreferences setSharedPref(Context context) {
        return sp = (sp == null) ? context.getSharedPreferences(MM_secrets, Context.MODE_PRIVATE) : sp;
    }

    public SharedPreferences.Editor setEditor(Context context) {
        return editor = (editor == null) ? setSharedPref(context).edit() : editor;
    }

    // ********** clear prefrences **********//
    public void clearPreferences() {
        editor.clear();
        editor.apply();
    }

    // ********** set string prefrences **********//
    public void setStringInPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringFromPref(String key) {
        return getStringFromPref(key, null);
    }

    public String getStringFromPref(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    // ********** Int **********//
    public void setIntInPref(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntInPref(String key) {
        return getIntInPref(key, -1);
    }

    public int getIntInPref(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    // ********** Long **********//
    public void setLongInPref(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLongInPref(String key) {
        return getLongInPref(key, -1);
    }

    public long getLongInPref(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    // ********** Float **********//
    public void setFloatInPref(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloatInPref(String key) {
        return getFloatInPref(key, -1);
    }

    public float getFloatInPref(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    // ********** Boolean **********//
    public void setBooleanInPref(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanInPref(String key) {
        return sp.getBoolean(key, false);
    }

    // Shared Preferences end

    // storing user detail
    public UserModel getUserDetails() {
        String params = sp.getString("userDetail", null);

        if (params == null)
            return null;

        Type mapType = new TypeToken<UserModel>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(params, mapType);
    }

    // getting user detail
    public void setUserDetails(UserModel params) {
        if (params == null) {
            return;
        }
        Type mapType = new TypeToken<UserModel>() {
        }.getType();
        Gson gson = new Gson();
        String userMap = gson.toJson(params, mapType);

        editor.putString("userDetail", userMap);
        editor.apply();
    }

}
