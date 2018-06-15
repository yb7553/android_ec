package com.flj.latte.task;

/**
 * 任务回调
 */
public interface TaskCallback<T> {
    void onSuccess(T data);
    void onFailed(Exception ex);
}
