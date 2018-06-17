package com.flj.latte.ec.main.personal.address;

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

public class AddressDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = array.size();
        for (int i = 0; i < size; i++) {

            final JSONObject data = array.getJSONObject(i);
            final int id = data.getInteger("id");
            final String name = data.getString("receiverName");
            final String phone = data.getString("receiverMobile");
            final String address = data.getString("receiverAddress");
            final boolean isDefault = data.getBoolean("receiverDefault");
            final int provinceId = data.getInteger("provinceId");
            final int cityId = data.getInteger("cityId");
            final int districtId = data.getInteger("districtId");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(AddressItemType.ITEM_ADDRESS)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.NAME, name)
                    .setField(MultipleFields.TAG, isDefault)
                    .setField(AddressItemFields.ADDRESS, address)
                    .setField(AddressItemFields.PHONE, phone)
                    .setField(AddressItemFields.PROVINCEID, provinceId)
                    .setField(AddressItemFields.CITYID, cityId)
                    .setField(AddressItemFields.DISTRICTID, districtId)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
