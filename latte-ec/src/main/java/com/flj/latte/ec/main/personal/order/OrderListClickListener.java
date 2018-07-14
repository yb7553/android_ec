package com.flj.latte.ec.main.personal.order;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.flj.latte.ui.recycler.MultipleItemEntity;

/**
 * Created by yb
 */

public class OrderListClickListener extends SimpleClickListener {

    private final OrderListDelegate DELEGATE;

    public OrderListClickListener(OrderListDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleItemEntity entity = (MultipleItemEntity) baseQuickAdapter.getData().get(position);
       // final String titleVal = entity.getField(MultipleFields.TITLE);
       // final long timeVal = entity.getField(OrderItemFields.TIME);
        final double priceVal = entity.getField(OrderItemFields.PRICE);
        final String lsOrderGoods = entity.getField(OrderItemFields.LSORDERGOODS);
       // final int orderStatus = entity.getField(MultipleFields.TAG);
      //  final int orderId = entity.getField(MultipleFields.ID);
        String order_status = entity.getField(OrderItemFields.STATUSDESC);
        long send_time = entity.getField(OrderItemFields.SENDTIME);
        String address_address = entity.getField(OrderItemFields.ADDRESS);
        String address_phone = entity.getField(OrderItemFields.PHONE);
        String address_name = entity.getField(OrderItemFields.USERNAME);

        String send_mode= entity.getField(OrderItemFields.DELIVERNAME);
        /*if (orderStatus == 3) {
            DELEGATE.getSupportDelegate().start(new OrderCommentDelegate());
        }*/
        OrderDetailDelegate orderDetailDelegate = new OrderDetailDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("order_status", order_status);
        bundle.putString("status_tip", "订单有疑问？请联系客服进行处理");
        bundle.putString("address_name", address_name);
        bundle.putString("address_phone", address_phone);
        bundle.putString("send_mode", send_mode);
        bundle.putString("address_address", address_address);
        bundle.putLong("send_time", send_time);
        bundle.putDouble("goods_price", priceVal);
        bundle.putDouble("send_price", 0.00);
        bundle.putDouble("goods_off", 0.00);
        bundle.putDouble("order_total", priceVal);
        bundle.putString("lsOrderGoods", lsOrderGoods);

        orderDetailDelegate.setArguments(bundle);

        DELEGATE.getSupportDelegate().start(orderDetailDelegate);
        //ToastUtil.showToast(DELEGATE.getContext(),"点击了");
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
