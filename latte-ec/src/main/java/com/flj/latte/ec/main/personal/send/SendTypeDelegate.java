package com.flj.latte.ec.main.personal.send;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.timepicker.TimePickerUtils;

import java.util.List;

public class SendTypeDelegate extends LatteDelegate implements View.OnClickListener {
    TextInputEditText edit_send_time, edit_send_staff;
    TimePickerUtils timePickerUtils;
    List<MultipleItemEntity> senddata;

    public SendTypeDelegate create( List<MultipleItemEntity> senddata) {
        this.senddata = senddata;
        return this;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_send_time) {
            // SendTime.create(this).beginSendDialog();
            if (null == timePickerUtils)
                timePickerUtils = new TimePickerUtils(getContext());
            timePickerUtils.setTextInputEditText(edit_send_time);
            timePickerUtils.showTimeDialog();
        } else if (i == R.id.btn_sign_update) {
            /*if (StringUtils.isEmpty(edit_send_time.getText().toString())) {
                ToastUtil.showToast(getContext(), "请选择时间");
                return;
            }*/
            if (StringUtils.isEmpty(edit_send_staff.getText().toString())) {
                ToastUtil.showToast(getContext(), "请选择配送人员");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("sendstaff", timePickerUtils.getSendID());
            bundle.putLong("endtime", timePickerUtils.getSendTime());
            bundle.putString("sendname", timePickerUtils.getSendName());
            bundle.putString("selecttime", timePickerUtils.getSelectTime());
            getSupportDelegate().setFragmentResult(Activity.RESULT_OK, bundle);
            getSupportDelegate().pop();
        } else if (i == R.id.tv_all_order2) {
            if (null == timePickerUtils)
                timePickerUtils = new TimePickerUtils(getContext());
            timePickerUtils.setTextInputEditText(edit_send_staff);
            if (null != senddata)
                timePickerUtils.showSelectDialog(senddata);

        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_send_type;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.tv_send_time).setOnClickListener(this);
        $(R.id.btn_sign_update).setOnClickListener(this);
        $(R.id.tv_all_order2).setOnClickListener(this);
        edit_send_time = $(R.id.edit_send_time);
        edit_send_staff = $(R.id.edit_send_staff);
    }




}
