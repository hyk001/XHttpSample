package com.android.anqiansong.xhttpsample;

import android.app.Application;

import com.android.anqiansong.xhttp.XHttp;

public class UApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XHttp.init(this);// 初始化
    }
}
