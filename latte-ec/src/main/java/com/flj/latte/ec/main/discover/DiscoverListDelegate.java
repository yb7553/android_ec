package com.flj.latte.ec.main.discover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.main.personal.PersonalDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class DiscoverListDelegate extends BottomItemDelegate {

    private String mType = null;

    private RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_discover_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final Bundle args = getArguments();
//        if (args != null) {
//            mType = args.getString(PersonalDelegate.ORDER_TYPE);
//        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_discover_list);
    }
    @Override

    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        String url = API.Config.getDomain() + API.ARTICLE_LIST;
        LatteLogger.d("url",url);
        final WeakHashMap<String, Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("page",1);
        weakHashMap.put("per_page",10);
        final String jsonString = JSON.toJSONString(weakHashMap);

        RestClient.builder()
                .loader(getContext())
                .url(url)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("response", response);
                        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(manager);
                        final List<MultipleItemEntity> data =
                                new DiscoverListDataConverter().setJsonData(response).convert();
                        final DiscoverListAdapter adapter = new DiscoverListAdapter(data);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.addOnItemTouchListener(new DiscoverListClickListener(DiscoverListDelegate.this));
                    }
                })
                .build()
                .post();
    }
}
