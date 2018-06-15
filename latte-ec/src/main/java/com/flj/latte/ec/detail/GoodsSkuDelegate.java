package com.flj.latte.ec.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.flj.latte.app.ConfigKeys;
import com.flj.latte.app.Latte;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.pay.IAlPayResultListener;
import com.flj.latte.ec.pay.PayAsyncTask;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.loader.LatteLoader;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.wechat.LatteWeChat;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Set;


/**
 * Created by yb
 */


public class GoodsSkuDelegate extends LatteDelegate implements View.OnClickListener {


    private Activity mActivity = null;

    private AlertDialog mDialog = null;
    private int mGoodsID = -1;
    private JSONObject mGoodsDetail = null;


    private GoodsSkuDelegate(LatteDelegate delegate) {
        this.mActivity = delegate.getProxyActivity();
        this.mDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }

    public static GoodsSkuDelegate create(LatteDelegate delegate) {
        return new GoodsSkuDelegate(delegate);
    }

    public void beginSkuDialog() {
        mDialog.show();
        final Window window = mDialog.getWindow();
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

            window.findViewById(R.id.btn_dialog_sku_cart).setOnClickListener(this);
            //window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this);
            //window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this);

            final ImageView imageView = window.findViewById(R.id.mGoodsIconIv);
            final TextView textView =  window.findViewById(R.id.mGoodsCodeTv);
            final TextView priceView =window.findViewById(R.id.mGoodsPriceTv);
            textView.setText(this.mGoodsDetail.getString("name"));

            priceView.setText(Double.toString(this.mGoodsDetail.getDouble("price")));
            //final Uri uri=Uri.parse(this.mGoodsDetail.getJSONObject("default_photo").getString("large"));
             Glide.with(this.mDialog.getContext())
                    .load(this.mGoodsDetail.getJSONObject("default_photo").getString("large"))
                    .into(imageView);



        }
    }



    public GoodsSkuDelegate setGoodsId(int goodsId) {
        this.mGoodsID = goodsId;
        return this;
    }

    public GoodsSkuDelegate setGoodsDetail(JSONObject goodsDetail){
        this.mGoodsDetail = goodsDetail;
        return this;
    }

    private void addSku(int goodsId) {

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



            mDialog.cancel();
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.dialog_sku_pop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.mSkuContentView);
        //mFlowLayout.setMaxSelectCount(3);
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals)
        {

            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.layout_sku_item,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
                //view.setVisibility(View.GONE);
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                getActivity().setTitle("choose:" + selectPosSet.toString());
            }
        });
    }
}
