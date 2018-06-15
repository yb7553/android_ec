package com.flj.latte.ec.main.discover;

import android.annotation.SuppressLint;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flj.latte.ec.R;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * Created by yb
 */

public class DiscoverListAdapter extends MultipleRecyclerAdapter {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    protected DiscoverListAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(DiscoverListItemType.ITEM_DISCOVER_LIST, R.layout.item_discover_list);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case DiscoverListItemType.ITEM_DISCOVER_LIST:
                final AppCompatImageView imageView = holder.getView(R.id.image_discover_list);
                final AppCompatTextView title = holder.getView(R.id.tv_discover_list_title);
                //final AppCompatTextView price = holder.getView(R.id.tv_discover_list_price);
                final AppCompatTextView time = holder.getView(R.id.tv_discover_list_time);

                final String titleVal = entity.getField(MultipleFields.TITLE);
                final String timeVal = entity.getField(DiscoverItemFields.TIME);
                //final double priceVal = entity.getField(DiscoverItemFields.PRICE);
                final String imageUrl = entity.getField(MultipleFields.IMAGE_URL);
                final String url = entity.getField(DiscoverItemFields.URL);

                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(OPTIONS)
                        .into(imageView);

                title.setText(titleVal);
                title.setTag(url);
                //price.setText("价格：" + String.valueOf(priceVal));
                time.setText(timeVal);
                break;
            default:
                break;
        }
    }
}
