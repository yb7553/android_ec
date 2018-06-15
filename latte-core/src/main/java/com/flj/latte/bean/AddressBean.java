package com.flj.latte.bean;



import org.json.JSONObject;

/**
 * author: lmj
 * date  : 2017/12/22.
 */

public class AddressBean implements UnProguard{
    public String id;
    public String name;

    public AddressBean() {
    }

    public AddressBean(JSONObject object) {
        if (object.has("id")) {
            id = object.optString("id");
        }
        if (object.has("name")) {
            name = object.optString("name");
        }
    }

    public String getPickerViewText(){
        return name;
    }
}
