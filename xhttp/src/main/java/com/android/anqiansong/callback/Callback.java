package com.android.anqiansong.callback;

import com.android.anqiansong.response.ErrorInfo;

/**
 * 字符串解析后回调</br>
 * XHttp作者博客:<a href="http://blog.csdn.net/qq243223991">安前松博客</a></br>
 */

public abstract class Callback<T> {
    /**
     * 成功返回结果
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 失败返回结果,非必要实现
     *
     * @param errorInfo
     */
    public void onFailed(ErrorInfo errorInfo) {

    }

    /**
     * 上传进度,当需要上传文件时调用
     * @param currentSize 当前已上传的文件大小(单位:kb)
     * @param totalSize 需要上传的文件大小(单位:kb)
     * @param progress 当前上传进度(0-1)
     * @param networkSpeed 当前上传网速(单位秒)
     */
    public void onUploadProgress(long currentSize, long totalSize, float progress, long networkSpeed){

    }
    /**
     * 下载进度,当需要下载文件时调用
     * @param currentSize 当前已上传的文件大小(单位:kb)
     * @param totalSize 需要上传的文件大小(单位:kb)
     * @param progress 当前上传进度(0-1)
     * @param networkSpeed 当前上传网速(单位秒)
     */
    public void onDownloadProgress(long currentSize, long totalSize, float progress, long networkSpeed){

    }
}
