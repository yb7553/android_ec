package com.flj.latte.ec.main.personal.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.main.personal.PersonalDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IError;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class OrderListDelegate extends LatteDelegate {

    private String mType = null;
    private final int RESULTCODE = 200;
    private RecyclerView mRecyclerView = null;
    private LinearLayout ll_status;
    private TextView tv_pay, tv_all, tv_send_goods, tv_confirm_goods, tv_comment;
    private View v_pay, v_all, v_send_goods, v_confirm_goods, v_comment;
    List<MultipleItemEntity> allList, payList, sendgoodsList, confirmGoodsList, commentList;


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
        tv_pay = $(R.id.tv_pay);
        tv_all = $(R.id.tv_all);
        tv_send_goods = $(R.id.tv_send_goods);
        tv_confirm_goods = $(R.id.tv_confirm_goods);
        tv_comment = $(R.id.tv_comment);

        v_pay = $(R.id.v_pay);
        v_all = $(R.id.v_all);
        v_send_goods = $(R.id.v_send_goods);
        v_confirm_goods = $(R.id.v_confirm_goods);
        v_comment = $(R.id.v_comment);
        ll_status = $(R.id.ll_statuss);

        $(R.id.icon_back).setOnClickListener(view -> {
            getSupportDelegate().pop();
        });
        $(R.id.ll_all).setOnClickListener((View view) -> {
            tv_all.setTextColor(getResources().getColor(R.color.color_text_dark));
            tv_pay.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_send_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_confirm_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_comment.setTextColor(getResources().getColor(R.color.color_dark_gray));
            v_all.setVisibility(View.VISIBLE);
            v_pay.setVisibility(View.INVISIBLE);
            v_send_goods.setVisibility(View.INVISIBLE);
            v_confirm_goods.setVisibility(View.INVISIBLE);
            v_comment.setVisibility(View.INVISIBLE);
            adapter = new OrderListAdapter(OrderListDelegate.this, allList, -1);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));


        });
        $(R.id.ll_pay).setOnClickListener(view -> {
            tv_all.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_pay.setTextColor(getResources().getColor(R.color.color_text_dark));
            tv_send_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_confirm_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_comment.setTextColor(getResources().getColor(R.color.color_dark_gray));

            v_all.setVisibility(View.INVISIBLE);
            v_pay.setVisibility(View.VISIBLE);
            v_send_goods.setVisibility(View.INVISIBLE);
            v_confirm_goods.setVisibility(View.INVISIBLE);
            v_comment.setVisibility(View.INVISIBLE);

            adapter = new OrderListAdapter(OrderListDelegate.this, payList, 0);
            mRecyclerView.setAdapter(adapter);adapter.notifyDataSetChanged();
            mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));

        });
        $(R.id.ll_send_goods).setOnClickListener(view -> {
            tv_all.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_pay.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_send_goods.setTextColor(getResources().getColor(R.color.color_text_dark));
            tv_confirm_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_comment.setTextColor(getResources().getColor(R.color.color_dark_gray));

            v_all.setVisibility(View.INVISIBLE);
            v_pay.setVisibility(View.INVISIBLE);
            v_send_goods.setVisibility(View.VISIBLE);
            v_confirm_goods.setVisibility(View.INVISIBLE);
            v_comment.setVisibility(View.INVISIBLE);

            adapter = new OrderListAdapter(OrderListDelegate.this, sendgoodsList, 1);
            mRecyclerView.setAdapter(adapter);adapter.notifyDataSetChanged();
            mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));

        });
        $(R.id.ll_confirm_goods).setOnClickListener(view -> {
            tv_all.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_pay.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_send_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_confirm_goods.setTextColor(getResources().getColor(R.color.color_text_dark));
            tv_comment.setTextColor(getResources().getColor(R.color.color_dark_gray));
            v_all.setVisibility(View.INVISIBLE);
            v_pay.setVisibility(View.INVISIBLE);
            v_send_goods.setVisibility(View.INVISIBLE);
            v_confirm_goods.setVisibility(View.VISIBLE);
            v_comment.setVisibility(View.INVISIBLE);

            adapter = new OrderListAdapter(OrderListDelegate.this, confirmGoodsList, 2);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();            mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));


        });
        $(R.id.ll_comment).setOnClickListener(view -> {
            tv_all.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_pay.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_send_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_confirm_goods.setTextColor(getResources().getColor(R.color.color_dark_gray));
            tv_comment.setTextColor(getResources().getColor(R.color.color_text_dark));
            v_all.setVisibility(View.INVISIBLE);
            v_pay.setVisibility(View.INVISIBLE);
            v_send_goods.setVisibility(View.INVISIBLE);
            v_confirm_goods.setVisibility(View.INVISIBLE);
            v_comment.setVisibility(View.VISIBLE);

            adapter = new OrderListAdapter(OrderListDelegate.this, commentList, 3);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));

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
                ll_status.setVisibility(View.VISIBLE);
                weakHashMap.put("page", 1);
                weakHashMap.put("per_page", 20);
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
                        allList = data;
                        adapter = new OrderListAdapter(OrderListDelegate.this, data, finalStatus);
                        mRecyclerView.setAdapter(adapter);
                        sortlist(data);
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

    //对 总数据进行分类
    private void sortlist(List<MultipleItemEntity> data) {
        payList = new ArrayList<MultipleItemEntity>();
        sendgoodsList = new ArrayList<MultipleItemEntity>();
        confirmGoodsList = new ArrayList<MultipleItemEntity>();
        commentList = new ArrayList<MultipleItemEntity>();
        if (null == data) return;
        for (MultipleItemEntity entity :
                data) {
            if (StringUtils.isEmpty(entity.getField(OrderItemFields.STATUSDESC))) {
                commentList.add(entity);
            } else {
                if (entity.getField(OrderItemFields.STATUSDESC).toString().contains("付款"))
                    payList.add(entity);
                if (entity.getField(OrderItemFields.STATUSDESC).toString().contains("发货"))
                    sendgoodsList.add(entity);
                if (entity.getField(OrderItemFields.STATUSDESC).toString().contains("收货"))
                    confirmGoodsList.add(entity);
                if (entity.getField(OrderItemFields.STATUSDESC).toString().contains("评价"))
                    commentList.add(entity);
            }

        }

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