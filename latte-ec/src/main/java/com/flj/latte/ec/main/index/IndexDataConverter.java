package com.flj.latte.ec.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.ItemType;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by yb
 */

public final class IndexDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();


        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);


            ///final String imageUrl = data.getString("imageUrl");

            final String imageUrl = data.getJSONObject("default_photo").getString("large");
            ///final String text = data.getString("text");
            final String text = data.getString("name");
            final String price = data.getString("current_price");
            ///final int spanSize = data.getInteger("spanSize");
            int spanSize = 2;
            ///final int id = data.getInteger("goodsId");
            final int id = data.getInteger("id");
            final JSONArray banners = data.getJSONArray("banners");

            final ArrayList<String> bannerImages = new ArrayList<>();
            int type = 0;
            if (imageUrl == null && text != null) {
                type = ItemType.TEXT;
            } else if (imageUrl != null && text == null) {
                type = ItemType.IMAGE;
            } else if (imageUrl != null&&id!=97) {
                type = ItemType.TEXT_IMAGE;
            } else if (banners != null&&id==97) {
                type = ItemType.BANNER;
                spanSize = 4;
                //Banner的初始化
                final int bannerSize = banners.size();
                for (int j = 0; j < bannerSize; j++) {
                    final String banner = banners.getString(j);
                    bannerImages.add(banner);
                }
            }

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE,type)
                    .setField(MultipleFields.SPAN_SIZE,spanSize)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.TEXT,text)
                    .setField(MultipleFields.IMAGE_URL,imageUrl)
                    .setField(MultipleFields.BANNERS,bannerImages)
                    .setField(MultipleFields.TAG,price)
                    .build();

            ENTITIES.add(entity);

        }



        return ENTITIES;
    }
}
