package com.diabin.fastec.yanbin.generators;

import com.flj.latte.annotations.AppRegisterGenerator;
import com.flj.latte.wechat.templates.AppRegisterTemplate;

/**
 * Created by yb on 2017/4/22
 */
@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "com.diabin.fastec.yanbin",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
