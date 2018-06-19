package com.flj.latte.ec.main.personal.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.flj.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IError;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.flj.latte.util.callback.IGlobalCallback;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class NameDelegate extends LatteDelegate {

    private AppCompatTextView mName = null;
    private AppCompatEditText mNameEdit =null;
    private MyListerner listerner=null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_name;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //listerner=(MyListerner) getSupportDelegate().getActivity();
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {



        mNameEdit=$(R.id.tv_top_user_profile_name_edit);
        mName = $(R.id.tv_top_user_profile_name_save);
        mNameEdit.setText(LattePreference.getCustomAppProfile("nickname"));
        $(R.id.tv_top_user_profile_name_save).setOnClickListener(view -> onClickUserName());



    }

    private void onClickUserName(){


        final String updateNameUrl = API.Config.getDomain() + API.USER_NICKNAME_UPDATE;
        LatteLogger.d("updateName", updateNameUrl);
        final WeakHashMap<String, Object> updateName = new WeakHashMap<>();
        final Long mUserId= LattePreference.getCustomAppProfileLong("userId");
        updateName.put("userId",mUserId);
        final String mUserName = String.valueOf(mNameEdit.getText());
        updateName.put("nickName",mUserName);


        final String jsonString = JSON.toJSONString(updateName);

        RestClient.builder()
                .url(updateNameUrl)
                .raw(jsonString)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("updateName", response);
                        final Integer isAdded = JSON.parseObject(response).getInteger("code");
                        if (isAdded == 0) {
                            LattePreference.addCustomAppProfile("nickname", mUserName);
                            //listerner.sendMessage("test");
                            @SuppressWarnings("unchecked") final IGlobalCallback<String> callback = CallbackManager
                                    .getInstance()
                                    .getCallback(CallbackType.ON_ARGS);
                            if (callback != null) {
                                callback.executeCallback(mUserName);
                            }

                            FragmentManager fm = getFragmentManager();
                            fm.popBackStack();
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        LatteLogger.d("updateName", code);
                        LatteLogger.d("updateName", msg);
                    }
                })
                .build()
                .post();
    }


    public interface MyListerner{
        void sendMessage(String str);
    }




}
