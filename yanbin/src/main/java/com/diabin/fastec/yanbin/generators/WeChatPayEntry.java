package com.diabin.fastec.yanbin.generators;

import com.flj.latte.annotations.PayEntryGenerator;
import com.flj.latte.wechat.templates.WXPayEntryTemplate;

/**
 * Created by yb on 2017/4/22
 */
@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = "com.diabin.fastec.yanbin",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
