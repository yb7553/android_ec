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
import com.blankj.utilcode.util.LogUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.ec.pay.IAlPayResultListener;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleFields;
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

public class ShopCartDetailDelegate extends LatteDelegate implements View.OnClickListener, ISuccess, ICartItemListener, IAlPayResultListener {

    private ShopCartAdapter mAdapter = null;
    //购物车数量标记
    private int mCurrentCount = 0;
    private int mTotalCount = 0;
    private double mTotalPrice = 0.00;

    private RecyclerView mRecyclerView = null;
    private IconTextView mIconSelectAll = null;
    private ViewStubCompat mStubNoItem = null;
    private AppCompatTextView mTvTotalPrice = null;


    void onClickSelectAll() {
        final int tag = (int) mIconSelectAll.getTag();
        if (tag == 0) {
            final Context context = getContext();
            if (context != null) {
                mIconSelectAll.setTextColor
                        (ContextCompat.getColor(context, R.color.app_main));
            }
            mIconSelectAll.setTag(1);
            mAdapter.setIsSelectedAll(true);
            //更新RecyclerView的显示状态
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
        } else {
            mIconSelectAll.setTextColor(Color.GRAY);
            mIconSelectAll.setTag(0);
            mAdapter.setIsSelectedAll(false);
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
        }
    }

