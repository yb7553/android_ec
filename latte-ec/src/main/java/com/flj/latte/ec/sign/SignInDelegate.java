package com.flj.latte.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flj.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.wechat.LatteWeChat;
import com.flj.latte.wechat.callbacks.IWeChatSignInCallback;

import java.util.WeakHashMap;


/**
 * Created by yb on 2017/4/22
 */

public class SignInDelegate extends LatteDelegate implements View.OnClickListener {

    private TextInputEditText mMobile = null;
    private TextInputEditText mPassword = null;
    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    private void onClickSignIn() {
        if (checkForm()) {
            final String signinUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.auth.signin";
            LatteLogger.d("signinUrl", signinUrl);
            final WeakHashMap<String, Object> signin = new WeakHashMap<>();
            signin.put("username",mMobile.getText().toString());

            signin.put("password",mPassword.getText().toString());
            final String jsonString = JSON.toJSONString(signin);

            RestClient.builder()
                    .url(signinUrl)
                    .raw(jsonString)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            LatteLogger.json("signinUrl", response);
                            final Integer code = JSON.parseObject(response).getInteger("code");
                            if(code==0) {
                                SignHandler.onSignIn(response, mISignListener);

                                getSupportDelegate().startWithPop(new EcBottomDelegate());
                            }else{

                                mMobile.setError("用户或密码错误");
                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    private void onClickWeChat() {
        LatteWeChat
                .getInstance()
                .onSignSuccess(new IWeChatSignInCallback() {
                    @Override
                    public void onSignInSuccess(String userInfo) {
                        Toast.makeText(getContext(), userInfo, Toast.LENGTH_LONG).show();
                    }
                })
                .signIn();
    }

    private void onClickLink() {
        getSupportDelegate().start(new SignUpDelegate());
    }

    private void  onClickForgetPassword(){
        getSupportDelegate().start(new SignForgetDelegate());
    }

    private boolean checkForm() {
        final String mobile = mMobile.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;

        if (mobile.isEmpty()) {
            mMobile.setError("错误的手机格式");
            isPass = false;
        } else {
            mMobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mMobile = $(R.id.edit_sign_in_mobile);
        mPassword = $(R.id.edit_sign_in_password);
        $(R.id.btn_sign_in).setOnClickListener(this);
        $(R.id.tv_link_sign_up).setOnClickListener(this);
        //$(R.id.icon_sign_in_wechat).setOnClickListener(this);

        $(R.id.tv_forget_password).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_sign_in) {
            onClickSignIn();
        } else if (i == R.id.tv_link_sign_up) {
            onClickLink();
//        } else if (i == R.id.icon_sign_in_wechat) {
//            onClickWeChat();
        } else if (i == R.id.tv_forget_password) {
            onClickForgetPassword();
        }
    }
}
