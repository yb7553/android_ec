package com.flj.latte.ec.main.discover;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yb
 */

public class DiscoverListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = array.getJSONObject(i);
            final String thumb = data.getString("picurl");
            final String title = data.getString("title");
            final int id = data.getInteger("id");
            final String url = data.getString("url");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            final String time = sdf.format(new Date(Long.parseLong(String.valueOf(data.getInteger("addtime")))));
            //final int time = data.getInteger("addtime");


            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(DiscoverListItemType.ITEM_DISCOVER_LIST)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.IMAGE_URL, thumb)
                    .setField(MultipleFields.TITLE, title)
                    .setField(DiscoverItemFields.URL, url)
                    .setField(DiscoverItemFields.TIME,time)
                    .build();

            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
