package com.flj.latte.ec.main.personal.address;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
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
        LogUtils.e("s", getJsonData());
        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        if (array==null)return null;
        final int size = array.size();
        for (int i = 0; i < size; i++) {

            final JSONObject data = array.getJSONObject(i);
            final int id = data.getInteger("id");
            final String name = data.getString("receiverName");
            final String phone = data.getString("receiverMobile");
            String receiverProvince = ""+data.getString("receiverProvince");
            String receiverCity = ""+data.getString("receiverCity");
            String receiverDistrict = ""+data.getString("receiverDistrict");
            String address = data.getString("receiverAddress");
            String loacation = receiverProvince + (receiverProvince.equals(receiverCity) ? "" : receiverCity)
                    + (receiverDistrict.equals(receiverDistrict) ? "" : receiverDistrict);

            final boolean isDefault = data.getBoolean("receiverDefault");
            final int provinceId = data.getInteger("receiverProvinceId");
            final int cityId = data.getInteger("receiverCityId");
            final int districtId = data.getInteger("receiverDistrictId");
            final String receiverZip = data.getString("receiverZip");
            final int receiverCityId= data.getInteger("receiverCityId");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(AddressItemType.ITEM_ADDRESS)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.NAME, name)
                    .setField(MultipleFields.TAG, isDefault)
                    .setField(AddressItemFields.LOCATION,loacation)
                    .setField(AddressItemFields.ADDRESS, address)
                    .setField(AddressItemFields.PHONE, phone)
                    .setField(AddressItemFields.PROVINCEID, provinceId)
                    .setField(AddressItemFields.CITYID, cityId)
                    .setField(AddressItemFields.DISTRICTID, districtId)
                    .setField(AddressItemFields.RECEIVERZIP, receiverZip)
                    .setField(AddressItemFields.RECEIVERCITYID, receiverCityId)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
