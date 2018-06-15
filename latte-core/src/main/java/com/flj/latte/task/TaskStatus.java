package com.flj.latte.task;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;

/**
 * 具有状态属性的任务
 */
public abstract class TaskStatus implements Runnable {
    /**
     * 任务状态的定义
     */
    public enum Status {
        /**
         * 未执行
         */
        READY,
        /**
         * 正在执行
         */
        DOING,
        /**
         * 执行完成
         */
        DONE,
        /**
         * 执行期间发生了错误
         */
        ERROR,
        /**
         * 任务被取消
         */
        CANCEL
    }

    /**
     * 当前状态
     */
    protected Status mStatus = Status.READY;
    /***
     * 任务回调
     */
    private TaskCallback mCallback;

    @Override
    public void run() {
        try {
            setStatus(Status.DOING);
            execute();
            setStatus(Status.DONE);
        } catch (Exception ex) {
            ex.printStackTrace();
            setStatus(Status.ERROR);
            handleException(ex);
        }
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        synchronized (mStatus) {
            this.mStatus = mStatus;
        }
    }

    /**
     * 获取当前的回调对象
     *
     * @return
     */
    public TaskCallback getCallback() {
        return mCallback;
    }

    /**
     * 设置任务回调，覆盖原先回调
     *
     * @param callback
     */
    public void setCallback(TaskCallback callback) {
        mCallback = callback;
    }

    /**
     * 处理回调
     *
     * @param data 回调数据
     */
    protected void handleCallback(final Object data) {
        handleCallback(data, null);
    }

    /**
     * 处理回调
     *
     * @param data
     * @param exception
     */
    protected void handleCallback(final Object data, final Exception exception) {
        if (mCallback == null) {
            return;
        }
        try {
            if (exception != null) {
                executeMethod(mCallback, "onFailed", exception);
            } else if (data != null) {
                executeMethod(mCallback, "onSuccess", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeMethod(Object provider, String methodName, Object... params) throws Exception {
        Method[] methods = provider.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                InvokeDelegate invoker = new InvokeDelegate(provider, method, params);
                //线程切换
                CallbackThreadMode callback = method.getAnnotation(CallbackThreadMode.class);
                if (callback != null && !callback.runInMainThread()) {
                    invoker.execute();
                } else {
                    runCallbackInMainThread(invoker);
                }

                break;
            }
        }
    }

    private void runCallbackInMainThread(final InvokeDelegate delegate) {
        //根据回调的声明，判断线程
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    delegate.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 方法的调用代理
     */
    private static class InvokeDelegate {
        private Method mMethod;
        private Object[] mParams;
        private Object mService;

        public InvokeDelegate(Object service, Method method, Object... params) {
            mService = service;
            mMethod = method;
            mParams = params;
        }

        public void execute() throws Exception {
            mMethod.invoke(mService, mParams);
        }
    }

    /**
     * 执行任务
     */
    protected abstract void execute() throws Exception;

    /**
     * 处理异常
     *
     * @param error
     */
    protected void handleException(Exception error) {
        handleCallback(null, error);
    }

    /**
     * 任务是否执行中
     *
     * @return
     */
    public boolean isExecuting() {
        return mStatus == Status.DOING;
    }

    /**
     * 取消任务
     */
    public void cancel() {
        setStatus(Status.CANCEL);
    }

    public void reset() {
        setStatus(Status.READY);
    }
}
