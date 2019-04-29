package com.instwall.xutilsdemo;

import android.app.Application;

import org.xutils.x;

public class xUtilsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
