package com.flj.latte.ec.main.personal.address;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe 地址选择器适配器
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/21 0021.
 */
public class SelectAddressAdapter extends MultipleRecyclerAdapter {
    LatteDelegate delegate;
    protected SelectAddressAdapter(List<MultipleItemEntity> data,LatteDelegate delegate) {
        super(data);
        this.delegate=delegate;
        addItemType(AddressItemType.ITEM_ADDRESS, R.layout.item_select_address);
    }

    @Override
    protected void convert(final MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case AddressItemType.ITEM_ADDRESS:
                final String name = entity.getField(MultipleFields.NAME);
                final String phone = entity.getField(AddressItemFields.PHONE);
                String address = entity.getField(AddressItemFields.ADDRESS);
                String location = entity.getField(AddressItemFields.LOCATION);
                address = location + address;
                final boolean isDefault = entity.getField(MultipleFields.TAG);
                final int id = entity.getField(MultipleFields.ID);
                final AppCompatTextView nameText = holder.getView(R.id.tv_address_name);
                final AppCompatTextView phoneText = holder.getView(R.id.tv_address_phone);
                final AppCompatTextView addressText = holder.getView(R.id.tv_address_address);
                final IconTextView select = holder.getView(R.id.icon_item_shop_cart);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                String finalAddress = address;
                ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ll_item.setClickable(true);
                        Bundle bundle=new Bundle();
                        bundle.putString("name",name);
                        bundle.putString("phone",phone);
                        bundle.putString("address", finalAddress);
                        bundle.putInt("id",id);
                        delegate.getSupportDelegate().setFragmentResult(Activity.RESULT_OK,bundle);
                        delegate.getSupportDelegate().pop();
                    }
                });
                nameText.setText(name);
                phoneText.setText(phone);
                addressText.setText(address);
                break;
            default:
                break;
        }
    }
}
