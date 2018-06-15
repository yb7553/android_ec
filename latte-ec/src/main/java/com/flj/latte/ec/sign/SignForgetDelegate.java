package com.flj.latte.ec.sign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.flj.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;

public class SignForgetDelegate extends LatteDelegate implements View.OnClickListener {

    private TextInputEditText mMobile = null;
    private TextInputEditText mVcode = null;
    @Override
    public void onClick(View view) {

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_forget;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mMobile = $(R.id.edit_sign_in_mobile);
        mVcode = $(R.id.edit_sign_forget_vcode);
        $(R.id.btn_sign_forget).setOnClickListener(this);
    }
}