    /*删除数据*/
    void onClickRemoveSelectedItem() {
        final List<MultipleItemEntity> data = mAdapter.getData();
        if (null == data || data.size() == 0) {
            ToastUtil.showToast(getContext(), "无物品信息不能进行删除");
            return;
        }
        //要删除的数据
        final List<MultipleItemEntity> deleteEntities = new ArrayList<>();
        for (MultipleItemEntity entity : data) {
            final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelected) {
                deleteEntities.add(entity);
            }
        }
        //全量删除
        if (data.size() == deleteEntities.size()) {
            for (int i = deleteEntities.size() - 1; i >= 0; i--) {
                //此处进行移除服务器的数据
                deleteGoods(deleteEntities.get(i).getField(MultipleFields.ID));
                mAdapter.remove(deleteEntities.get(i).getField(ShopCartItemFields.POSITION));
                mCurrentCount = mAdapter.getItemCount();
                //更新数据
                mAdapter.notifyItemRangeChanged(deleteEntities.get(i).getField(ShopCartItemFields.POSITION), mAdapter.getItemCount());
            }
            //删除成功重新设置界面价格
            mAdapter.calTotalPrice(mAdapter.getData());
        } else {
            for (MultipleItemEntity entity : deleteEntities) {
                int removePosition;
                final int entityPosition = entity.getField(ShopCartItemFields.POSITION);
                if (entityPosition > mCurrentCount - 1) {
                    removePosition = entityPosition - (mTotalCount - mCurrentCount);
                } else {
                    removePosition = entityPosition;
                }
                if (removePosition <= mAdapter.getItemCount()) {
                    //此处进行移除服务器的数据
                    deleteGoods(mAdapter.getItem(removePosition).getField(MultipleFields.ID));
                    mAdapter.remove(removePosition);
                    mCurrentCount = mAdapter.getItemCount();
                    //删除成功重新设置界面价格
                    mAdapter.calTotalPrice(mAdapter.getData());
                    //更新数据
                    mAdapter.notifyItemRangeChanged(removePosition, mAdapter.getItemCount());
                }
            }
        }
        checkItemCount();
    }

    void onClickClear() {
        if (null == mAdapter || null == mAdapter.getData() || mAdapter.getData().size() == 0) {
            ToastUtil.showToast(getContext(), "无物品信息不能进行清空操作");
            return;
        }
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        checkItemCount();
    }

    //创建订单，注意，和支付是没有关系的
    private void createOrder() {
        //final String orderUrl = "https://dsn.apizza.net/mock/4fcf60b56ecb0411bd10c19d7ac3a009/v2/ecapi.cart.checkout";
        final String orderUrl = API.Config.getDomain() + API.CART_CHECKOUT;
        //final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        //加入你的参数
        RestClient.builder()
                .url(orderUrl)
                //.loader(getContext())
                //.params(orderParams)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        LatteLogger.d("ORDER", response);
                        // final int orderId = JSON.parseObject(response).getInteger("result");
                        final String data = JSON.parseObject(response).getString("data");
                        LatteLogger.d("DATA", data);
                        final int orderId = JSON.parseObject(data).getInteger("orderId");
                        final int userId = JSON.parseObject(data).getInteger("userId");
                        LatteLogger.d("orderId", orderId);
                        LatteLogger.d("userId", userId);
                        //
//                        FastPay.create(ShopCartDelegate.this)
//                                .setPayResultListener(ShopCartDelegate.this)
//                                .setOrderId(orderId)
//                                .beginPayDialog();
                    }
                })
                .build()
                .post();

    }


    @SuppressWarnings("RestrictedApi")
    private void checkItemCount() {
        final int count = mAdapter.getItemCount();
        if (count == 0) {
            final View stubView = mStubNoItem.inflate();
            final AppCompatTextView tvToBuy =
                    stubView.findViewById(R.id.tv_stub_to_buy);
            tvToBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "你该购物啦！", Toast.LENGTH_SHORT).show();
                    //切换到首页
                    final int indexTab = 0;
                    final EcBottomDelegate ecBottomDelegate = getParentDelegate();
                    final BottomItemDelegate indexDelegate = ecBottomDelegate.getItemDelegates().get(indexTab);
                    ecBottomDelegate
                            .getSupportDelegate()
                            .showHideFragment(indexDelegate, ShopCartDetailDelegate.this);
                    ecBottomDelegate.changeColor(indexTab);
                }
            });
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_shop_cart);
        mIconSelectAll = $(R.id.icon_shop_cart_select_all);
        mStubNoItem = $(R.id.stub_no_item);
        mTvTotalPrice = $(R.id.tv_shop_cart_total_price);
        mIconSelectAll.setTag(0);
        $(R.id.icon_shop_cart_select_all).setOnClickListener(this);
        $(R.id.tv_top_shop_cart_remove_selected).setOnClickListener(this);
        $(R.id.tv_top_shop_cart_clear).setOnClickListener(this);
        $(R.id.tv_shop_cart_pay).setOnClickListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final String shopcartUrl = API.Config.getDomain() + API.CART_GET;
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        LatteLogger.d("shopcart", shopcartUrl);
        final WeakHashMap<String, Object> shopcart = new WeakHashMap<>();
        shopcart.put("userId", mUserId);
        final String jsonString = JSON.toJSONString(shopcart);

        RestClient.builder()
                .url(shopcartUrl)
                .raw(jsonString)
                // .loader(getContext())
                .success(this)
                .build()
                .post();


    }

    @Override
    public void onSuccess(String response) {
        final ArrayList<MultipleItemEntity> data =
                new ShopCartDataConverter()
                        .setJsonData(response)
                        .convert();
        mAdapter = new ShopCartAdapter(data);
        mAdapter.setCartItemListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mTotalPrice = mAdapter.getTotalPrice();
        mTvTotalPrice.setText(String.valueOf(mTotalPrice));
        checkItemCount();
    }

    /*删除购物车商品*/
    private void deleteGoods(int goodsId) {
        final String shopcartUrl = API.Config.getDomain() + API.CART_DELETE;
        // final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        LatteLogger.d("shopcart", shopcartUrl);
        final WeakHashMap<String, Object> shopcart = new WeakHashMap<>();
        shopcart.put("goods_id", goodsId);
        final String jsonString = JSON.toJSONString(shopcart);
        LogUtils.e("jsonString", jsonString);
        RestClient.builder()
                .url(shopcartUrl)
                .loader(getContext())
                .raw(jsonString)
                // .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtils.e("response", response);
                        int code = JSON.parseObject(response).getInteger("code");
                        String msg = JSON.parseObject(response).getString("msg");
                        if (0 == code) {
                            // ToastUtil.showToast(getContext(), msg);
                        } else {
                            ToastUtil.showToast(getContext(), msg);
                        }
                    }
                })
                .build()
                .post();

    }

    @Override
    public void onItemClick(double itemTotalPrice) {
        final double price = mAdapter.getTotalPrice();
        mTvTotalPrice.setText(String.valueOf(price));
        /*//增加删减
        final String shopcartUrl = API.Config.getDomain() + API.CART_UPDATE;
        // final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        LatteLogger.d("shopcart", shopcartUrl);
        final WeakHashMap<String, Object> shopcart = new WeakHashMap<>();
        shopcart.put("good", id);
        shopcart.put("amount", goodsId);
        final String jsonString = JSON.toJSONString(shopcart);
        LogUtils.e("jsonString", jsonString);
        RestClient.builder()
                .url(shopcartUrl)
                .loader(getContext())
                .raw(jsonString)
                // .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtils.e("response", response);
                        int code = JSON.parseObject(response).getInteger("code");
                        String msg = JSON.parseObject(response).getString("msg");
                        if (0 == code) {
                            // ToastUtil.showToast(getContext(), msg);
                        } else {
                            ToastUtil.showToast(getContext(), msg);
                        }
                    }
                })
                .build()
                .post();*/
    }

    @Override
    public void onPaySuccess() {

    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {

    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public void onPayConnectError() {

    }

    private void onClickShopOrder() {
        ArrayList<MultipleItemEntity> data = (ArrayList<MultipleItemEntity>) mAdapter.getData();
        if (null == data) {
            ToastUtil.showToast(getContext(), "无物品信息不能结算");
            return;
        }
        //判断是否有库存

        boolean flag = true;
        //跳转详情
        String cart_good_id = "[";
        int selectCount = 0;
        for (MultipleItemEntity entity : data) {
            cart_good_id += entity.getField(ShopCartItemFields.GOODS_ID) + ",";
            boolean isSelect = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelect) ++selectCount;
            if (!(boolean) entity.getField(ShopCartItemFields.IS_ON_SALE) && isSelect) {
                ToastUtil.showToast(getContext(), "" + entity.getField(ShopCartItemFields.TITLE) + "下架了");
                flag = false;
                return;
            }
            if (!(boolean) entity.getField(ShopCartItemFields.IS_EXIST_GOODS) && isSelect) {
                ToastUtil.showToast(getContext(), "" + entity.getField(ShopCartItemFields.TITLE) + "暂无库存");
                flag = false;
                return;
            }
            if (!(boolean) entity.getField(ShopCartItemFields.IS_EXIST_ATTR) && isSelect) {
                ToastUtil.showToast(getContext(), "" + entity.getField(ShopCartItemFields.TITLE) + "暂无库存");
                flag = false;
                return;
            }
        }
        if (!flag) return;
        cart_good_id = cart_good_id.substring(0, cart_good_id.length() - 1) + "]";
        if (selectCount == 0) {
            ToastUtil.showToast(getContext(), "请选择需要购买的商品");
            return;
        }
        getSupportDelegate().start(new ShopOrderDelegate().create(cart_good_id, mTvTotalPrice.getText().toString().trim()));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.icon_shop_cart_select_all) {
            onClickSelectAll();
        } else if (i == R.id.tv_top_shop_cart_remove_selected) {
            onClickRemoveSelectedItem();
        } else if (i == R.id.tv_top_shop_cart_clear) {
            onClickClear();
        } else if (i == R.id.tv_shop_cart_pay) {
            //  createOrder();
            onClickShopOrder();
        }
    }
}
