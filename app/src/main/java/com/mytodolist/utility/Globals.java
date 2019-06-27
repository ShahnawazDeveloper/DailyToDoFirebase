package com.mytodolist.utility;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class Globals extends MultiDexApplication {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext(); // context = this ;
        SharedPreference.getInstance().setEditor(getApplicationContext());
    }

    public Context getContext() {
        return context;
    }

}
