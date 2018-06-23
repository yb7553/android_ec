package com.flj.latte.ec.main.personal.send;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;


import com.flj.latte.ec.R;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * Created by yb
 */

public class SendAdapter extends MultipleRecyclerAdapter {

    protected SendAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SendItemType.ITEM_SENDSTAFF, R.layout.item_sendstaff);
    }

    @Override
    protected void convert(final MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case SendItemType.ITEM_SENDSTAFF:
                final String name = entity.getField(MultipleFields.NAME);
                final String phone = entity.getField(SendItemFields.PHONE);
              //  final String address = entity.getField(SendItemFields.ADDRESS);
                final boolean isDefault = entity.getField(MultipleFields.TAG);
                final int id = entity.getField(MultipleFields.ID);

                final AppCompatTextView nameText = holder.getView(R.id.tv_send_name);
                final AppCompatTextView phoneText = holder.getView(R.id.tv_send_phone);
                //final AppCompatTextView addressText = holder.getView(R.id.tv_send_address);
                final AppCompatTextView deleteTextView = holder.getView(R.id.tv_send_choose);
                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestClient.builder()
                                .url("address.php")
                                .params("id", id)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        remove(holder.getLayoutPosition());
                                    }
                                })
                                .build()
                                .post();
                    }
                });

                nameText.setText(name);
                phoneText.setText(phone);
                //addressText.setText(address);
                break;
            default:
                break;
        }
    }
}
