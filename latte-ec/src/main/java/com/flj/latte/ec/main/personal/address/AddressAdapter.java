package com.flj.latte.ec.main.personal.address;

import android.app.Dialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;


import com.alibaba.fastjson.JSON;
import com.flj.latte.dialog.CommomDialog;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class AddressAdapter extends MultipleRecyclerAdapter {

    protected AddressAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(AddressItemType.ITEM_ADDRESS, R.layout.item_address);
    }

    @Override
    protected void convert(final MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case AddressItemType.ITEM_ADDRESS:
                final String name = entity.getField(MultipleFields.NAME);
                final String phone = entity.getField(AddressItemFields.PHONE);
                final String address = entity.getField(AddressItemFields.ADDRESS);
                final boolean isDefault = entity.getField(MultipleFields.TAG);
                final int id = entity.getField(MultipleFields.ID);

                final AppCompatTextView nameText = holder.getView(R.id.tv_address_name);
                final AppCompatTextView phoneText = holder.getView(R.id.tv_address_phone);
                final AppCompatTextView addressText = holder.getView(R.id.tv_address_address);
                final AppCompatTextView deleteTextView = holder.getView(R.id.tv_address_delete);
                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CommomDialog(mContext, R.style.commondialog).setContent("是否删除该条地址信息").setNegativeButton("取消", new CommomDialog.onCancelListener() {
                            @Override
                            public void onClickCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确定", new CommomDialog.onSubmitListener() {
                            @Override
                            public void onClickSubmit(Dialog dialog) {
                                dialog.dismiss();

                                final String addressDeleteUrl = API.Config.getDomain() + API.CONFIGNEE_DELETE;
                                LatteLogger.d("addressDeleteUrl", addressDeleteUrl);
                                final WeakHashMap<String, Object> addressDelete = new WeakHashMap<>();
                                LatteLogger.d("addressid", id);
                                addressDelete.put("id",id);
                                final String jsonString = JSON.toJSONString(addressDelete);

                                RestClient.builder()
                                        .url(addressDeleteUrl)
                                        .raw(jsonString)
                                        .success(new ISuccess() {
                                            @Override
                                            public void onSuccess(String response) {
                                                LatteLogger.d("addressUrl", response);
                                                remove(holder.getLayoutPosition());
                                            }
                                        })
                                        .build()
                                        .post();
                            }
                        }).show();

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
