package com.flj.latte.ec.main.personal.order;

import android.app.Activity;
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
import com.flj.latte.ec.main.personal.PersonalDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IError;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class OrderListDelegate extends LatteDelegate {

    private String mType = null;
    private final int RESULTCODE = 200;
    private RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_list;
    }

    OrderListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mType = args.getString(PersonalDelegate.ORDER_TYPE);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_order_list);
        $(R.id.icon_back).setOnClickListener(view -> {
            getSupportDelegate().pop();
        });
    }

    @Override

    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        int status = -1;
        String url = API.Config.getDomain() + API.ORDER_LIST;
        LatteLogger.d("url", url);
        final WeakHashMap<String, Object> weakHashMap = new WeakHashMap<>();

        switch (mType) {
            case "all":
                weakHashMap.put("page", 1);
                weakHashMap.put("per_page", 10);
                break;
            case "pay":
                weakHashMap.put("page", 1);
                weakHashMap.put("per_page", 10);
                weakHashMap.put("status", 0);
                status = 0;
                break;
            case "receive":
                weakHashMap.put("page", 1);
                weakHashMap.put("per_page", 10);
                weakHashMap.put("status", 1);
                status = 1;
                break;
            case "evaluate":
                weakHashMap.put("page", 1);
                weakHashMap.put("per_page", 10);
                weakHashMap.put("status", 2);
                status = 2;
                break;
            case "after_market":
                weakHashMap.put("page", 1);
                weakHashMap.put("per_page", 10);
                weakHashMap.put("status", 3);
                status = 3;
                break;
        }

        final String jsonString = JSON.toJSONString(weakHashMap);

        int finalStatus = status;
        RestClient.builder()
                .loader(getContext())
                .url(url)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("url", response);
                        LogUtils.e("url:" + response);
                        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(manager);
                        final List<MultipleItemEntity> data =
                                new OrderListDataConverter().setJsonData(response).convert();
                        adapter = new OrderListAdapter(OrderListDelegate.this, data, finalStatus);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        LatteLogger.d("url", code);
                        LatteLogger.d("url", msg);
                    }
                })
                .build()
                .post();
    }

    public int getResultCode() {
        return RESULTCODE;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == RESULTCODE && resultCode == Activity.RESULT_OK) {
            int removePosition = data.getInt("position");
            adapter.remove(removePosition);
            //删除成功重新设置界面价格
            //更新数据
            adapter.notifyItemRangeChanged(removePosition, adapter.getItemCount());
        }
    }
}