package com.flj.latte.ec.main.personal.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.main.personal.list.ListAdapter;
import com.flj.latte.ec.main.personal.list.ListBean;
import com.flj.latte.ec.main.personal.list.ListItemType;
import com.flj.latte.ec.main.personal.settings.NameDelegate;
import com.flj.latte.util.storage.LattePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yb
 */

public class UserProfileDelegate extends LatteDelegate implements NameDelegate.MyListerner {

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_profile;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        final RecyclerView recyclerView = $(R.id.rv_user_profile);

        final ListBean image = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_AVATAR)
                .setId(1)
                .setImageUrl(LattePreference.getCustomAppProfile("avatar"))
                .build();

        final ListBean name = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setText("昵称")
                .setDelegate(new NameDelegate())
                .setValue(LattePreference.getCustomAppProfile("nickname"))
                .build();

        final ListBean gender = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(3)
                .setText("性别")
                .setValue("男")
                .build();

//        final ListBean birth = new ListBean.Builder()
//                .setItemType(ListItemType.ITEM_NORMAL)
//                .setId(4)
//                .setText("生日")
//                .setValue("未设置生日")
//                .build();

        final List<ListBean> data = new ArrayList<>();
        data.add(image);
        data.add(name);
        data.add(gender);
//        data.add(birth);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new UserProfileClickListener(this));
    }

    @Override
    public void sendMessage(String str) {
        if(str!=null&&!"".equals(str)){
            Toast.makeText(this.getContext(), str, Toast.LENGTH_LONG).show();
        }
    }





}
