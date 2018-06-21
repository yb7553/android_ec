package com.flj.latte.ec.main.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.lbs.view.MainActivity;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.ec.main.index.search.SearchDelegate;
import com.flj.latte.ec.rfragment.Friend;
import com.flj.latte.ec.rfragment.HomeActivity;
import com.flj.latte.ec.sign.SignInDelegate;
import com.flj.latte.net.RestCreator;
import com.flj.latte.net.rx.RxRestClient;
import com.flj.latte.ui.recycler.BaseDecoration;
import com.flj.latte.ui.refresh.RefreshHandler;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.flj.latte.util.storage.LattePreference;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;


import android.net.Uri;
import android.util.Log;
import io.rong.imlib.model.Message;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by yb
 */

public class IndexDelegate extends BottomItemDelegate implements
        View.OnFocusChangeListener,RongIM.UserInfoProvider {

    private EditText ed;
    ArrayList<Friend> userIdList = new ArrayList<Friend>();
    // public static final String TOKEN ="AzDJwMVt2nRAZOFVsKTbKq+YsUIoF3ojin3K277sfOlvDwWFPGN4jClKhWxl7gE0N9pVf/zbHE5GfOaV2jK4tatdpZUyLdaH";
    //悟空  18673668974  1100
    private static final String token1 = "dW+nKsnlAJgaYlUW9GYEEmMRlYabxy6EVmDP0KKdd1ik/l6ffCLpRVr7xNrJ6VeZVT09++kymu2oEWYnly5Jvg==";
    //贝吉塔 18673668975 1101
    private static final String token2 = LattePreference.getCustomAppProfile("rongtoken");


    private String string="";
    private String roomId;

    private RecyclerView mRecyclerView = null;
    private SwipeRefreshLayout mRefreshLayout = null;


    private RefreshHandler mRefreshHandlerbanner = null;
    private RefreshHandler mRefreshHandler = null;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_index);
        mRefreshLayout = $(R.id.srl_index);

        final IconTextView mIconScan = $(R.id.icon_index_scan);
        final AppCompatEditText mSearchView = $(R.id.et_search_view);

        //$(R.id.icon_index_scan).setOnClickListener(view -> startScanWithCheck(getParentDelegate()));
        $(R.id.icon_index_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                connectRongServer(token2);
//                //启动会话列表
//                startActivity(new Intent(getContext(), HomeActivity.class));

                //启动会话界面
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startPrivateChat(getContext(), "1100", "悟空");

                //首先需要构造使用客服者的用户信息
                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                CSCustomServiceInfo csInfo = csBuilder
                        .nickName("客官莫急")
                        .loginName(LattePreference.getCustomAppProfile("nickname"))
                        .mobileNo(LattePreference.getCustomAppProfile("username"))
                        .userId(LattePreference.getCustomAppProfile("id"))
                        .enterUrl(LattePreference.getCustomAppProfile("avatar"))
                        .build();

/**
 * 启动客户服聊天界面。
 *
 * @param context           应用上下文。
 * @param customerServiceId 要与之聊天的客服 Id。
 * @param title             聊天的标题，如果传入空值，则默认显示与之聊天的客服名称。
 * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
 */
                RongIM.getInstance().startCustomerServiceChat(getActivity(),"KEFU152959386239127", "在线客服",csInfo);
            }
        });

        $(R.id.icon_index_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                Toast.makeText(getContext(), "testasdfa", Toast.LENGTH_SHORT).show();

            }
        });



        mRefreshHandlerbanner= RefreshHandler.create(mRefreshLayout, mRecyclerView, new BannerDataConverter());
        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());


        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN, args ->
                        Toast.makeText(getContext(), "得到的二维码是" + args, Toast.LENGTH_LONG).show());
        mSearchView.setOnFocusChangeListener(this);

//        onCallRxGet();
//        onCallRxRestClient();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Toolbar toolbar = view.findViewById(R.id.tb_index);
        toolbar.getBackground().setAlpha(0);

        //setContentView(R.layout.activity_main);
        initUserInfo();
//        String packageName = this.getPackageName();
//        log("----------->"+packageName);
        initView();
        connectRongServer(token2);
        //initData();
    }


    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void initRecyclerView() {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        final Context context = getContext();
        mRecyclerView.setLayoutManager(manager);
        if (context != null) {
            mRecyclerView.addItemDecoration
                    (BaseDecoration.create(ContextCompat.getColor(context, R.color.app_background), 5));
        }
        final EcBottomDelegate ecBottomDelegate = getParentDelegate();
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(ecBottomDelegate));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();

        //mRefreshHandlerbanner.banners("http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.banner.list");
        //mRefreshHandlerbanner.banners("http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.banner.list");
        mRefreshHandler.firstPage("http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.product.list");


    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            getParentDelegate().start(new SearchDelegate());
        }
    }

    @Override
    public void onDestroy() {
        RongIM.getInstance().disconnect();
        super.onDestroy();
    }


    private void initView() {

    }
    private void connectRongServer(String token1) {
        RongIM.connect(token2, new RongIMClient.ConnectCallback() {
            //token1参数报错
            @Override
            public void onTokenIncorrect() {
                Log.e("TAG","参数错误");
                Toast.makeText(getContext(), "token1参数报错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s) {
                Log.e("TAG","成功");
                Toast.makeText(getContext(), "聊天连接成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("TAG","失败");
                Toast.makeText(getContext(), errorCode.getValue()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //初始化用户信息
    private void initUserInfo() {

        userIdList.add(new Friend("1100", "悟空", "http://pic1.win4000.com/pic/c/87/866c1411319.jpg"));//悟空图标
        userIdList.add(new Friend("1101", "贝吉塔", "http://pic1.win4000.com/pic/e/f1/4fb01408746.jpg"));//贝吉塔图标

        RongIM.setUserInfoProvider(this, true);
    }
    private void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    private void log(String text) {
        Log.e("TAG->Result", text);
    }

    @Override
    public UserInfo getUserInfo(String s) {
        for (Friend i : userIdList) {
            if (i.getUserId().equals(s)) {
                Log.e("TAG", i.getPortraitUri());
                return new UserInfo(i.getUserId(), i.getName(), Uri.parse(i.getPortraitUri()));
            }
        }
        Log.e("TAG", "UserId is : " + s);
        return null;
    }
}
