package com.konsung.monitor.ui;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.konsung.monitor.util.UiUtils;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 入口类
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UiUtils.initData(this);
        startAppDevice();
    }

    /**
     * 启动appdevice
     */
    public void startAppDevice() {
        PackageManager packageManager = getPackageManager();
        Intent intent;
        try {
            intent = packageManager.getLaunchIntentForPackage("org.qtproject.qt5.android.bindings");
            if (intent == null) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
