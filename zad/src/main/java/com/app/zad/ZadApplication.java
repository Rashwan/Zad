package com.app.zad;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by rashwan on 12/17/16.
 */

public class ZadApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
    }
}
