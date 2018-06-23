package com.flj.latte.ec.main.cart;

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

public class ShopCartDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        //防止用户初次注册，点击购物车崩溃的bug
        if (null == dataArray) return null;
        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);
            ///final String thumb = data.getString("thumb");
            final String thumb = data.getJSONObject("product").getJSONObject("default_photo").getString("large");
            //final String desc = data.getString("desc");
            final String desc = data.getJSONObject("product").getString("name");
            ///final String title = data.getString("title");
            final String title = data.getJSONObject("product").getString("name");
            final int id = data.getInteger("id");
            final int count = data.getInteger("amount");
            final double price = data.getDouble("price");
            final int goods_id = data.getInteger("goods_id");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ShopCartItemType.SHOP_CART_ITEM)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.IMAGE_URL, thumb)
                    .setField(ShopCartItemFields.TITLE, title)
                    .setField(ShopCartItemFields.DESC, desc)
                    .setField(ShopCartItemFields.COUNT, count)
                    .setField(ShopCartItemFields.PRICE, price)
                    .setField(ShopCartItemFields.IS_SELECTED, false)
                    .setField(ShopCartItemFields.POSITION, i)
                    .setField(ShopCartItemFields.GOODS_ID, goods_id)
                    .build();

            dataList.add(entity);
        }

        return dataList;
    }
}
