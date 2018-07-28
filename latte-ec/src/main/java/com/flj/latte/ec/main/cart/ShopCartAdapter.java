package com.flj.latte.ec.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flj.latte.app.Latte;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.utils.Utils;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public final class ShopCartAdapter extends MultipleRecyclerAdapter {

    private boolean mIsSelectedAll = false;
    private ICartItemListener mCartItemListener = null;
    private double mTotalPrice = 0.00;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    ShopCartAdapter(List<MultipleItemEntity> data) {
        super(data);
        //防止用户初次注册，点击购物车崩溃的bug
        if (null == data) return;
        //初始化总价
        for (MultipleItemEntity entity : data) {
            boolean isSelect = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelect) {
                final double price = entity.getField(ShopCartItemFields.PRICE);
                final int count = entity.getField(ShopCartItemFields.COUNT);
                //Double类型加减乘除有精度的，不能用普通的加减乘除来计算
                final double total = Utils.mul(price, (double) count);
                mTotalPrice = Utils.add(mTotalPrice, total);
            }
        }
        //添加购物测item布局
        addItemType(ShopCartItemType.SHOP_CART_ITEM, R.layout.item_shop_cart);
    }

    public void setIsSelectedAll(boolean isSelectedAll) {
        this.mIsSelectedAll = isSelectedAll;
        mTotalPrice = 0.00;
        if (mIsSelectedAll) {
            //全选计算金额
            //初始化总价
            for (MultipleItemEntity entity : getData()) {
                final double price = entity.getField(ShopCartItemFields.PRICE);
                final int count = entity.getField(ShopCartItemFields.COUNT);
                //Double类型加减乘除有精度的，不能用普通的加减乘除来计算
                final double total = Utils.mul(price, (double) count);
                mTotalPrice = Utils.add(mTotalPrice, total);
            }
        }
        mCartItemListener.onItemClick(mTotalPrice);
    }

    public void setCartItemListener(ICartItemListener listener) {
        this.mCartItemListener = listener;
    }

    public double getTotalPrice() {
        return mTotalPrice;
    }

    public double calTotalPrice(List<MultipleItemEntity> data) {
        mTotalPrice = 0.00;
        //初始化总价
        for (MultipleItemEntity entity : data) {
            boolean isSelect = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelect) {
                final double price = entity.getField(ShopCartItemFields.PRICE);
                final int count = entity.getField(ShopCartItemFields.COUNT);
                //Double类型加减乘除有精度的，不能用普通的加减乘除来计算
                final double total = Utils.mul(price, (double) count);
                mTotalPrice = Utils.add(mTotalPrice, total);
            }
        }
        mCartItemListener.onItemClick(mTotalPrice);
        return mTotalPrice;
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case ShopCartItemType.SHOP_CART_ITEM:
                //先取出所有值
                final int id = entity.getField(MultipleFields.ID);
                final String thumb = entity.getField(MultipleFields.IMAGE_URL);
                final String title = entity.getField(ShopCartItemFields.TITLE);
                final String desc = entity.getField(ShopCartItemFields.DESC);
                final int count = entity.getField(ShopCartItemFields.COUNT);
                final double price = entity.getField(ShopCartItemFields.PRICE);
                //取出所以控件
                final AppCompatImageView imgThumb = holder.getView(R.id.image_item_shop_cart);
                final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);
                final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);
                final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);
                final IconTextView iconMinus = holder.getView(R.id.icon_item_minus);
                final IconTextView iconPlus = holder.getView(R.id.icon_item_plus);
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);
                final IconTextView iconIsSelected = holder.getView(R.id.icon_item_shop_cart);

                //赋值
                tvTitle.setText(title);
                tvDesc.setText(desc);
                tvPrice.setText(String.valueOf(price));
                tvCount.setText(String.valueOf(count));
                Glide.with(mContext)
                        .load(thumb)
                        .apply(OPTIONS)
                        .into(imgThumb);

                //在左侧勾勾渲染之前改变全选与否状态
                entity.setField(ShopCartItemFields.IS_SELECTED, mIsSelectedAll);
                final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
                //根据数据状态显示左侧勾勾
                if (isSelected) {
                    iconIsSelected.setTextColor
                            (ContextCompat.getColor(Latte.getApplicationContext(), R.color.app_main));
                } else {
                    iconIsSelected.setTextColor(Color.GRAY);
                }
                //添加左侧勾勾点击事件
                iconIsSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final boolean currentSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
                        if (currentSelected) {
                            iconIsSelected.setTextColor(Color.GRAY);
                            entity.setField(ShopCartItemFields.IS_SELECTED, false);
                        } else {
                            iconIsSelected.setTextColor
                                    (ContextCompat.getColor(Latte.getApplicationContext(), R.color.app_main));
                            entity.setField(ShopCartItemFields.IS_SELECTED, true);
                        }
                        calTotalPrice(getData());
                    }
                });
                //添加加减事件
                iconMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentCount = entity.getField(ShopCartItemFields.COUNT);
                        if (Integer.parseInt(tvCount.getText().toString()) > 1) {
                            final String shopcartUrl = API.Config.getDomain() + API.CART_UPDATE;
                            final WeakHashMap<String, Object> shopcart = new WeakHashMap<>();
                            shopcart.put("good", id);
                            currentCount--;
                            shopcart.put("amount", currentCount);
                            final String jsonString = JSON.toJSONString(shopcart);
                            RestClient.builder()
                                    .url(shopcartUrl)
                                    .loader(mContext)
                                    .raw(jsonString)
                                    .success(new ISuccess() {
                                        @Override
                                        public void onSuccess(String response) {
                                            LogUtils.e("response", response);
                                            int countNum = Integer.parseInt(tvCount.getText().toString());
                                            countNum--;
                                            tvCount.setText(String.valueOf(countNum));
                                            if (mCartItemListener != null) {
                                                final boolean currentSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
                                                if (currentSelected) {
                                                    mTotalPrice = Utils.subtract(mTotalPrice, price);
                                                    final double itemTotal = Utils.mul(countNum, price);
                                                    mCartItemListener.onItemClick(itemTotal);
                                                }
                                            }
                                        }
                                    })
                                    .build()
                                    .post();
                        }
                    }
                });

                iconPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentCount = entity.getField(ShopCartItemFields.COUNT);
                        final String shopcartUrl = API.Config.getDomain() + API.CART_UPDATE;
                        final WeakHashMap<String, Object> shopcart = new WeakHashMap<>();
                        shopcart.put("good", id);
                        currentCount++;
                        shopcart.put("amount", currentCount);
                        final String jsonString = JSON.toJSONString(shopcart);
                        RestClient.builder()
                                .url(shopcartUrl)
                                .loader(mContext)
                                .raw(jsonString)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LogUtils.e("response", response);
                                        int countNum = Integer.parseInt(tvCount.getText().toString());
                                        countNum++;
                                        tvCount.setText(String.valueOf(countNum));
                                        if (mCartItemListener != null) {
                                            final boolean currentSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
                                            if (currentSelected) {
                                                mTotalPrice = Utils.add(mTotalPrice, price);
                                                final double itemTotal = Utils.mul(countNum, price);
                                                mCartItemListener.onItemClick(itemTotal);
                                            }
                                        }
                                    }
                                })
                                .build()
                                .post();
                    }
                });

                break;
            default:
                break;
        }
    }
}
