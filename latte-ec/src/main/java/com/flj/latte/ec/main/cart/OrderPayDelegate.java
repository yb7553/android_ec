package com.flj.latte.ec.main.cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.delegates.web.WebDelegateImpl;
import com.flj.latte.ec.R;
import com.flj.latte.util.log.LatteLogger;

/**
 * Created by yb
 */

public class OrderPayDelegate extends LatteDelegate {

   private String mHtml= null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_orderpay;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mHtml = args.getString(ShopOrderDelegate.HTML_TYPE);
            LatteLogger.d("mHtml",mHtml);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final WebDelegateImpl delegate = WebDelegateImpl.create(mHtml);
        delegate.setTopDelegate(this.getParentDelegate());
        getSupportDelegate().loadRootFragment(R.id.web_orderpay_container, delegate);
    }

//    @Override
//    public FragmentAnimator onCreateFragmentAnimator() {
//        return new DefaultHorizontalAnimator();
//    }
}
