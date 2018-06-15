package com.flj.latte.bean;

import java.io.Serializable;

/**
 * Created by kangbixing_dian91 on 2016/3/3.
 */
public class PageBean implements Serializable {
    private String url;
    private int currentPage;
    private int pageType;

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
