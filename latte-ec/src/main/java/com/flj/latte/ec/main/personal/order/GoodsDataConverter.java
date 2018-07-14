package com.flj.latte.ec.main.personal.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by yb
 */

public class GoodsDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
        JSONArray dataArray = null;
        //服务器异常连不上防止报空崩溃处理
        try {
            dataArray = JSON.parseArray(getJsonData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //防止用户初次注册，点击购物车崩溃的bug
        if (null == dataArray) return null;
        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);
            final String goodsAttr = data.getString("goodsAttr");
            final String goodsName = data.getString("goodsName");
            final String goodsPic = data.getString("goodsPic");
            final int goodsNum = data.getInteger("goodsNum");
            final int goodsId = data.getInteger("goodsId");
            final double goodsPrice = data.getDouble("goodsPrice");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, OrderGoodsAdapter.SHOP_CART_ITEM)
                    .setField(OrderItemFields.GOODSATTR, goodsAttr)
                    .setField(OrderItemFields.GOODSNAME, goodsName)
                    .setField(OrderItemFields.GOODSPIC, goodsPic)
                    .setField(OrderItemFields.GOODSNUM, goodsNum)
                    .setField(OrderItemFields.GOODSID, goodsId)
                    .setField(OrderItemFields.GOODSPRICE, goodsPrice)
                    .build();

            dataList.add(entity);
        }

        return dataList;
    }
}
