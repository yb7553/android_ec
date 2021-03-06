package com.flj.latte.ec.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;

/**
 * Created by yb
 */

public class GoodsInfoDelegate extends LatteDelegate {

    private static final String ARG_GOODS_DATA = "ARG_GOODS_DATA";
    private JSONObject mData = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        final String goodsData;
        if (args != null) {
            goodsData = args.getString(ARG_GOODS_DATA);
            mData = JSON.parseObject(goodsData);
        }
    }

    public static GoodsInfoDelegate create(String goodsInfo) {
        final Bundle args = new Bundle();
        args.putString(ARG_GOODS_DATA, goodsInfo);
        final GoodsInfoDelegate delegate = new GoodsInfoDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final AppCompatTextView goodsInfoTitle = $(R.id.tv_goods_info_title);
        final AppCompatTextView goodsInfoDesc = $(R.id.tv_goods_info_desc);
        final AppCompatTextView goodsInfoPrice = $(R.id.tv_goods_info_price);
        final AppCompatTextView tv_goods_promote=$(R.id.tv_goods_promote);
        final AppCompatTextView tv_goods_gifts=$(R.id.tv_goods_gifts);

        final String name = mData.getString("name");
        final String desc = mData.getString("description");
        final double price = mData.getDouble("price");
        int is_gifts=mData.getInteger("is_gifts");
        int is_promote=mData.getInteger("is_promote");
        goodsInfoTitle.setText(name);
        goodsInfoDesc.setText(desc);
        goodsInfoPrice.setText(String.valueOf(price));
        //促销标志
        tv_goods_promote.setVisibility(is_promote==1?View.VISIBLE:View.GONE);
        //赠品标志
        tv_goods_gifts.setVisibility(is_gifts==1?View.VISIBLE:View.GONE);
    }
}
