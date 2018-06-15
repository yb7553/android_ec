package com.flj.latte.task;

/**
 * 申明回调在是否在主线程中运行
 */
public @interface CallbackThreadMode {
    /**
     * 默认是在主线程中运行
     * @return
     */
    boolean runInMainThread() default true;
}
