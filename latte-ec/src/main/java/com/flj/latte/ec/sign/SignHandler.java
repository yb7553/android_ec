package com.flj.latte.ec.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.app.AccountManager;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

/**
 * Created by yb on 2017/4/22
 */

public class SignHandler {

    public static void onSignIn(String response, ISignListener signListener) {


        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final String token = profileJson.getString("token");
        final long expired = profileJson.getLong("expired");



        final long userId = profileJson.getJSONObject("user").getLong("id");
        final String name = profileJson.getJSONObject("user").getString("username");
        final String nickname = profileJson.getJSONObject("user").getString("nickname");
        //final String avatar = profileJson.getJSONObject("user").getString("avatar");
        //final long gender = profileJson.getJSONObject("user").getLong("gender");
        //final String address = profileJson.getJSONObject("user").getString("address");

        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignInSuccess();
        LattePreference.addCustomAppProfile("token",token);
        LattePreference.addCustomAppProfileLong("expired",expired);
        LattePreference.addCustomAppProfileLong("userId",userId);
        LattePreference.addCustomAppProfile("name",name);
        LattePreference.addCustomAppProfile("nickname",nickname);
        //LattePreference.addCustomAppProfile("avatar",avatar);
        //LattePreference.addCustomAppProfileLong("gender",gender);

    }


    public static void onSignUp(String response, ISignListener signListener) {
        //final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        //final String token = profileJson.getString("token");
        //final long expired = profileJson.getLong("expired");



        //final long userId = profileJson.getJSONObject("user").getLong("id");
        //final String name = profileJson.getJSONObject("user").getString("username");
        //final String avatar = profileJson.getString("avatar");
        //final String gender = profileJson.getString("gender");
        //final String address = profileJson.getString("address");

        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignUpSuccess();
    }
}
