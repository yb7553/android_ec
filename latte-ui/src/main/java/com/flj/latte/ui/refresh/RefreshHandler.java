package com.flj.latte.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flj.latte.app.Latte;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.util.log.LatteLogger;

import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class RefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final PagingBean BEAN;
    private final RecyclerView RECYCLERVIEW;
    private MultipleRecyclerAdapter mAdapter = null;
    private final DataConverter CONVERTER;

    private RefreshHandler(SwipeRefreshLayout swipeRefreshLayout,
                           RecyclerView recyclerView,
                           DataConverter converter, PagingBean bean) {
        this.REFRESH_LAYOUT = swipeRefreshLayout;
        this.RECYCLERVIEW = recyclerView;
        this.CONVERTER = converter;
        this.BEAN = bean;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static RefreshHandler create(SwipeRefreshLayout swipeRefreshLayout,
                                        RecyclerView recyclerView, DataConverter converter) {
        return new RefreshHandler(swipeRefreshLayout, recyclerView, converter, new PagingBean());
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
                REFRESH_LAYOUT.setRefreshing(false);
            }
        }, 1000);
    }

    public void banners(String url) {
        LatteLogger.d("banners", url);
        //BEAN.setDelayed(1000);
        final WeakHashMap<String, Object> firstpage = new WeakHashMap<>();
        firstpage.put("cityId",14);
        final String jsonString = JSON.toJSONString(firstpage);

        RestClient.builder()
                .url(url)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject object = JSON.parseObject(response);
///                    BEAN.setTotal(object.getInteger("total"))
///                            .setPageSize(object.getInteger("page_size"));
//                        BEAN.setTotal(100)
//                                .setPageSize(20);
                        //设置Adapter
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        //BEAN.addIndex();
                    }
                })

                .build()
                .post();

    }

    public void      firstPage(String url) {

        //banners("http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.banner.list");
        LatteLogger.d("IUDHAS", url);
        BEAN.setDelayed(1000);
        final WeakHashMap<String, Object> firstpage = new WeakHashMap<>();
        firstpage.put("page",1);
        firstpage.put("per_page",4);
        firstpage.put("sort_key",2);
        firstpage.put("sort_value",2);
        final String jsonString = JSON.toJSONString(firstpage);

        RestClient.builder()
                .url(url)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject object = JSON.parseObject(response);
///                    BEAN.setTotal(object.getInteger("total"))
///                            .setPageSize(object.getInteger("page_size"));
//                        BEAN.setTotal(100)
//                                .setPageSize(20);
                        //设置Adapter
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        //BEAN.addIndex();
                    }
                })

                .build()
                .post();


        LatteLogger.d("IUDHAS", url);
        BEAN.setDelayed(1000);
        final WeakHashMap<String, Object> firstpage1 = new WeakHashMap<>();
        firstpage1.put("page",1);
        firstpage1.put("per_page",4);
        firstpage1.put("sort_key",5);
        firstpage1.put("sort_value",2);
        final String jsonString1 = JSON.toJSONString(firstpage1);

        RestClient.builder()
                .url(url)
                .raw(jsonString1)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject object = JSON.parseObject(response);
///                    BEAN.setTotal(object.getInteger("total"))
///                            .setPageSize(object.getInteger("page_size"));
//                        BEAN.setTotal(100)
//                                .setPageSize(20);
                        //设置Adapter
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        //BEAN.addIndex();
                    }
                })

                .build()
                .post();


        LatteLogger.d("IUDHAS", url);
        BEAN.setDelayed(1000);
        final WeakHashMap<String, Object> firstpage2 = new WeakHashMap<>();
        firstpage2.put("page",1);
        firstpage2.put("per_page",4);
        firstpage2.put("sort_key",4);
        firstpage2.put("sort_value",2);
        final String jsonString2 = JSON.toJSONString(firstpage2);

        RestClient.builder()
                .url(url)
                .raw(jsonString2)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject object = JSON.parseObject(response);
///                    BEAN.setTotal(object.getInteger("total"))
///                            .setPageSize(object.getInteger("page_size"));
//                        BEAN.setTotal(100)
//                                .setPageSize(20);
                        //设置Adapter
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        //BEAN.addIndex();
                    }
                })

                .build()
                .post();


    }

    private void paging(final String url) {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getData().size() < pageSize || currentCount >= total) {
            mAdapter.loadMoreEnd(true);
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RestClient.builder()
                            .url(url + index)
                            .success(response -> {
                                LatteLogger.json("paging", response);
                                CONVERTER.clearData();
                                mAdapter.addData(CONVERTER.setJsonData(response).convert());
                                //累加数量
                                BEAN.setCurrentCount(mAdapter.getData().size());
                                mAdapter.loadMoreComplete();
                                BEAN.addIndex();
                            })
                            .build()
                            .get();
                }
            }, 1000);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }


    @Override
    public void onLoadMoreRequested() {
        paging("refresh.php?index=");
    }
}
