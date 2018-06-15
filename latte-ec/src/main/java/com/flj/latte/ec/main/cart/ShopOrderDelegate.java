package com.flj.latte.ec.main.cart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.ec.main.personal.PersonalDelegate;
import com.flj.latte.ec.main.personal.order.OrderListAdapter;
import com.flj.latte.ec.main.personal.order.OrderListClickListener;
import com.flj.latte.ec.main.personal.order.OrderListDataConverter;
import com.flj.latte.ec.main.personal.order.OrderListDelegate;
import com.flj.latte.ec.main.personal.send.SendTypeDelegate;
import com.flj.latte.ec.pay.FastPay;
import com.flj.latte.ec.pay.IAlPayResultListener;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IError;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class ShopOrderDelegate extends LatteDelegate implements View.OnClickListener  {

    private String mOrderId = null;

    private RecyclerView mRecyclerView = null;

    private String actionUrl=null;
    private  String requestData=null;

    private String receiverAddress=null;

    private AppCompatTextView mTvShopOrderAddressName = null;




    public static final String HTML_TYPE = "HTML_TYPE";
    private Bundle mArgs = null;



    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_order;
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

        mTvShopOrderAddressName = $(R.id.tv_shop_order_address_name);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final String consigneeUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.consignee.choose";
        LatteLogger.d("consigneeUrl", consigneeUrl);
        final WeakHashMap<String, Object> consignee = new WeakHashMap<>();
        final Long mUserId= LattePreference.getCustomAppProfileLong("userId");
        consignee.put("userId",mUserId);
        final String jsonString = JSON.toJSONString(consignee);

        RestClient.builder()
                //.loader(getContext())
                .url(consigneeUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("consigneeUrl", response);
                        final String data = JSON.parseObject(response).getString("data");
                        LatteLogger.d("DATA", data);
                        //receiverAddress=JSON.parseObject(data).getString("receiverAddress");
                        mTvShopOrderAddressName.setText("收货人");

                        LatteLogger.d("receiverAddress", receiverAddress);
                    }
                }) .error(new IError() {
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
        final String orderUrl = "你的生成订单的API";
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        //加入你的参数
        RestClient.builder()
                .url(orderUrl)
                .loader(getContext())
                .params(orderParams)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        LatteLogger.d("ORDER", response);
                        final int orderId = JSON.parseObject(response).getInteger("result");
                        FastPay.create(ShopOrderDelegate.this)
                                //.setPayResultListener(ShopOrderDelegate.this)
                                .setOrderId(orderId)
                                .beginPayDialog();
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
        payOrSign.put("actionUrl",actionUrl);
        payOrSign.put("requestData",requestData);
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



    private void  onClickSendType(){
        getSupportDelegate().start(new SendTypeDelegate());
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_shop_order_pay) {
            //onClickSendType();
            payOrSign();
        } else if (i == R.id.tv_shop_order_send) {
            //onClickClear();
            //payOrSign();
            onClickSendType();




        }
   }

}
