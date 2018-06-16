package com.flj.latte.ec.main.discover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.delegates.web.WebDelegateImpl;

import com.flj.latte.delegates.web.WebDelegatePageImpl;
import com.flj.latte.ec.R;
import com.flj.latte.ec.main.cart.ShopOrderDelegate;
import com.flj.latte.ec.main.personal.PersonalDelegate;
import com.flj.latte.ec.main.personal.address.AddressAddDelegate;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.log.LatteLogger;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by yb
 */

public class DiscoverDelegate extends LatteDelegate implements ISuccess,View.OnClickListener {
    private String mUrl= null;
    private AppCompatTextView mAppCompatTextView=null;
    @Override
    public Object setLayout() {
        return R.layout.delegate_discover;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mUrl = args.getString(DiscoverListClickListener.URL_TYPE);
            LatteLogger.d("mUrl",mUrl);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mAppCompatTextView = $(R.id.web_discovery_container_share);
        $(R.id.web_discovery_container_share).setOnClickListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final WebDelegatePageImpl delegate = WebDelegatePageImpl.create(mUrl);
        delegate.setTopDelegate(this.getParentDelegate());
        getSupportDelegate().loadRootFragment(R.id.web_discovery_container, delegate);
    }

//    @Override
//    public FragmentAnimator onCreateFragmentAnimator() {
//        return new DefaultHorizontalAnimator();
//    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.icon_address_add) {
            onClickDiscoveryShare();
        }
    }

    private void onClickDiscoveryShare() {



    }

    @Override
    public void onSuccess(String response) {

    }
}
