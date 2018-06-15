package com.flj.latte.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/15 0015.
 */
public class ThreadPoolFactory {
    static ThreadPoolProxy mPool;
    /**得到一个普通的线程池*/
    public static ThreadPoolProxy getThreadPool() {
        if (mPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mPool == null) {
                    mPool = new ThreadPoolProxy(4, 4, 3000);
                }
            }
        }
        return mPool;
    }

    private static ExecutorService mExecutorService;

    /**得到一个的线程池*/
    public static ExecutorService getSingleThread() {
        if (mExecutorService == null) {
            synchronized (ExecutorService.class) {
                if (mExecutorService == null) {
                    mExecutorService = Executors.newSingleThreadExecutor();
                }
            }
        }
        return mExecutorService;
    }
}
