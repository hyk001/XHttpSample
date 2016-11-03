package com.android.anqiansong.request;

import java.util.HashMap;

/**
 * 基本的请求载体| The Basic Request Entity
 * <a href="http://blog.csdn.net/qq243223991">安前松博客</a>
 */

public class XBase {
    /**
     * 请求地址
     */
    public String requestUrl;
    /**
     * 请求头
     */
    public HashMap<String,String> requestHeaders;
}
