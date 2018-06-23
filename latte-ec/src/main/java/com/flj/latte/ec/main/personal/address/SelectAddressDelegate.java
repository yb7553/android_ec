package com.flj.latte.ec.main.personal.address;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
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
 * @Describe 地址选择器
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/21 0021.
 */
public class SelectAddressDelegate extends LatteDelegate implements ISuccess, IFailure, View.OnClickListener {
    private RecyclerView mRecyclerView = null;
    private AppCompatTextView tv_address_title;
    private AppCompatButton btn_address_add;

    @Override
    public void onClick(View view) {

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_address;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_address);
        tv_address_title = $(R.id.tv_address_title);
        tv_address_title.setText("地址选择");
        btn_address_add = $(R.id.btn_address_add);
        btn_address_add.setVisibility(View.GONE);
        $(R.id.icon_address_add).setVisibility(View.GONE);
        initData();
    }

    private void initData() {
        final String addressUrl = API.Config.getDomain() + API.CONFIGNEE_CHOOSE;
        LatteLogger.d("addressUrl", addressUrl);
        final WeakHashMap<String, Object> address = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        address.put("userId", mUserId);
        final String jsonString = JSON.toJSONString(address);
        RestClient.builder()
                .url(addressUrl)
                .loader(getContext())
                .raw(jsonString)
                .success(this)
                .failure(this)
                .build()
                .post();
    }

    @Override
    public void onSuccess(String response) {
        LogUtils.e("AddressDelegate", response);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final List<MultipleItemEntity> data =
                new AddressDataConverter().setJsonData(response).convert();
        final SelectAddressAdapter addressAdapter = new SelectAddressAdapter(data,this);
        mRecyclerView.setAdapter(addressAdapter);
    }

    @Override
    public void onFailure() {
        ToastUtil.showToast(getContext(), "服务器繁忙");
    }
}
