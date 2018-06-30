package com.flj.latte.ec.detail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * 评论信息
 */

public class CommentDataConverter extends DataConverter {
    private int ITEM_COMMENT = 50;

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        if (array == null) return null;
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = array.getJSONObject(i);
            final long addTime = data.getInteger("addTime");
            final float commentRank = data.getFloat("commentRank");
            final String content = data.getString("content");
            String userName = data.getString("userName");
            String imgurls = data.getString("imgUrls");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ITEM_COMMENT)
                    .setField(CommentItemFields.ADDTIME, addTime)
                    .setField(CommentItemFields.CONTENT, content)
                    .setField(CommentItemFields.COMMENTRANK, commentRank)
                    .setField(CommentItemFields.USERNAME, userName)
                    .setField(CommentItemFields.IMGURLS, imgurls)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
