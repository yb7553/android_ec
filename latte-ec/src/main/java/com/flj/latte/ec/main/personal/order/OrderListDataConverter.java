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

public class OrderListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        JSONArray array = null;
        try {
            array = JSON.parseObject(getJsonData()).getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == array) return null;
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = array.getJSONObject(i);
            //final String thumb = data.getString("thumb");
            final String title = data.getString("orderSn");
            final int id = data.getInteger("orderId");
            final double price = data.getDouble("orderAmount");
            //final String time = data.getString("time");
            final int orderStatus = data.getInteger("orderStatus");
            final long addTime = data.getInteger("addTime");
            final String lsOrderGoods=data.getString("lsOrderGoods");
            final String statusDesc=data.getString("statusDesc");
            final String address=data.getString("address");
            final String deliverName=data.getString("deliverName");
            final String phone=data.getString("phone");
            final long sendTime=data.getLong("sendTime");
            final String userName=data.getString("userName");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(OrderListItemType.ITEM_ORDER_LIST)
                    .setField(MultipleFields.ID, id)
                    // .setField(MultipleFields.IMAGE_URL, thumb)
                    .setField(MultipleFields.TITLE, title)
                    .setField(OrderItemFields.PRICE, price)
                    // .setField(OrderItemFields.TIME,time)
                    .setField(MultipleFields.TAG, orderStatus)
                    .setField(OrderItemFields.TIME, addTime)
                    .setField(OrderItemFields.LSORDERGOODS, lsOrderGoods)
                    .setField(OrderItemFields.STATUSDESC, statusDesc)
                    .setField(OrderItemFields.ADDRESS, address)
                    .setField(OrderItemFields.DELIVERNAME, deliverName)
                    .setField(OrderItemFields.PHONE, phone)
                    .setField(OrderItemFields.SENDTIME, sendTime)
                    .setField(OrderItemFields.USERNAME, userName)
                    .build();

            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
