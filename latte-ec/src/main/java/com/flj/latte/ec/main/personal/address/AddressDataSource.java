package com.flj.latte.ec.main.personal.address;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.flj.latte.bean.AddressBean;
import com.flj.latte.ec.MyTaxiApplication;
import com.flj.latte.factory.ThreadPoolFactory;
import com.flj.latte.net.wrapper.AddressWrapper;
import com.flj.latte.task.TaskCallback;
import com.flj.latte.task.TaskStatus;
import com.flj.latte.util.file.FileUtil;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: lmj
 * date  : 2017/12/22.
 */

public class AddressDataSource {

    public static final String CACHE_FILE_NAME = "address_cache.txt";
    private static volatile AddressDataSource instance;
    /**
     * 收货地址列表
     */
    private List<AddressWrapper.DataEntity> mReceiverAddresses;

    /**
     * 默认地址
     */
    private AddressWrapper.DataEntity mDefaultAddress;

    /**
     * 默认地址位置
     */
    private int addressPosition = 0;

    //    当前选择的地址id
    private int receiverAddressId;

    private ArrayList<AddressBean> provinces = new ArrayList<>();

    private ArrayList<AddressBean> carCitys = new ArrayList<>();

    private Map<String, ArrayList<AddressBean>> citys = new HashMap<>();
    private Map<String, ArrayList<AddressBean>> districts = new HashMap<>();


    private AddressDataSource() {
    }

    public static AddressDataSource getInstance() {
        if (instance == null) {
            if (instance == null) {
                instance = new AddressDataSource();
            }
        }
        return instance;
    }


    public void setReceiverAddresses(List<AddressWrapper.DataEntity> receiverAddresses) {
        mReceiverAddresses = receiverAddresses;
    }

    public void setReceiverAddressId(int receiverAddressId) {
        this.receiverAddressId = receiverAddressId;
    }

    public void setAddressPosition(int addressPosition) {
        this.addressPosition = addressPosition;
        mDefaultAddress = mReceiverAddresses.get(addressPosition);
    }

    public void updateDefaultAddress(int position) {
        mReceiverAddresses.get(addressPosition).receiverDefault = 0;
        mReceiverAddresses.get(position).receiverDefault = 1;
        mDefaultAddress = mReceiverAddresses.get(position);
        mReceiverAddresses.remove(position);
        mReceiverAddresses.add(0, mDefaultAddress);
        addressPosition = 0;
    }

    public void deleteAddress(int position) {
        mReceiverAddresses.remove(position);
    }

    public String getAddressId(int position) {
        int id = mReceiverAddresses.get(position).id;
        return String.valueOf(id);
    }

    public List<AddressWrapper.DataEntity> getReceiverAddresses() {
        return mReceiverAddresses;
    }

    public AddressWrapper.DataEntity getAddress(int position) {
        return mReceiverAddresses.get(position);
    }

    public int getReceiverAddressId() {
        return receiverAddressId;
    }

    public AddressWrapper.DataEntity getDefaultAddress() {
        return mDefaultAddress;
    }

    public void requestFZCity() {
        TaskStatus taskStatus = new TaskStatus() {
            @Override
            protected void execute() throws Exception {
//                AddressRequestBusiness requestBusiness = new AddressRequestBusiness();
//                String json = requestBusiness.requestFZCityList();
//                handleCallback(json);
            }
        };

        taskStatus.setCallback(new TaskCallback<String>() {
            @Override
            public void onSuccess(String data) {
                if (data != null) {
                    try {
                        carCitys.clear();
                        parseFZCity(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    loadLocalCity();
                }
            }

            @Override
            public void onFailed(Exception ex) {
                loadLocalCity();
            }
        });
        ThreadPoolFactory.getThreadPool().execute(taskStatus);
    }

    private void parseFZCity(String json) throws Exception {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        JSONArray citysArr = new JSONArray(json);
        if (citysArr.length() > 0) {
            for (int i = 0; i < citysArr.length(); i++) {
                AddressBean bean = new AddressBean();
                JSONObject city = citysArr.getJSONObject(i);
                if (city.has("codeid")) {
                    bean.id = city.optString("codeid");
                }
                if (city.has("cityname")) {
                    bean.name = city.optString("cityname");
                }
                carCitys.add(bean);
            }
        }
    }

    public void requestCity() {
        TaskStatus taskStatus = new TaskStatus() {
            @Override
            protected void execute() throws Exception {
//                AddressRequestBusiness requestBusiness = new AddressRequestBusiness();
//                String json = requestBusiness.requestCityList();
//                handleCallback(json);
            }
        };

        taskStatus.setCallback(new TaskCallback<String>() {
            @Override
            public void onSuccess(String data) {
                if (data != null) {
                    try {
                        FileUtil.save(MyTaxiApplication.getInstance(), CACHE_FILE_NAME, data);
                        provinces.clear();
                        citys.clear();
                        districts.clear();
                        parseCityJson(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    loadLocalCity();
                }
            }

            @Override
            public void onFailed(Exception ex) {
                loadLocalCity();
            }
        });
        ThreadPoolFactory.getThreadPool().execute(taskStatus);
    }

    private void loadLocalCity() {
        try {
            String json = FileUtil.read(MyTaxiApplication.getInstance(), CACHE_FILE_NAME);
            if (TextUtils.isEmpty(json)) {
                provinces.clear();
                citys.clear();
                districts.clear();
                parseCityJson(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseCityJson(String json) throws Exception {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        JSONArray provincesArr = new JSONArray(json);
        if (provincesArr.length() > 0) {
            for (int i = 0; i < provincesArr.length(); i++) {
                JSONObject province = provincesArr.getJSONObject(i);
                provinces.add(new AddressBean(province));
                if (province.has("citys") && province.has("id")) {
                    JSONArray citysArr = province.optJSONArray("citys");
                    ArrayList<AddressBean> cityList = new ArrayList<>();
                    for (int j = 0; j < citysArr.length(); j++) {
                        JSONObject city = citysArr.getJSONObject(j);
                        cityList.add(new AddressBean(city));
                        if (city.has("districts") && city.has("id")) {
                            JSONArray districtsArr = city.optJSONArray("districts");
                            ArrayList<AddressBean> districtList = new ArrayList<>();
                            for (int k = 0; k < districtsArr.length(); k++) {
                                districtList.add(new AddressBean(districtsArr.getJSONObject(k)));
                            }
                            districts.put(city.optString("id"), districtList);
                        }
                    }
                    citys.put(province.optString("id"), cityList);
                }
            }
        }
    }

    public ArrayList<AddressBean> getProvinces() {
        return provinces;
    }

    public Map<String, ArrayList<AddressBean>> getDistricts() {
        return districts;
    }

    public Map<String, ArrayList<AddressBean>> getCitys() {
        return citys;
    }

    public ArrayList<AddressBean> getCarCitys() {
        return carCitys;
    }
}
