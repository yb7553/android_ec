package com.flj.latte.ec.main.personal.address;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class AddressDelegate extends LatteDelegate implements ISuccess, View.OnClickListener {

    private RecyclerView mRecyclerView = null;
    private IconTextView mIconTextView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_address;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_address);
        mIconTextView = $(R.id.icon_address_add);
        $(R.id.icon_address_add).setOnClickListener(this);

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
        final AddressAdapter addressAdapter = new AddressAdapter(data);
        mRecyclerView.setAdapter(addressAdapter);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.icon_address_add) {
            onClickAddressAdd();
        }
    }

   // private int recode = 100;
    private void onClickAddressAdd() {
        getSupportDelegate().start(new AddressAddDelegate());
    }
}
