package com.flj.latte.ec.main.personal.send;

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

public class SendDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = array.size();
        for (int i = 0; i < size; i++) {

            final JSONObject data = array.getJSONObject(i);
            final String id = data.getString("id");
            final String name = data.getString("name");
            final String phone = data.getString("mobile");
            final String city = data.getString("city");
            final boolean isDefault = data.getBoolean("is_default");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(SendItemType.ITEM_SENDSTAFF)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.NAME, name)
                    .setField(MultipleFields.TAG, isDefault)
                    .setField(SendItemFields.CITYID, city)
                    .setField(SendItemFields.PHONE, phone)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
