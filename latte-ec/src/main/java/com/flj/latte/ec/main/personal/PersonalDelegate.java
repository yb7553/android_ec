package com.flj.latte.ec.main.personal;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.main.personal.address.AddressDelegate;
import com.flj.latte.ec.main.personal.list.ListAdapter;
import com.flj.latte.ec.main.personal.list.ListBean;
import com.flj.latte.ec.main.personal.list.ListItemType;
import com.flj.latte.ec.main.personal.order.OrderListDelegate;
import com.flj.latte.ec.main.personal.profile.UserProfileDelegate;
import com.flj.latte.ec.main.personal.settings.AboutDelegate;
import com.flj.latte.ec.main.personal.settings.SettingsDelegate;
import com.flj.latte.ec.sign.SignInDelegate;
import com.flj.latte.ec.sign.SignUpdateDelegate;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yb
 */

public class PersonalDelegate extends BottomItemDelegate {

    public static final String ORDER_TYPE = "ORDER_TYPE";
    private Bundle mArgs = null;

    private CircleImageView mCircleImageView= null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    private void onClickAllOrder() {
        mArgs.putString(ORDER_TYPE, "all");

        startOrderListByType();
    }

    private void onClickAvatar() {
        getParentDelegate().getSupportDelegate().start(new UserProfileDelegate());
    }

    private void onClickSignOut() {
        getParentDelegate().getSupportDelegate().start(new SignInDelegate());
    }

    private void startOrderListByType() {
        final OrderListDelegate delegate = new OrderListDelegate();
        delegate.setArguments(mArgs);
        getParentDelegate().getSupportDelegate().start(delegate);
    }






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();
    }



    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        final RecyclerView rvSettings = $(R.id.rv_personal_setting);
        mCircleImageView=$(R.id.img_user_avatar);


        Glide.with(getContext())
                .load(LattePreference.getCustomAppProfile("avatar"))
                .into(mCircleImageView);


        $(R.id.tv_all_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAllOrder();
            }
        });
        $(R.id.img_user_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAvatar();
            }
        });

        $(R.id.ll_outlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignOut();
            }
        });



        final ListBean address = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setDelegate(new AddressDelegate())
                .setText("收货地址")
                .build();

        final ListBean system = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setDelegate(new AboutDelegate())
                .setText("我的智能设备")
                .build();

        final ListBean message = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(3)
                //.setDelegate(new MessageDelegate())
                .setText("我的消息")
                .build();

        final ListBean  password= new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(4)
                .setDelegate(new SignUpdateDelegate())
                .setText("修改密码")
                .build();

        final List<ListBean> data = new ArrayList<>();
        data.add(message);
        data.add(system);
        data.add(address);
        data.add(password);


        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvSettings.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        rvSettings.setAdapter(adapter);
        rvSettings.addOnItemTouchListener(new PersonalClickListener(this));
    }
}
