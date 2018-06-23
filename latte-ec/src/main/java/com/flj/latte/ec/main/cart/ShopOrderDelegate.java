package com.flj.latte.ec.main.cart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.ec.main.personal.address.AddressDataConverter;
import com.flj.latte.ec.main.personal.address.AddressItemFields;
import com.flj.latte.ec.main.personal.address.SelectAddressDelegate;
import com.flj.latte.ec.main.personal.send.SendDataConverter;
import com.flj.latte.ec.main.personal.send.SendTypeDelegate;
import com.flj.latte.ec.pay.FastPay;
import com.flj.latte.ec.utils.Utils;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IError;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class ShopOrderDelegate extends LatteDelegate implements View.OnClickListener {
    private String mOrderId = null;
    private RecyclerView mRecyclerView = null;
    private String actionUrl = null;
    private String requestData = null;
    private String receiverAddress = null;
    public static final String HTML_TYPE = "HTML_TYPE";
    private Bundle mArgs = null;
    private int AddressCallBackCode = 300;//地址选择器回调码
    private int TimeCallBackCode = 301;//时间选择器回调码

    private int addressId = 0;
    private String addressName = "";
    private String addressLocation = "";
    private String phone = "";
    private int cictyid;
    private AppCompatTextView tv_address_name, tv_address_phone, tv_shop_order_address,
            tv_shop_order_send_staff, tv_sendmode, tv_shop_cart_total_price;
    private String cart_good_id;
    private String totalAmount;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_order;
    }

    public ShopOrderDelegate create(String cart_good_id, String amout) {
        this.cart_good_id = cart_good_id;
        this.totalAmount=amout;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_shop_order);
        $(R.id.tv_shop_order_send).setOnClickListener(this);
        $(R.id.tv_shop_order_pay).setOnClickListener(this);
        $(R.id.llc_recevice_personal).setOnClickListener(this);
        // RelativeLayout rl_title = $(R.id.rl_title);
        //设置标题栏的颜色
        // rl_title.setBackgroundColor(Color.parseColor("#ff9999"));
        tv_address_name = $(R.id.tv_shop_order_address_name);
        tv_address_phone = $(R.id.tv_shop_order_address_phone);
        tv_shop_order_address = $(R.id.tv_shop_order_address);
        tv_shop_order_send_staff = $(R.id.tv_shop_order_send_staff);
        tv_sendmode = $(R.id.tv_sendmode);
        tv_shop_cart_total_price = $(R.id.tv_shop_cart_total_price);
        //同步金额
        tv_shop_cart_total_price.setText(totalAmount);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final String consigneeUrl = API.Config.getDomain() + API.CONFIGNEE_CHOOSE;
        LatteLogger.d("consigneeUrl", consigneeUrl);
        final WeakHashMap<String, Object> consignee = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        consignee.put("userId", mUserId);
        final String jsonString = JSON.toJSONString(consignee);
        RestClient.builder()
                //.loader(getContext())
                .url(consigneeUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("consigneeUrl", response);
                        LogUtils.e("consigneeUrl", response);
                        final List<MultipleItemEntity> data =
                                new AddressDataConverter().setJsonData(response).convert();
                        LatteLogger.d("DATA", data);
                        //receiverAddress=JSON.parseObject(data).getString("receiverAddress");
                        if (null == data) return;
                        for (MultipleItemEntity entity : data) {
                            //默认选择默认地址
                            if ((Boolean) entity.getField(MultipleFields.TAG)) {
                                addressId = entity.getField(MultipleFields.ID);
                                addressName = entity.getField(MultipleFields.NAME);
                                phone = entity.getField(AddressItemFields.PHONE);
                                String address = entity.getField(AddressItemFields.ADDRESS);
                                String location = entity.getField(AddressItemFields.LOCATION);
                                addressLocation = location + address;
                                cictyid = entity.getField(AddressItemFields.RECEIVERCITYID);
                            }
                        }
                        //没有默认地址默认选择第一个
                        if (StringUtils.isEmpty(addressName) && StringUtils.isEmpty(phone)
                                && null != data) {
                            addressId = data.get(0).getField(MultipleFields.ID);
                            addressName = data.get(0).getField(MultipleFields.NAME);
                            phone = data.get(0).getField(AddressItemFields.PHONE);
                            String address = data.get(0).getField(AddressItemFields.ADDRESS);
                            String location = data.get(0).getField(AddressItemFields.LOCATION);
                            addressLocation = location + address;
                            cictyid = data.get(0).getField(AddressItemFields.RECEIVERCITYID);
                        }
                        updateAddress();
                        initSendData();
                        LatteLogger.d("receiverAddress", receiverAddress);
                    }
                }).error(new IError() {
            @Override
            public void onError(int code, String msg) {
                LatteLogger.d("code", code);
                LatteLogger.d("msg", msg);
            }
        })
                .build()
                .post();
    }

    //创建订单，注意，和支付是没有关系的
    private void createOrder() {
        //final String orderUrl = "https://dsn.apizza.net/mock/4fcf60b56ecb0411bd10c19d7ac3a009/v2/ecapi.cart.checkout";
        final String orderUrl = API.Config.getDomain() + API.CART_CHECKOUT;
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        orderParams.put("userId", mUserId);
        orderParams.put("senderId", sendstaff);
        orderParams.put("sendType", endtime == 0 ? 0 : 1);
        orderParams.put("sendTime", endtime == 0 ? System.currentTimeMillis() : endtime);
        orderParams.put("cart_good_id", cart_good_id);
        orderParams.put("consignee", addressId);

        final String jsonString = JSON.toJSONString(orderParams);
        LogUtils.e("jsonString", jsonString);
        //加入你的参数
        RestClient.builder()
                .url(orderUrl)
                //.loader(getContext())
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        LatteLogger.d("ORDER", response);
                        LogUtils.e("ORDER", response);
                        int code = JSON.parseObject(response).getInteger("code");
                        String msg = JSON.parseObject(response).getString("msg");
                        if (0 == code) {
                            int orderId = JSON.parseObject(response).getJSONObject("data").getInteger("orderId");
                            postSendMsg(orderId);
                            FastPay.create(ShopOrderDelegate.this)
                                    //.setPayResultListener(ShopOrderDelegate.this)
                                    .setOrderId(orderId)
                                    .beginPayDialog();
                        } else {
                            ToastUtil.showToast(getContext(), msg);
                        }
                    }
                })
                .build()
                .post();


    }


    //选择支付方式
    private void payOrSign() {
        FastPay.create(ShopOrderDelegate.this)
                // .setPayResultListener(this)
                .setOrderId(4)
                .beginPayDialog();
//        final String payOrSignUrl = "http://120.79.230.229/bfwl-mall/calmdown/payOrSign";
//        LatteLogger.d("payOrSignUrl", payOrSignUrl);
//        final WeakHashMap<String, Object> payOrSign = new WeakHashMap<>();
//        payOrSign.put("userId",1);
//        payOrSign.put("orderId",4);
//        final String jsonString = JSON.toJSONString(payOrSign);
//
//        //加入你的参数
//        RestClient.builder()
//                .url(payOrSignUrl)
//                //.params(payOrSign)
//                .raw(jsonString)
//                //.raw("{\"userId\":1,\"orderId\":4}")
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        //进行具体的支付
//                        LatteLogger.d("ORDER", response);
//                        final String data = JSON.parseObject(response).getString("data");
//                        LatteLogger.d("DATA", data);
//                        actionUrl = JSON.parseObject(data).getString("actionUrl");
//                        requestData = JSON.parseObject(data).getString("requestData");
//                        LatteLogger.d("actionUrl", actionUrl);
//                        LatteLogger.d("requestData", requestData);
//                        //payOrSignUrl();
//                        FastPay.create(ShopOrderDelegate.this)
//                                .setPayResultListener(ShopOrderDelegate.this)
//                                .setOrderId(4)
//                                .beginPayDialog();
//                    }
//                })
//                .error(new IError() {
//                    @Override
//                    public void onError(int code, String msg) {
//                        LatteLogger.d("code", code);
//                        LatteLogger.d("msg", msg);
//                    }
//                })
//                .build()
//                .post();

    }

    //支付/签约接口
    private void payOrSignUrl() {
        final String payOrSignUrlUrl = API.Config.getDomain() + API.PAY_OR_SIGNURL;
        LatteLogger.d("payOrSignUrlUrl", payOrSignUrlUrl);
        final WeakHashMap<String, Object> payOrSign = new WeakHashMap<>();
        payOrSign.put("actionUrl", actionUrl);
        payOrSign.put("requestData", requestData);
        final String jsonString = JSON.toJSONString(payOrSign);

        //加入你的参数
        RestClient.builder()
                .url(payOrSignUrlUrl)
                //.params(payOrSign)
                .raw(jsonString)
                //.raw("{\"userId\":1,\"orderId\":4}")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        LatteLogger.d("ORDER", response);

                        mArgs.putString(HTML_TYPE, response);

                        //mArgs.putString("mHtml", "test");

                        final OrderPayDelegate delegate = new OrderPayDelegate();
                        delegate.setArguments(mArgs);
                        getSupportDelegate().start(delegate);

                    }
                })
                .build()
                .post();

    }


    private void onClickSendType() {
        getSupportDelegate().startForResult(new SendTypeDelegate().create(senddata), TimeCallBackCode);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_shop_order_pay) {
            //onClickSendType();
            createOrder();
            //payOrSign();
        } else if (i == R.id.tv_shop_order_send) {
            //onClickClear();
            //payOrSign();
            onClickSendType();
        } else if (i == R.id.llc_recevice_personal) {
            getSupportDelegate().startForResult(new SelectAddressDelegate(), AddressCallBackCode);
        }
    }

    /*上传地址*/
    private void postSendMsg(int orderId) {
        //final String orderUrl = "https://dsn.apizza.net/mock/4fcf60b56ecb0411bd10c19d7ac3a009/v2/ecapi.cart.checkout";
        final String orderUrl = API.Config.getDomain() + API.SEND_TYPE_UPDATE;
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        //orderParams.put("userId", mUserId);
        orderParams.put("shopid", orderId);
        orderParams.put("sendstaff", sendstaff);
        orderParams.put("startstime", System.currentTimeMillis());
        orderParams.put("endtime", endtime == 0 ? System.currentTimeMillis() : endtime);
        final String jsonString = JSON.toJSONString(orderParams);
        LogUtils.e("jsonString", jsonString);
        //加入你的参数
        RestClient.builder()
                .url(orderUrl)
                //.loader(getContext())
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        LatteLogger.d("ORDER", response);
                        LogUtils.e("sendMode", response);
                        int code = JSON.parseObject(response).getInteger("code");
                        String msg = JSON.parseObject(response).getString("msg");
                        if (0 == code) {

                        } else {
                            ToastUtil.showToast(getContext(), msg);
                        }
                    }
                })
                .build()
                .post();

    }

    /*选择地址，配送方式的回调*/
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode && requestCode == AddressCallBackCode) {
            addressId = data.getInt("id");
            addressName = data.getString("name");
            phone = data.getString("phone");
            addressLocation = data.getString("address");
            //并且更新界面
            updateAddress();
        } else if (Activity.RESULT_OK == resultCode && requestCode == TimeCallBackCode) {
            sendstaff = data.getString("sendstaff");
            sendname = data.getString("sendname");
            selecttime = data.getString("selecttime");
            endtime = data.getLong("endtime");
            updateSendMode();
        }
    }

    //更新地址信息ui
    private void updateAddress() {
        tv_address_name.setText(addressName);
        tv_address_phone.setText(Utils.getStringForX(phone, 3, 4));
        tv_shop_order_address.setText(addressLocation);
    }

    private String sendstaff;//配送员id
    private long endtime;//配送截止时间
    private String sendname;//快递员名称
    private String selecttime;//配送时间

    //设置配送方式ui
    private void updateSendMode() {
        tv_shop_order_send_staff.setText(sendname);
        if (!StringUtils.isEmpty(selecttime))
            tv_sendmode.setText(selecttime);
    }

    List<MultipleItemEntity> senddata;

    //获取默认配送员信息
    private void initSendData() {
        final String consigneeUrl = API.Config.getDomain() + API.CONFIGNEE_SEND_LIST;
        LatteLogger.d("consigneeUrl", consigneeUrl);
        final WeakHashMap<String, Object> consignee = new WeakHashMap<>();
        //TODO:测试city写死
        cictyid = 150;
        consignee.put("city", cictyid);
        final String jsonString = JSON.toJSONString(consignee);
        LogUtils.e("sa", jsonString);
        RestClient.builder()
                //.loader(getContext())
                .url(consigneeUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("consigneeUrl", response);
                        LogUtils.e("sa", response);
                        senddata = new SendDataConverter().setJsonData(response).convert();
                        for (MultipleItemEntity entity : senddata) {
                            if ((Boolean) entity.getField(MultipleFields.TAG)) {
                                sendstaff = entity.getField(MultipleFields.ID);
                                sendname = entity.getField(MultipleFields.NAME);
                                updateSendMode();
                                return;
                            }
                        }
                    }
                }).error(new IError() {
            @Override
            public void onError(int code, String msg) {
                LatteLogger.d("code", code);
                LatteLogger.d("msg", msg);
            }
        })
                .build()
                .post();
    }
}
