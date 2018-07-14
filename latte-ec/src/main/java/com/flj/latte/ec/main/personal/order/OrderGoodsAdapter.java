package com.flj.latte.ec.main.personal.order;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flj.latte.ec.R;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe com.flj.latte.ec.main.personal.order.
 * @Notice
 * @Author Administrator.
 * @Date 2018/7/14 0014.
 */
public class OrderGoodsAdapter extends MultipleRecyclerAdapter {
    static final int SHOP_CART_ITEM = 8;

    protected OrderGoodsAdapter(List<MultipleItemEntity> data) {
        super(data);
        //防止用户初次注册，点击购物车崩溃的bug
        if (null == data) return;
        addItemType(SHOP_CART_ITEM, R.layout.item_shop_goods);
    }

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case SHOP_CART_ITEM:
                //先取出所有值
                final String goodsAttr = entity.getField(OrderItemFields.GOODSATTR);
                final String goodsName = entity.getField(OrderItemFields.GOODSNAME);
                final String goodsPic = entity.getField(OrderItemFields.GOODSPIC);
                final int goodsNum = entity.getField(OrderItemFields.GOODSNUM);
                final int goodsId = entity.getField(OrderItemFields.GOODSID);
                final double goodsPrice = entity.getField(OrderItemFields.GOODSPRICE);
                //取出所以控件
                final AppCompatImageView imgThumb = holder.getView(R.id.image_item_shop_cart);
                final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);
                final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);
                final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);

                //赋值
                tvTitle.setText(goodsName);
                tvDesc.setText(goodsAttr);
                tvPrice.setText(String.valueOf(goodsPrice));
                tvCount.setText("x "+String.valueOf(goodsNum));
                Glide.with(mContext)
                        .load(goodsPic)
                        .apply(OPTIONS)
                        .into(imgThumb);

                break;
            default:
                break;
        }
    }

}
