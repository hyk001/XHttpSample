package com.android.anqiansong.xhttpsample.entity;


import java.util.List;

public class JokeResponseBody {
    private int allPages;
    private int ret_code;
    private int currentPage;
    private int allNum;
    private int maxResult;
    private List<JokeItemResponse> contentlist;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public List<JokeItemResponse> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<JokeItemResponse> contentlist) {
        this.contentlist = contentlist;
    }
}
