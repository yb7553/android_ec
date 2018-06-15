package com.flj.latte.ec.main;

import android.graphics.Color;

import com.flj.latte.delegates.bottom.BaseBottomDelegate;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.delegates.bottom.BottomTabBean;
import com.flj.latte.delegates.bottom.ItemBuilder;
import com.flj.latte.ec.main.cart.ShopCartDelegate;
import com.flj.latte.ec.main.discover.DiscoverDelegate;
import com.flj.latte.ec.main.discover.DiscoverListDelegate;
import com.flj.latte.ec.main.index.IndexDelegate;
import com.flj.latte.ec.main.personal.PersonalDelegate;
import com.flj.latte.ec.main.sort.SortDelegate;

import java.util.LinkedHashMap;

/**
 * Created by yb
 */

public class EcBottomDelegate extends BaseBottomDelegate {

    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{fa-home}", "首页"), new IndexDelegate());
        items.put(new BottomTabBean("{fa-th-large}", "分类"), new SortDelegate());
        items.put(new BottomTabBean("{fa-search}", "发现"), new DiscoverListDelegate());
        items.put(new BottomTabBean("{fa-shopping-cart}", "购物车"), new ShopCartDelegate());
        items.put(new BottomTabBean("{fa-user}", "我的"), new PersonalDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#ff9999");
    }
}
