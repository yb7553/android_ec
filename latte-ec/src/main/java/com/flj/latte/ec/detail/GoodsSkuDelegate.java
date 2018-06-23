package com.flj.latte.ec.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.ec.main.cart.ShopCartDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IFailure;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppingselect.BigClassification;
import com.shoppingselect.OnSelectedListener;
import com.shoppingselect.ShoppingSelectView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import ren.qinc.numberbutton.NumberButton;


/**
 * Created by yb
 */


@SuppressLint("ValidFragment")
public class GoodsSkuDelegate extends LatteDelegate implements OnSelectedListener, View.OnClickListener {
    private Activity mActivity = null;
    private AlertDialog mDialog = null;
    private int mGoodsID = -1;
    private JSONObject mGoodsDetail = null;
    private NumberButton mSkuCountBtn;
    private LinearLayout mSkuAtts;//商品属性列表
    private ShoppingSelectView shoppingselectview;
    private TextView priceView;
    private double baseAmout = 0.00;//基础价格
    private double totalAmout = 0.00;//总价格
    private String attsStr = "";//属性集合，* 号分隔

    private GoodsSkuDelegate(LatteDelegate delegate) {
        this.mActivity = delegate.getProxyActivity();
        this.mDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }

    public static GoodsSkuDelegate create(LatteDelegate delegate) {
        return new GoodsSkuDelegate(delegate);
    }
     Window window;
    public void beginSkuDialog() {
        mDialog.show();
        window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_sku_pop);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);

            Button btn_dialog_sku_cart = window.findViewById(R.id.btn_dialog_sku_cart);
            btn_dialog_sku_cart.setOnClickListener(this);
            btn_dialog_sku_cart.setBackgroundColor(Color.parseColor("#ff9999"));
            mSkuAtts = window.findViewById(R.id.mSkuAtrr);
            shoppingselectview = window.findViewById(R.id.shoppingselectview);

            //window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this);
            //window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this);

            final ImageView imageView = window.findViewById(R.id.mGoodsIconIv);
            final TextView textView = window.findViewById(R.id.mGoodsCodeTv);
            priceView = window.findViewById(R.id.mGoodsPriceTv);
            final ImageView mCloseIv = window.findViewById(R.id.mCloseIv);
            mCloseIv.setOnClickListener(this);
            mSkuCountBtn = window.findViewById(R.id.mSkuCountBtn);
            mSkuCountBtn.setCurrentNumber(1);
            textView.setText(this.mGoodsDetail.getString("name"));
            baseAmout = this.mGoodsDetail.getDouble("price");
            setSkuAtts();
            priceView.setText(Double.toString(totalAmout));
            //final Uri uri=Uri.parse(this.mGoodsDetail.getJSONObject("default_photo").getString("large"));
            Glide.with(this.mDialog.getContext())
                    .load(this.mGoodsDetail.getJSONObject("default_photo").getString("large"))
                    .into(imageView);


        }
    }

    List<BigClassification> attslist;//商品规格列表

    private void setSkuAtts() {
        if (null == mGoodsDetail || (null == mGoodsDetail.getString("properties"))) {
            mSkuAtts.setVisibility(View.GONE);
            return;
        }
        String attsJson = mGoodsDetail.getString("properties");
        Type type = new TypeToken<ArrayList<BigClassification>>() {
        }.getType();
        attslist = new Gson().fromJson(attsJson, type);
        //设置监听需要在设置数据之前
        shoppingselectview.setOnSelectedListener(this);
        shoppingselectview.setData(attslist);
        //TODO:测试金额累加，id 96
        //attslist.get(0).getList().get(0).setAttr_price("10.02");
        //attslist.get(1).getList().get(2).setAttr_price("10.02");
       // attslist.get(1).getList().get(3).setAttr_price("1.02");
        //累计默认选中第一项金额
        totalAmout = baseAmout;
        for (BigClassification bigClassification :
                attslist) {
                java.math.BigDecimal d1 = new java.math.BigDecimal(String.valueOf(totalAmout));
                java.math.BigDecimal d2 = new java.math.BigDecimal(bigClassification.getList().get(0).getAttr_price());
                totalAmout = d1.add(d2).doubleValue();
                attsStr = attsStr + "*" + bigClassification.getList().get(0).getName();
        }

    }

    @Override
    public void onSelected(String title, String smallTitle) {
        //商品规格选择回调
        //priceView.setText(Double.toString(this.mGoodsDetail.getDouble("price")));
        totalAmout = baseAmout;
        attsStr = "";
        for (int i = 0; i < attslist.size(); i++) {
            if (attslist.get(i).getName().trim().contentEquals(title.trim())) {
                for (int j = 0; j < attslist.get(i).getList().size(); j++) {
                    if (attslist.get(i).getList().get(j).getName().trim().contentEquals(smallTitle.trim())) {
                        attslist.get(i).getList().get(j).setSelect(true);
                    } else {
                        attslist.get(i).getList().get(j).setSelect(false);
                    }
                }
            }
        }
        //累计选中金额
        for (BigClassification bigClassification :
                attslist) {
            for (BigClassification.SmallClassification smallClassification :
                    bigClassification.getList()) {
                if (smallClassification.isSelect()) {
                    java.math.BigDecimal d1 = new java.math.BigDecimal(String.valueOf(totalAmout));
                    java.math.BigDecimal d2 = new java.math.BigDecimal(smallClassification.getAttr_price());
                    totalAmout = d1.add(d2).doubleValue();
                    attsStr = attsStr + "*" + smallClassification.getName();
                }
            }
        }
        priceView.setText(Double.toString(totalAmout));
    }

    public GoodsSkuDelegate setGoodsId(int goodsId) {
        this.mGoodsID = goodsId;
        return this;
    }

    public GoodsSkuDelegate setGoodsDetail(JSONObject goodsDetail) {
        this.mGoodsDetail = goodsDetail;
        return this;
    }

    private void addSku(int goodsId) {
        getSupportDelegate().popTo(ShopCartDelegate.class, true);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mCloseIv) {
            mDialog.cancel();
        } else if (id == R.id.mSkuView) {
            addSku(mGoodsID);
            //加入购物车
        } else if (id == R.id.btn_dialog_sku_cart) {
            //上送报文指 平台
            postSkuCart();

        }
    }

    private void postSkuCart() {
        final String addcartUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.cart.insert";
        LatteLogger.d("addcart", addcartUrl);
        final WeakHashMap<String, Object> addcart = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        addcart.put("userId", mUserId);
        addcart.put("goods_id", mGoodsID);
        addcart.put("goods_sn", mGoodsID);
        addcart.put("goods_name", this.mGoodsDetail.getString("name"));
        addcart.put("market_price", this.mGoodsDetail.getDoubleValue("price"));
        addcart.put("goods_price", totalAmout);
        addcart.put("goods_number", mSkuCountBtn.getNumber());
        if (null == attslist) {
            ToastUtil.showToast(window.getContext(), "数据有误，请稍后再试");
            return;
        }
        addcart.put("goods_attr_id", attslist.get(0).getList().get(0).getId());

        if (StringUtils.isEmpty(attsStr)) {

            ToastUtil.show(getContext(), "服务器忙，请稍后再试");
            return;
        }
        addcart.put("goods_attr", attsStr.startsWith("*") ? attsStr.substring(1) : attsStr);
        final String jsonString = JSON.toJSONString(addcart);
        LogUtils.e(""+jsonString);
        RestClient.builder()
                .url(addcartUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        final Integer isAdded = JSON.parseObject(response).getInteger("code");
                        final String tip = JSON.parseObject(response).getString("msg");
                        if (isAdded == 0 && !StringUtils.isEmpty(tip)) {
                            mDialog.cancel();
                            ToastUtil.showToast(window.getContext(), "添加成功");
                        }else{
                            ToastUtil.showToast(window.getContext(), "" + tip);
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        ToastUtil.showToast(window.getContext(), "购物车添加失败" );
                    }
                })
                .build()
                .post();
    }

    @Override
    public Object setLayout() {
        return R.layout.dialog_sku_pop;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};

    private TagFlowLayout mFlowLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_sku_pop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.mSkuContentView);
        //mFlowLayout.setMaxSelectCount(3);
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.layout_sku_item,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
                //view.setVisibility(View.GONE);
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                getActivity().setTitle("choose:" + selectPosSet.toString());
            }
        });
    }


}
