package com.flj.latte.ec.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.flj.latte.ec.R;

import java.util.List;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe 评论图片显示适配器
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/30 0030.
 */
public class GoodCommentImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    public GoodCommentImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size() + 1;//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.comment_grid_item, null);
        mImageView = (ImageView) convertView.findViewById(R.id.item);
        if (position < list.size()) {
            LogUtils.e("url:"+list.get(position));
            Glide.with(convertView)
                    .load(list.get(position))
                    .into(mImageView);
            //mImageView.setBackgroundResource();
        } else {
           // mImageView.setBackgroundResource(R.drawable.pic3);//最后一个显示加号图片
        }
        return convertView;
    }

}