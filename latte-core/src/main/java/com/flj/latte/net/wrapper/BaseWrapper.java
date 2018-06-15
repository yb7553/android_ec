package com.flj.latte.net.wrapper;

import com.flj.latte.bean.PageBean;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/2.
 */

public class BaseWrapper implements Serializable {

    public boolean atLastPage = false;
    private PageBean pageBean = new PageBean();
    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
