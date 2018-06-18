package com.flj.latte.ec.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 工具类
 * Created by liuguangli on 17/3/7.
 */
public class ToastUtil {
    private static Toast mToast;
    private static String lastContextName = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static String oldMsg;
    public static void show(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            lastContextName = context.getClass().getName();
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            oneTime = System.currentTimeMillis();
            mToast.show();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    mToast.setText(msg);
                    mToast.show();
                }
            } else {
                oldMsg = msg;
                mToast.setText(oldMsg);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            }
        }

    }

    public static void showToast(Context context, String msg, boolean isLong) {
        if (mToast == null || context.getClass().getName().equals(lastContextName)) {
            lastContextName = context.getClass().getName();
            mToast = Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
