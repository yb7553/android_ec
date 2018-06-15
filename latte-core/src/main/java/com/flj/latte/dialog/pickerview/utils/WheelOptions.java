package com.flj.latte.dialog.pickerview.utils;

import android.view.View;

import com.diabin.latte.R;
import com.flj.latte.bean.AddressBean;
import com.flj.latte.dialog.pickerview.adapter.ArrayWheelAdapter;
import com.flj.latte.dialog.pickerview.lib.WheelView;
import com.flj.latte.dialog.pickerview.listener.OnItemSelectedListener;


import java.util.ArrayList;
import java.util.Map;

/**
 * author: lmj
 * date  : 2017/12/22.
 */

public class WheelOptions {
    private View view;
    private WheelView wv_option1;
    private WheelView wv_option2;
    private WheelView wv_option3;
    private ArrayList<AddressBean> mOptions1Items;
    private ArrayList<AddressBean> mOptions2Items;
    private ArrayList<AddressBean> mOptions3Items;
    private Map<String, ArrayList<AddressBean>> mOptions2Maps;
    private Map<String, ArrayList<AddressBean>> mOptions3Maps;
    private OnItemSelectedListener wheelListener_option1;
    private OnItemSelectedListener wheelListener_option2;

    public View getView() {
        return view;

    }

    public void setView(View view) {
        this.view = view;
    }

    public WheelOptions(View view) {
        super();
        this.view = view;
        setView(view);
    }

    public void setPicker(ArrayList<AddressBean> options1Items,
                          Map<String, ArrayList<AddressBean>> options2Maps, Map<String, ArrayList<AddressBean>> options3Maps) {
        this.mOptions1Items = options1Items;
        this.mOptions2Maps = options2Maps;
        this.mOptions3Maps = options3Maps;
        int len = ArrayWheelAdapter.DEFAULT_LENGTH;

        if (this.mOptions2Maps == null)
            len = 12;
        // 选项1
        wv_option1 = (WheelView) view.findViewById(R.id.select_wheel_1);
        wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 设置显示数据
        wv_option1.setCurrentItem(0);// 初始化时显示的数据
        // 选项2
        wv_option2 = (WheelView) view.findViewById(R.id.select_wheel_2);
        if (mOptions2Maps != null&&mOptions2Maps.size()>0) {
            mOptions2Items = mOptions2Maps.get(mOptions1Items.get(0).id);
            wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items));// 设置显示数据
            wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据
            wv_option2.setVisibility(View.VISIBLE);
        }
        wv_option3 = (WheelView) view.findViewById(R.id.select_wheel_3);
        if (mOptions3Maps != null&&mOptions3Maps.size()>0) {
            mOptions3Items = mOptions3Maps.get(mOptions2Items.get(0).id);
            wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items));// 设置显示数据
            wv_option3.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据
            wv_option3.setVisibility(View.VISIBLE);
        }
        int textSize = 15;

        wv_option1.setTextSize(textSize);
        wv_option2.setTextSize(textSize);
        wv_option3.setTextSize(textSize);

        // 联动监听器
        wheelListener_option1 = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                int opt2Select = 0;
                if (mOptions2Maps != null) {
                    mOptions2Items = mOptions2Maps.get(mOptions1Items.get(index).id);
                    opt2Select = wv_option2.getCurrentItem();//上一个opt2的选中位置
                    //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt2Select = opt2Select >= mOptions2Items.size() - 1 ? mOptions2Items.size() - 1 : opt2Select;

                    wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items));
                    wv_option2.setCurrentItem(opt2Select);
                    if (mOptions3Items != null) {
                        wheelListener_option2.onItemSelected(opt2Select);
                    }
                }
            }
        };
        wheelListener_option2 = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                int opt3Select = 0;
                if (mOptions3Maps != null) {
                    mOptions3Items = mOptions3Maps.get(mOptions2Items.get(index).id);
                    opt3Select = wv_option3.getCurrentItem();//上一个opt2的选中位置
                    //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt3Select = opt3Select >= mOptions3Items.size() - 1 ? mOptions3Items.size() - 1 : opt3Select;

                    wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items));
                    wv_option3.setCurrentItem(opt3Select);
                }
            }
        };
        // 添加联动监听
        if (mOptions2Maps != null)
            wv_option1.setOnItemSelectedListener(wheelListener_option1);
// 添加联动监听
        if (mOptions3Maps != null)
            wv_option2.setOnItemSelectedListener(wheelListener_option2);

    }

    /**
     * 分别设置第一二三级是否循环滚动
     *
     * @param cyclic1,cyclic2
     */
    public void setCyclic(boolean cyclic1, boolean cyclic2) {
        wv_option1.setCyclic(cyclic1);
        wv_option2.setCyclic(cyclic2);

    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    public int[] getCurrentItems() {
        int[] currentItems = new int[3];
        int first = wv_option1.getCurrentItem();
        currentItems[0] = first;
        if (mOptions2Items != null) {
            int second = wv_option2.getCurrentItem();
            currentItems[1] = second;
        }
        if (mOptions3Items != null) {
            int third = wv_option3.getCurrentItem();
            currentItems[2] = third;
        }
        return currentItems;
    }

    /**
     * 设置单前文字
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        wv_option1.setTextSize(textSize);
        wv_option2.setTextSize(textSize);
    }
}
