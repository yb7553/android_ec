package com.flj.latte.ec.main.personal.send;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;

public class SendTypeDelegate extends LatteDelegate implements View.OnClickListener {


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_send_time) {
            SendTime.create(this).beginSendDialog();
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_send_type;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.tv_send_time).setOnClickListener(this);
    }
}
