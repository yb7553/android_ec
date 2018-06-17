package com.diabin.fastec.yanbin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.activities.ProxyActivity;
import com.flj.latte.app.AccountManager;
import com.flj.latte.app.Latte;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.launcher.LauncherSplash;
import com.flj.latte.ec.lbs.presenter.IMainPresenter;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.ec.sign.ISignListener;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.launcher.ILauncherListener;
import com.flj.latte.ui.launcher.OnLauncherFinishTag;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.WeakHashMap;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import qiu.niorgai.StatusBarCompat;

public class ExampleActivity extends ProxyActivity implements
        ISignListener,
        ILauncherListener {


    private IMainPresenter mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Latte.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, true);
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    @Override
    protected void onDestroy() {
        RongIM.getInstance().disconnect();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new LauncherSplash();
        //return new LauncherDelegate();
        //return new EcBottomDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag) {
            case SIGNED:
                Toast.makeText(this, "启动结束，用户登录了", Toast.LENGTH_LONG).show();
                loginByToken();
                getSupportDelegate().startWithPop(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                Toast.makeText(this, "启动结束，用户没登录", Toast.LENGTH_LONG).show();
                getSupportDelegate().startWithPop(new EcBottomDelegate());
                //getSupportDelegate().startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }

    public void loginByToken() {

        // 登录是否过期
        boolean tokenValid = false;

        // 检查token是否过期


        if (LattePreference.getCustomAppProfileLong("expired") > System.currentTimeMillis()) {
            // token 有效
            tokenValid = true;
        }

        if (!tokenValid) {
            //重新用token登录
            Toast.makeText(this, "重新用token登录", Toast.LENGTH_LONG).show();
            final String tokenUrl = API.Config.getDomain() + API.AUTH_TOKEN;
            LatteLogger.d("tokenUrl", tokenUrl);
            final WeakHashMap<String, Object> signin = new WeakHashMap<>();
            signin.put("token",LattePreference.getCustomAppProfile("token"));

            final String jsonString = JSON.toJSONString(signin);

            RestClient.builder()
                    .url(tokenUrl)
                    .raw(jsonString)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            LatteLogger.json("tokenUrl", response);
                            if (JSON.parseObject(response).getInteger("code") != 0) {
                                AccountManager.setSignState(false);
                            }else{
                            final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");

                            final String token = profileJson.getString("token");
                            final long expired = profileJson.getLong("expired");


                            final long userId = profileJson.getJSONObject("user").getLong("id");
                            final String name = profileJson.getJSONObject("user").getString("username");
                            //final String avatar = profileJson.getJSONObject("user").getString("avatar");
                            //final long gender = profileJson.getJSONObject("user").getLong("gender");
                            //final String address = userprofileJson.getString("address");

                            //已经注册并登录成功了
                            AccountManager.setSignState(true);
                            LattePreference.addCustomAppProfile("token", token);
                            LattePreference.addCustomAppProfileLong("expired", expired);
                            LattePreference.addCustomAppProfileLong("userId", userId);
                            LattePreference.addCustomAppProfile("name", name);

                        }
                        }
                    })
                    .build()
                    .post();
        }
        }





}
