package com.flj.latte.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flj.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.common.util.FormatUtil;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.log.LatteLogger;

import java.util.WeakHashMap;

/**
 * Created by yb on 2017/4/22
 */

public class SignUpDelegate extends LatteDelegate  {


    private TextInputEditText mMobile = null;
    private TextInputEditText mVcode = null;
    private TextInputEditText mPassword = null;

    private AppCompatButton mSms = null;

    private ISignListener mISignListener = null;
    private int mSurplus;

    private int mVerify = 0;
    private int mSignup= 0;





    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    private void onClickSignUp() {
        if (checkForm()) {

            final String SignUpUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.auth.mobile.signup";
            LatteLogger.d("SignUpUrl", SignUpUrl);
            final WeakHashMap<String, Object> SignUp = new WeakHashMap<>();
            SignUp.put("mobile",mMobile.getText().toString());
            SignUp.put("code",mVcode.getText().toString());
            SignUp.put("password",mPassword.getText().toString());
            final String jsonString = JSON.toJSONString(SignUp);

            RestClient.builder()
                    .url(SignUpUrl)
                    .raw(jsonString)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            LatteLogger.json("SignUp", response);
                            SignHandler.onSignUp(response, mISignListener);
                            getSupportDelegate().startWithPop(new SignInDelegate());
                        }
                    })
                    .build()
                    .post();
        }
    }

    private void onClickSms() {

        if (checkMobile()) {
            final String smsUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.auth.mobile.send";
            LatteLogger.d("payOrSignUrl", smsUrl);
            final WeakHashMap<String, Object> sms = new WeakHashMap<>();
            sms.put("mobile", mMobile.getText().toString());


            final String jsonString = JSON.toJSONString(sms);
            RestClient.builder()
                    .url(smsUrl)
                    .raw(jsonString)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            LatteLogger.d("sms", response);
                            final int code = JSON.parseObject(response).getInteger("code");

                            CountDownTimer timer = new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    mSms.setEnabled(false);
                                    mSms.setText("已发送(" + millisUntilFinished / 1000 + ")");

                                }

                                @Override
                                public void onFinish() {
                                    mSms.setEnabled(true);
                                    mSms.setText("重新获取");

                                }
                            }.start();


                        }
                    })
                    .build()
                    .post();
        }

    }

    private int onClickSmsverify() {


        final String smsverifyUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.auth.mobile.verify";
        LatteLogger.d("payOrSignUrl", smsverifyUrl);
        final WeakHashMap<String, Object> smsverify = new WeakHashMap<>();
        smsverify.put("mobile",mMobile.getText().toString());
        smsverify.put("code",mVcode.getText().toString());
        final String jsonString = JSON.toJSONString(smsverify);
        RestClient.builder()
                .url(smsverifyUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("smsverify", response);
                        mVerify = JSON.parseObject(response).getInteger("code");

                    }
                })
                .build()
                .post();

        return mVerify;

    }

    private int onClickissignup() {


        final String signupUrl = "http://120.79.230.229/bfwl-mall/calmdown/v2/ecapi.auth.mobile.issignup";
        LatteLogger.d("signupUrl", signupUrl);
        final WeakHashMap<String, Object> signup = new WeakHashMap<>();
        signup.put("mobile",mMobile.getText().toString());
        final String jsonString = JSON.toJSONString(signup);
        RestClient.builder()
                .url(signupUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("signup", response);
                        mSignup = JSON.parseObject(response).getInteger("code");

                    }
                })
                .build()
                .post();

        return mSignup;

    }

    private void onClickLink() {
        getSupportDelegate().start(new SignInDelegate());
    }

    private boolean checkForm() {
        final String mobile = mMobile.getText().toString();
        final String password = mVcode.getText().toString();
        final String rePassword = mPassword.getText().toString();

        boolean isPass = true;



        if (mobile.isEmpty() || mobile.length() != 11) {
            mMobile.setError("手机号码错误");
            isPass = false;
        } else {
            mMobile.setError(null);
        }

        final int issignup=onClickissignup();
        if (issignup==0) {
            mMobile.setError("该手机用户已注册");
            isPass = false;
        } else {
            mVcode.setError(null);
        }

        if (password.isEmpty()) {
            mVcode.setError("请填写验证码");
            isPass = false;
        } else {
            mVcode.setError(null);
        }

        final int isVerify=onClickSmsverify();
        if (isVerify!=0) {
            mVcode.setError("验证码错误");
            isPass = false;
        } else {
            mVcode.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 ) {
            mPassword.setError("请填写至少6为密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        return isPass;
    }

    private boolean checkMobile() {
        final String mobile = mMobile.getText().toString();


        boolean isPass = true;



        if (mobile.isEmpty() || mobile.length() != 11) {
            mMobile.setError("手机号码错误");
            isPass = false;
        } else {
            mMobile.setError(null);
        }

        final int issignup=onClickissignup();
        if (issignup==0) {
            mMobile.setError("该手机用户已注册");
            isPass = false;
        } else {
            mVcode.setError(null);
        }



        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        mName = $(R.id.edit_sign_up_name);
        mSms = $(R.id.btn_sign_up_sms);
        mMobile = $(R.id.edit_sign_up_mobile);
        mVcode = $(R.id.edit_sign_up_vcode);
        mPassword = $(R.id.edit_sign_up_password);

        $(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });

        $(R.id.tv_link_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLink();
            }
        });

        $(R.id.btn_sign_up_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSms();
            }
        });
    }

}
