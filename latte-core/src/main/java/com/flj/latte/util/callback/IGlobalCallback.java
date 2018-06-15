package com.flj.latte.util.callback;

import android.support.annotation.Nullable;

/**
 * Created by yb
 */

public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
}
