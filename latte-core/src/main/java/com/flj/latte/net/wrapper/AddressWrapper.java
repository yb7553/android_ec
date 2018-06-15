package com.flj.latte.net.wrapper;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author: lmj
 * date  : 2017/12/21.
 */

public class AddressWrapper extends BaseWrapper {
    public List<DataEntity> dataList = new ArrayList<>();

    public AddressWrapper(String json) throws JSONException {
        if(TextUtils.isEmpty(json)){
            return;
        }
        JSONArray placesArray = new JSONArray(json);
        int len = placesArray.length();
        List<DataEntity> dataEntityList = new ArrayList<>();
        for (int index = 0; index < len; index++) {
            DataEntity valueEntity = new DataEntity(placesArray.getJSONObject(index));
            dataEntityList.add(valueEntity);
        }
        this.dataList = dataEntityList;
    }

    public static class DataEntity implements Serializable {

        public int id;
        //收货姓名
        public String receiverName;
        //收货固定电话
        public String receiverPhone;
        //收货移动电话
        public String receiverMobile;
        //省份id
        public String provinceId;
        //省份
        public String receiverProvince;
        //城市id
        public String cityId;
        //城市
        public String receiverCity;
        //区/县id
        public String districtId;
        //区/县
        public String receiverDistrict;
        //1为默认地址
        public int receiverDefault;
        //详细地址
        public String receiverAddress;
        //邮编
        public String receiverZip;
        //创建时间
        public String createTime;
        //拼接后的地址
        public String allAddress;

        public DataEntity() {
        }

        public DataEntity(JSONObject object) {
            if (object.has("id")) {
                id = object.optInt("id");
            }
            if (object.has("receiverName")) {
                receiverName = object.optString("receiverName");
            }
            if (object.has("receiverPhone")) {
                receiverPhone = object.optString("receiverPhone");
            }
            if (object.has("provinceId")) {
                provinceId = object.optString("provinceId");
            }
            if (object.has("receiverMobile")) {
                receiverMobile = object.optString("receiverMobile");
            }
            if (object.has("receiverProvince")) {
                receiverProvince = object.optString("receiverProvince");
            }
            if (object.has("receiverCity")) {
                receiverCity = object.optString("receiverCity");
            }
            if (object.has("receiverDistrict")) {
                receiverDistrict = object.optString("receiverDistrict");
            }
            if (object.has("cityId")) {
                cityId = object.optString("cityId");
            }
            if (object.has("districtId")) {
                districtId = object.optString("districtId");
            }
            if (object.has("receiverDefault")) {
                receiverDefault = object.optInt("receiverDefault");
            }
            if (object.has("receiverAddress")) {
                receiverAddress = object.optString("receiverAddress");
            }
            if (object.has("receiverZip")) {
                receiverZip = object.optString("receiverZip");
            }
            if (object.has("createTime")) {
                createTime = object.optString("createTime");
            }
            allAddress = receiverProvince + receiverCity + receiverDistrict + receiverAddress;
        }
    }
}
