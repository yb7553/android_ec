package com.flj.latte.ec.main.personal.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.utils.Utils;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe 订单详情
 * @Notice
 * @Author Administrator.
 * @Date 2018/7/14 0014.
 */
public class OrderDetailDelegate extends LatteDelegate implements View.OnClickListener {
    private AppCompatTextView order_status, status_tip, address_name, address_phone, address_address, send_mode, send_time, goods_price, send_price, goods_off, order_total;
    private String str_order_status;
    private String str_address_name;
    private String str_status_tip;
    private String str_address_phone;
    private long int_send_time;
    private String str_address_address;
    private String str_send_mode;
    private double str_goods_price;
    private double double_send_price;
    private double double_goods_off;
    private double double_order_total;
    private String str_lsOrderGoods;
    private RecyclerView rv_goods_list;
    private OrderGoodsAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        $(R.id.icon_back).setOnClickListener(this);
        order_status = $(R.id.tv_order_status);
        status_tip = $(R.id.tv_status_tip);
        address_name = $(R.id.tv_address_name);
        address_phone = $(R.id.tv_address_phone);
        send_mode = $(R.id.tv_send_mode);
        address_address = $(R.id.tv_address_address);
        send_time = $(R.id.tv_send_time);
        goods_price = $(R.id.tv_goods_price);
        send_price = $(R.id.tv_send_price);
        goods_off = $(R.id.tv_goods_off);
        order_total = $(R.id.tv_order_total);

        final Bundle args = getArguments();
        if (args != null) {
            // mType = args.getString(PersonalDelegate.ORDER_TYPE);
            str_order_status = args.getString("order_status");
            str_status_tip = args.getString("status_tip");
            str_address_name = args.getString("address_name");
            str_address_phone = args.getString("address_phone");
            str_send_mode = args.getString("send_mode");
            str_address_address = args.getString("address_address");
            int_send_time = args.getLong("send_time");
            str_goods_price = args.getDouble("goods_price");
            double_send_price = args.getDouble("send_price");
            double_goods_off = args.getDouble("goods_off");
            double_order_total = args.getDouble("order_total");
            str_lsOrderGoods = args.getString("lsOrderGoods");
        }
        order_status.setText(str_order_status == null ? "待评价" : str_order_status);
        status_tip.setText(str_status_tip);
        address_name.append(str_address_name);
        address_phone.setText(Utils.getStringForX(str_address_phone,3,4));
        send_mode.append(StringUtils.isEmpty(str_send_mode) ? "随机" : (str_send_mode + " 配送"));
        send_time.append(int_send_time == 0 ? "立即配送" : "");
        address_address.append(str_address_address);
        goods_price.append("" + str_goods_price);
        send_price.append("" + double_send_price);
        goods_off.append("" + double_goods_off);
        order_total.append("" + double_order_total);
        //显示商品详情:
        rv_goods_list=$(R.id.rv_goods_list);
       // rv_goods_list
        final ArrayList<MultipleItemEntity> data =
                new GoodsDataConverter()
                        .setJsonData(str_lsOrderGoods)
                        .convert();
        mAdapter = new OrderGoodsAdapter(data);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_goods_list.setLayoutManager(manager);
        rv_goods_list.setAdapter(mAdapter);
        //----------------------------------
        LinearLayout ll_pay = $(R.id.ll_pay);
        AppCompatTextView tv_cancel_order = $(R.id.tv_cancel_order);
        AppCompatTextView tv_order_pay = $(R.id.tv_order_pay);
        LinearLayout ll_send = $(R.id.ll_send);
        AppCompatTextView tv_send_cancel = $(R.id.tv_send_cancel);
        AppCompatTextView tv_send_comm = $(R.id.tv_send_comm);
        LinearLayout ll_confirm = $(R.id.ll_confirm);
        AppCompatTextView tv_confirm_comm = $(R.id.tv_confirm_comm);
        AppCompatTextView tv_confirm_order = $(R.id.tv_confirm_order);
        AppCompatTextView delete = $(R.id.tv_order_delete);
        AppCompatTextView comment = $(R.id.tv_order_comment);
        LinearLayout ll_comment = $(R.id.ll_comment);
        String status = order_status.getText().toString();
        if (status.contains("付款")) {
            //代付款
            ll_pay.setVisibility(View.VISIBLE);
            //取消订单
            tv_cancel_order.setOnClickListener(view -> {

            });
            //去支付
            tv_order_pay.setOnClickListener(view -> {


            });
        } else if (status.contains("发货")) {
            //待发货
            ll_send.setVisibility(View.VISIBLE);
            //取消订单
            tv_send_cancel.setOnClickListener(view -> {

            });
            //联系客服
            tv_send_comm.setOnClickListener(view -> {


            });

        } else if (status.contains("收货")) {
            //代收货
            ll_confirm.setVisibility(View.VISIBLE);
            //联系客服
            tv_confirm_comm.setOnClickListener(view -> {

            });
            //确认收货
            tv_confirm_order.setOnClickListener(view -> {


            });


        } else if (status.contains("评价")) {
            //评论部分处理
            ll_comment.setVisibility(View.VISIBLE);
            delete.setOnClickListener(view -> {

            });
            comment.setOnClickListener(view -> {

            });
        }

       // ScrollView sView = $(R.id.scrollView);
        //sView.setVerticalScrollBarEnabled(false); //禁用垂直滚动
       // sView.setHorizontalScrollBarEnabled(false); //禁用水平滚动
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.icon_back == id) {
            getSupportDelegate().pop();
        }
    }
}
