package com.flj.latte.app;

/**
 * Created by yb on 2017/4/22
 */

public interface IUserChecker {
    //有用户信息
    void onSignIn();
    //没有用户信息
    void onNotSignIn();
}
