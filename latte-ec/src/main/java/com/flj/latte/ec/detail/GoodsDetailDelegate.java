package com.flj.latte.ec.detail;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.YoYo;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.ec.main.cart.ShopCartDetailDelegate;
import com.flj.latte.ec.sign.SignInDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IFailure;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.animation.BezierUtil;
import com.flj.latte.ui.banner.HolderCreator;
import com.flj.latte.ui.widget.CircleTextView;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by yb
 */

public class GoodsDetailDelegate extends LatteDelegate implements
        AppBarLayout.OnOffsetChangedListener,
        BezierUtil.AnimationListener {

    private TabLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private ConvenientBanner<String> mBanner = null;

    private CircleTextView mCircleTextView = null;
    private RelativeLayout mRlAddShopCart = null;
    private IconTextView mIconShopCart = null;

    private static final String ARG_GOODS_ID = "ARG_GOODS_ID";
    private int mGoodsId = -1;
    private String mGoodesName = "";
    private double mGoodesPirce = 0;

    private String mGoodsThumbUrl = null;
    private int mShopCount = 0;

    private JSONObject mData = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .override(100, 100);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mGoodsId = args.getInt(ARG_GOODS_ID);
        }
    }

    private void onClickAddShopCart() {

        //final String mToken=LattePreference.getCustomAppProfile("token");
        final boolean mSign = LattePreference.getAppFlag("SIGN_TAG");
        if (mSign) {


//            final CircleImageView animImg = new CircleImageView(getContext());
//            Glide.with(this)
//                    .load(mGoodsThumbUrl)
//                    .apply(OPTIONS)
//                    .into(animImg);
//            BezierAnimation.addCart(this, mRlAddShopCart, mIconShopCart, animImg, this);
            GoodsSkuDelegate.create(GoodsDetailDelegate.this)
                    // .setPayResultListener(this)
                    .setGoodsId(mGoodsId)
                    .setGoodsDetail(mData)
                    .beginSkuDialog();
        } else {
            getSupportDelegate().start(new SignInDelegate());
        }
    }

    private void onClickShopCart() {

        if (LattePreference.getCustomAppProfile("token") != null) {
            getSupportDelegate().start(new ShopCartDetailDelegate());

        }
    }

    private void setShopCartCount(JSONObject data) {
        mGoodsThumbUrl = data.getString("thumb");
        if (mShopCount == 0) {
            mCircleTextView.setVisibility(View.GONE);
        }
    }

    public static GoodsDetailDelegate create(int goodsId) {
        final Bundle args = new Bundle();
        args.putInt(ARG_GOODS_ID, goodsId);
        final GoodsDetailDelegate delegate = new GoodsDetailDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTabLayout = $(R.id.tab_layout);
        mViewPager = $(R.id.view_pager);
        mBanner = $(R.id.detail_banner);
        final CollapsingToolbarLayout mCollapsingToolbarLayout = $(R.id.collapsing_toolbar_detail);
        final AppBarLayout mAppBar = $(R.id.app_bar_detail);

        //底部
        mCircleTextView = $(R.id.tv_shopping_cart_amount);
        mRlAddShopCart = $(R.id.rl_add_shop_cart);
        mIconShopCart = $(R.id.icon_shop_cart);

        $(R.id.rl_add_shop_cart).setOnClickListener(view -> onClickAddShopCart());

        $(R.id.rl_shop_cart).setOnClickListener(view -> onClickShopCart());

        mCollapsingToolbarLayout.setContentScrimColor(Color.WHITE);
        mAppBar.addOnOffsetChangedListener(this);
        mCircleTextView.setCircleBackground(Color.RED);
        initData();
        initTabLayout();
    }

    private void initPager(JSONObject data) {
        final PagerAdapter adapter = new TabPagerAdapter(getFragmentManager(), data);
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        final Context context = getContext();
        if (context != null) {
            mTabLayout.setSelectedTabIndicatorColor
                    (ContextCompat.getColor(context, R.color.app_main));
        }
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {
        final String goodesdetailUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.product.detail";
        LatteLogger.d("IUDHAS", goodesdetailUrl);
        final WeakHashMap<String, Object> goodesdetail = new WeakHashMap<>();
        //TODO:mGoodsId 测试
        mGoodsId=97;
        goodesdetail.put("id", mGoodsId);
        final String jsonString = JSON.toJSONString(goodesdetail);

        RestClient.builder()
                .url(goodesdetailUrl)
                //.params("goods_id", mGoodsId)
                .raw(jsonString)
                // .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        mData =
                                JSON.parseObject(response).getJSONObject("data");
                        if (null == mData) {
                            return;
                        }
                        mGoodesName = mData.getString("name");
                        mGoodesPirce = mData.getDouble("price");
                        initBanner(mData);
                        initGoodsInfo(mData);
                        initPager(mData);
                        setShopCartCount(mData);
                    }

                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        ToastUtil.show(getContext(), "服务器忙，请稍后再试");
                    }
                })
                .build()
                .post();
    }

    private void initGoodsInfo(JSONObject data) {
        final String goodsData = data.toJSONString();
        getSupportDelegate().
                loadRootFragment(R.id.frame_goods_info, GoodsInfoDelegate.create(goodsData));


    }

    private void initBanner(JSONObject data) {
        final JSONArray array = data.getJSONArray("banners");
        final List<String> images = new ArrayList<>();
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            images.add(array.getString(i));
        }
        mBanner
                .setPages(new HolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onAnimationEnd() {
        YoYo.with(new ScaleUpAnimator())
                .duration(500)
                .playOn(mIconShopCart);
        final String addcartUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.cart.insert";
        LatteLogger.d("addcart", addcartUrl);
        final WeakHashMap<String, Object> addcart = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        addcart.put("userId", mUserId);
        addcart.put("goods_id", mGoodsId);
        addcart.put("goods_sn", "sn");
        addcart.put("goods_name", mGoodesName);
        addcart.put("market_price", mGoodesPirce);
        addcart.put("goods_price", mGoodesPirce);
        addcart.put("goods_number", 1);
        addcart.put("goods_attr_id", 1);
        addcart.put("goods_attr", "测试");

        final String jsonString = JSON.toJSONString(addcart);

        RestClient.builder()
                .url(addcartUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        final Integer isAdded = JSON.parseObject(response).getInteger("code");
                        if (isAdded == 0) {
                            mShopCount++;
                            mCircleTextView.setVisibility(View.VISIBLE);
                            mCircleTextView.setText(String.valueOf(mShopCount));
                        }
                    }
                })
                // .params("count", mShopCount)
                .build()
                .post();
    }
}
