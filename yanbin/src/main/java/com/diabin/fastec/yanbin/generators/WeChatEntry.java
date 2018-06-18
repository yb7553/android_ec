package com.diabin.fastec.yanbin.generators;

import com.flj.latte.annotations.EntryGenerator;
import com.flj.latte.wechat.templates.WXEntryTemplate;

/**
 * Created by yb on 2017/4/22
 */

@SuppressWarnings("unused")
@EntryGenerator(
        packageName = "com.diabin.fastec.yanbin",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
