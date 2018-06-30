package com.flj.latte.ec.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IFailure;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe 更多评论
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/29 0029.
 */
public class MoreGoosCommentDelegate extends LatteDelegate implements View.OnClickListener {
    private static final String ARG_GOODS_DATA = "ARG_GOODS_DATA";
    private JSONObject mData = null;
    private int comment_count = 0;
    private RecyclerView rv_comment;
    MoreGoodsCommentAdapter adapter;
    private boolean isErr;
    private int mCurrentCounter = 0;
    private int TOTAL_COUNTER = 0;
    private int page = 1;
    private int goodsId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        final String goodsData;
        if (args != null) {
            goodsData = args.getString(ARG_GOODS_DATA);
            mData = JSON.parseObject(goodsData);
            try {
                comment_count = mData.getInteger("comment_count");
            } catch (Exception e) {
                comment_count = 0;
            }
            goodsId = mData.getInteger("id");
            getCommentData(goodsId, page);


        }
    }

    //获取评论内容
    private void getCommentData(int goodsData, int page) {
        final String goodesdetailUrl = API.Config.getDomain() + API.GET_COMMENT_DETAIL;
        LatteLogger.d("IUDHAS", goodesdetailUrl);
        final WeakHashMap<String, Object> goodesComment = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        //mGoodsId=96;
        goodesComment.put("idValue", goodsData);
        //获取商品评论固定0
        goodesComment.put("commentType", 0);
        goodesComment.put("page", page);
        goodesComment.put("per_page", 5);
        goodesComment.put("userId", mUserId);

        final String jsonString = JSON.toJSONString(goodesComment);
        LogUtils.e("jsonString", jsonString);
        RestClient.builder()
                .url(goodesdetailUrl)
                //.params("goods_id", mGoodsId)
                .loader(getContext())
                .raw(jsonString)
                // .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        LogUtils.e("goodesdetail", response);
                        List<MultipleItemEntity> list = new CommentDataConverter().setJsonData(response).
                                convert();
                        if (null == list) {
                            if (null != adapter) {
                                //数据全部加载完毕
                                adapter.loadMoreEnd();
                            }
                            return;
                        }
                        TOTAL_COUNTER += list.size();
                        setAdapate(list);
                    }
                }).failure(new IFailure() {
            @Override
            public void onFailure() {
                ToastUtil.show(getContext(), "服务器忙，请稍后再试");
            }
        })
                .build()
                .post();

    }

    //设置适配器
    private void setAdapate(List<MultipleItemEntity> list) {
        setCurrentList(list);
        if (null != adapter) return;
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_comment.setLayoutManager(manager);
        adapter = new MoreGoodsCommentAdapter(list, getParentDelegate());
        rv_comment.setAdapter(adapter);
        adapter.setOnLoadMoreListener(() -> rv_comment.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    //数据全部加载完毕
                    adapter.loadMoreEnd();
                } else {
                    if (isErr) {
                        //成功获取更多数据（可以直接往适配器添加数据）
                        adapter.addData(getCurrentList());
                        mCurrentCounter = adapter.getData().size();
                        //主动调用加载完成，停止加载
                        adapter.loadMoreComplete();
                    } else {
                        //获取更多数据失败
                        isErr = true;
                        //  ToastUtil.showToast(getContext(), "");
                        //同理，加载失败也要主动调用加载失败来停止加载（而且该方法会提示加载失败）
                        adapter.loadMoreFail();

                    }
                }
            }

        }, 100), rv_comment);


        rv_comment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
                    //加载更多
                    getCommentData(goodsId, ++page);
                }
            }
        });

    }

    private List<MultipleItemEntity> getCurrentList() {

        return currentlist;
    }

    List<MultipleItemEntity> currentlist;

    private void setCurrentList(List<MultipleItemEntity> list) {
        this.currentlist = list;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.icon_comment_back) {
            getSupportDelegate().pop();
        }
    }

    public static MoreGoosCommentDelegate create(String goodsData) {
        final Bundle args = new Bundle();
        args.putString(ARG_GOODS_DATA, goodsData);
        final MoreGoosCommentDelegate delegate = new MoreGoosCommentDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_more_comment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView();
        int id = mData.getInteger("id");
        try {
            comment_count = mData.getInteger("comment_count");
        } catch (Exception e) {
            comment_count = 0;
        }

    }

    private void initView() {

        $(R.id.icon_comment_back).setOnClickListener(this);
        rv_comment = $(R.id.rv_comment);
    }
}
