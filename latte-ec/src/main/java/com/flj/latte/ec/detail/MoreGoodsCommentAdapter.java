package com.flj.latte.ec.detail;

import android.support.v7.widget.AppCompatTextView;
import android.widget.GridView;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;
import com.flj.latte.ui.widget.StarBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe 详情适配器, 需要显示图片
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/28 0028.
 */
public class MoreGoodsCommentAdapter extends MultipleRecyclerAdapter {
    private final int ITEM_COMMENT = 50;
    LatteDelegate delegate;

    protected MoreGoodsCommentAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        this.delegate = delegate;
        addItemType(ITEM_COMMENT, R.layout.item_more_comment);
    }

    @Override
    protected void convert(final MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case ITEM_COMMENT:
                final String name = entity.getField(CommentItemFields.USERNAME);
                float starRank = entity.getField(CommentItemFields.COMMENTRANK);
                String content = entity.getField(CommentItemFields.CONTENT);
                long date = entity.getField(CommentItemFields.ADDTIME);
                String imgUrls = entity.getField(CommentItemFields.IMGURLS);
                final AppCompatTextView nameText = holder.getView(R.id.tv_comment_name);
                final StarBar starBar = holder.getView(R.id.tv_comment_star);
                final AppCompatTextView tv_comment = holder.getView(R.id.tv_comment);
                final AppCompatTextView tv_comment_date = holder.getView(R.id.tv_comment_date);
                final GridView gv_image = holder.getView(R.id.gv_image);
                nameText.setText(name);
                starBar.setStarMark(starRank);
                tv_comment.setText(content);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    tv_comment_date.setText(format.format(new Date(date)));
                } catch (Exception e) {
                    tv_comment_date.setText("");
                }
                List<ImageUrlBean> list = new Gson().fromJson(imgUrls, new TypeToken<List<ImageUrlBean>>() {
                }.getType());
                final List<String> images = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    images.add(list.get(i).getPath());
                }
                //加载图片
                GoodCommentImageAdapter adapter = new GoodCommentImageAdapter(holder.getConvertView().getContext(), images);
                gv_image.setAdapter(adapter);
                break;
            default:
                break;
        }
    }
}
