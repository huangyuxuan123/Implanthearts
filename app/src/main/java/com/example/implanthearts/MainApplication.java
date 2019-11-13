package com.example.implanthearts;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/12/3.
 */

public class MainApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getCustomApplicationContext() {
        return mContext;
    }
}


