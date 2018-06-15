package com.flj.latte.app;

import com.flj.latte.util.storage.LattePreference;

/**
 * Created by yb on 2017/4/22
 */

public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }

    //保存用户登录状态，登录后调用
    public static void setSignState(boolean state) {
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    private static boolean isSignIn() {
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {
            //如果已登录，执行登录回调
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}
