package com.diabin.fastec.yanbin;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diabin.fastec.yanbin.event.ShareEvent;
import com.diabin.fastec.yanbin.event.TestEvent;
import com.flj.latte.app.Latte;
import com.flj.latte.ec.MyTaxiApplication;
import com.flj.latte.ec.icon.FontEcModule;
import com.flj.latte.net.interceptors.DebugInterceptor;
import com.flj.latte.net.rx.AddCookieInterceptor;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.flj.latte.util.callback.IGlobalCallback;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by yb on 2017/3/29
 */
public class ExampleApp extends Application implements RongIMClient.OnReceiveMessageListener {


    private static ExampleApp instance;


    @Override
    public void onCreate() {
        super.onCreate();

        instance  = this;

        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withLoaderDelayed(1000)
                .withApiHost("http://www.bfhmall.com/RestServer/api/")
                //.withApiHost("http://192.168.31.232:8080/RestServer/api/")
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .withWeChatAppId("你的微信AppKey")
                .withWeChatAppSecret("你的微信AppSecret")
                .withJavascriptInterface("latte")
                .withWebEvent("test", new TestEvent())
                .withWebEvent("share", new ShareEvent())
                //添加Cookie同步拦截器
                .withWebHost("https://www.baidu.com/")
                .withInterceptor(new AddCookieInterceptor())
                .configure();


        /**
         * OnCreate 会被多个进程重入，这段保护代码，
         * 确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            //IMKit SDK调用第一步 初始化
            RongIM.init(this);
        }
        RongIM.setOnReceiveMessageListener(this);







        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (JPushInterface.isPushStopped(Latte.getApplicationContext())) {
                            //开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(Latte.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (!JPushInterface.isPushStopped(Latte.getApplicationContext())) {
                            JPushInterface.stopPush(Latte.getApplicationContext());
                        }
                    }
                });
    }

    public static ExampleApp getInstance() {
        return  instance;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        Log.d("TAG","接受到消息------》onReceived"+message.getTargetId()+"--"+message.getContent());
//        Intent intent = new Intent(this,ReceiveActivity.class);
//        String pushData = message.getTargetId();
//        intent.putExtra("pushData",pushData);
//        startActivity(intent);
        return false;
    }

}
