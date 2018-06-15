package com.flj.latte.ec.main.discover;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.main.cart.OrderPayDelegate;
import com.flj.latte.ec.main.personal.list.ListBean;
import com.flj.latte.ec.main.personal.order.OrderCommentDelegate;
import com.flj.latte.ec.main.personal.order.OrderListDelegate;
import com.flj.latte.util.log.LatteLogger;

/**
 * Created by yb
 */

public class DiscoverListClickListener extends SimpleClickListener {

    private final LatteDelegate DELEGATE;

    public static final String URL_TYPE = "URL_TYPE";
    private Bundle mArgs = null;

    public DiscoverListClickListener(LatteDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        AppCompatTextView editText=(AppCompatTextView)view.findViewById(R.id.tv_discover_list_title);
        String url= editText.getTag().toString();
        LatteLogger.d("url", url);

        mArgs = new Bundle();
        mArgs.putString(URL_TYPE, url);

        //mArgs.putString("mHtml", "test");

        final DiscoverDelegate delegate = new DiscoverDelegate();
        delegate.setArguments(mArgs);
        DELEGATE.getParentDelegate().getSupportDelegate().start(delegate);


    }



    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
