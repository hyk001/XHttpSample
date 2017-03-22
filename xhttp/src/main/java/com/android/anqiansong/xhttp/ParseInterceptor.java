package com.android.anqiansong.xhttp;

/**
 * 解析拦截器接口声明,用户可以实现此接口并调用{@link XHttp#addParseInterceptor(ParseInterceptor)}<br>
 * 方法实现拦截,可以在解析json之前做一些工作,例如session失效的错误码拦截处理等.
 * <a href="http://blog.csdn.net/qq243223991">安前松博客</a>
 */


public interface ParseInterceptor {

    /**
     * 做拦截处理
     *
     * @param json 返回的json数据mt
     * @return true, 不回调, false:回调
     */
    boolean doParseInterceptor(String json);
}
