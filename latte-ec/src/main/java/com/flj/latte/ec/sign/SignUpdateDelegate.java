package com.flj.latte.ec.sign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.flj.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;

public class SignUpdateDelegate extends LatteDelegate implements View.OnClickListener {
    @Override
    public void onClick(View view) {

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_update;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.btn_sign_update).setOnClickListener(this);
    }
}
