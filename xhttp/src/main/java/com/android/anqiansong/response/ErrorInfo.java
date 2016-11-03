package com.android.anqiansong.response;

/**
 * 错误信息载体
 * <a href="http://blog.csdn.net/qq243223991">安前松博客</a>
 */

public class ErrorInfo {
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 错误码
     */
    private int errorCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
