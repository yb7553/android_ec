package com.flj.latte.ec.main.personal.order;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flj.latte.ec.R;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;

import java.util.Date;
import java.util.List;

/**
 * Created by yb
 */

public class OrderListAdapter extends MultipleRecyclerAdapter {
    OrderListDelegate orderListDelegate;
    int status = -1;
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    protected OrderListAdapter(OrderListDelegate orderListDelegate, List<MultipleItemEntity> data, int finalStatus) {
        super(data);
        this.orderListDelegate = orderListDelegate;
        this.status = finalStatus;
        addItemType(OrderListItemType.ITEM_ORDER_LIST, R.layout.item_order_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case OrderListItemType.ITEM_ORDER_LIST:
                final AppCompatImageView imageView = holder.getView(R.id.image_order_list);
                final AppCompatTextView title = holder.getView(R.id.tv_order_list_title);
                final AppCompatTextView price = holder.getView(R.id.tv_order_list_price);
                final AppCompatTextView time = holder.getView(R.id.tv_order_list_time);
                final AppCompatTextView delete = holder.getView(R.id.tv_order_delete);
                final AppCompatTextView comment = holder.getView(R.id.tv_order_comment);
                final LinearLayout ll_comment = holder.getView(R.id.ll_comment);
                final LinearLayout ll_pay = holder.getView(R.id.ll_pay);
                final AppCompatTextView tv_cancel_order = holder.getView(R.id.tv_cancel_order);
                final AppCompatTextView tv_order_pay = holder.getView(R.id.tv_order_pay);
                final LinearLayout ll_send = holder.getView(R.id.ll_send);
                final AppCompatTextView tv_send_cancel = holder.getView(R.id.tv_send_cancel);
                final AppCompatTextView tv_send_comm = holder.getView(R.id.tv_send_comm);
                final LinearLayout ll_confirm = holder.getView(R.id.ll_confirm);
                final AppCompatTextView tv_confirm_comm = holder.getView(R.id.tv_confirm_comm);
                final AppCompatTextView tv_confirm_order = holder.getView(R.id.tv_confirm_order);


                final String titleVal = entity.getField(MultipleFields.TITLE);
                final long timeVal = entity.getField(OrderItemFields.TIME);
                final double priceVal = entity.getField(OrderItemFields.PRICE);
                final String imageUrl = entity.getField(MultipleFields.IMAGE_URL);
                final int orderStatus = entity.getField(MultipleFields.TAG);
                final int orderId = entity.getField(MultipleFields.ID);
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(OPTIONS)
                        .into(imageView);

                title.setText("单号:" + titleVal);

                price.setText("价格:" + String.valueOf(priceVal));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                time.setText("时间:" + format.format(new Date(timeVal)));
                if (0 == status) {
                    //代付款
                    ll_pay.setVisibility(View.VISIBLE);
                    //取消订单
                    tv_cancel_order.setOnClickListener(view -> {

                    });
                    //去支付
                    tv_order_pay.setOnClickListener(view -> {


                    });
                } else if (1 == status) {
                    //待发货
                    ll_send.setVisibility(View.VISIBLE);
                    //取消订单
                    tv_send_cancel.setOnClickListener(view -> {

                    });
                    //联系客服
                    tv_send_comm.setOnClickListener(view -> {


                    });

                } else if (2 == status) {
                    //代收货
                    ll_confirm.setVisibility(View.VISIBLE);
                    //联系客服
                    tv_confirm_comm.setOnClickListener(view -> {

                    });
                    //确认收货
                    tv_confirm_order.setOnClickListener(view -> {


                    });


                } else if (3 == status) {
                    //评论部分处理
                    ll_comment.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(view -> {

                    });
                    comment.setOnClickListener(view -> {
                        orderListDelegate.getSupportDelegate().startForResult(new OrderCommentDelegate().
                                        create(orderId, 0, holder.getPosition()),
                                orderListDelegate.getResultCode());
                    });
                }
                break;
            default:
                break;
        }
    }
}
