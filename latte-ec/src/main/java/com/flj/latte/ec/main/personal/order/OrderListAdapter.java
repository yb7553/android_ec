package com.flj.latte.ec.main.personal.order;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flj.latte.ec.R;
import com.flj.latte.ec.detail.GoodCommentImageAdapter;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
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
                final GridView gv_image = holder.getView(R.id.gv_image);
                final AppCompatTextView title = holder.getView(R.id.tv_order_list_title);
                final AppCompatTextView price = holder.getView(R.id.tv_order_list_price);
                //final AppCompatTextView time = holder.getView(R.id.tv_order_list_time);
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
                final IconTextView itv_total = holder.getView(R.id.itv_total);
                final AppCompatTextView tv_order_status = holder.getView(R.id.tv_order_status);


                final String titleVal = entity.getField(MultipleFields.TITLE);
                final long timeVal = entity.getField(OrderItemFields.TIME);
                final double priceVal = entity.getField(OrderItemFields.PRICE);
                final String lsOrderGoods = entity.getField(OrderItemFields.LSORDERGOODS);
                final int orderStatus = entity.getField(MultipleFields.TAG);
                final int orderId = entity.getField(MultipleFields.ID);
                String order_status= entity.getField(OrderItemFields.STATUSDESC);
                /*Glide.with(mContext)
                        .load(imageUrl)
                        .apply(OPTIONS)
                        .into(imageView);*/
                List<lsOrderGoodsBean> list = new Gson().fromJson(lsOrderGoods, new TypeToken<List<lsOrderGoodsBean>>() {
                }.getType());

                final List<String> images = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    images.add(list.get(i).getGoodspic());
                }
                //加载图片
                GoodCommentImageAdapter adapter = new GoodCommentImageAdapter(holder.getConvertView().getContext(), images);
                int size = images.size();
                int length = 100;
                DisplayMetrics dm = new DisplayMetrics();
                orderListDelegate.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                float density = dm.density;
                int gridviewWidth = (int) (size * (length + 4) * density);
                int itemWidth = (int) (length * density);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
                gv_image.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                gv_image.setColumnWidth(itemWidth); // 设置列表项宽
                gv_image.setHorizontalSpacing(5); // 设置列表项水平间距
                gv_image.setStretchMode(GridView.NO_STRETCH);
                gv_image.setNumColumns(size); // 设置列数量=列表集合数

                gv_image.setAdapter(adapter);

                itv_total.setText("共"+images.size()+"件"+"{fa-chevron-right}");
                title.setText("单号:" + titleVal);
                price.setText(" ¥ " + String.valueOf(priceVal));
               // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               // time.setText("时间:" + format.format(new Date(timeVal)));
                tv_order_status.setText(order_status);
                if(StringUtils.isEmpty(order_status)){
                    //评论部分处理
                    ll_comment.setVisibility(View.VISIBLE);
                    ll_pay.setVisibility(View.GONE);
                    delete.setOnClickListener(view -> {

                    });
                    comment.setOnClickListener(view -> {
                        orderListDelegate.getSupportDelegate().startForResult(new OrderCommentDelegate().
                                        create(orderId, 0, holder.getPosition()),
                                orderListDelegate.getResultCode());
                    });
                }else {
                    if (order_status.contains("付款")) {
                        //代付款
                        ll_pay.setVisibility(View.VISIBLE);
                        //取消订单
                        tv_cancel_order.setOnClickListener(view -> {

                        });
                        //去支付
                        tv_order_pay.setOnClickListener(view -> {


                        });
                    } else if (order_status.contains("发货")) {
                        //待发货
                        ll_send.setVisibility(View.VISIBLE);
                        //取消订单
                        tv_send_cancel.setOnClickListener(view -> {

                        });
                        //联系客服
                        tv_send_comm.setOnClickListener(view -> {


                        });

                    } else if (order_status.contains("收货")) {
                        //代收货
                        ll_confirm.setVisibility(View.VISIBLE);
                        //联系客服
                        tv_confirm_comm.setOnClickListener(view -> {

                        });
                        //确认收货
                        tv_confirm_order.setOnClickListener(view -> {


                        });


                    } else if (order_status.contains("评价")) {
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
                }
                break;
            default:
                break;
        }
    }
}
