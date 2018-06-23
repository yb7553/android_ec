package com.flj.latte.ui.timepicker;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe 日期选择器
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/22 0022.
 */
public class TimePickerUtils {
    private Context mContext;
    private TextInputEditText editText;

    public TimePickerUtils(Context context, TextInputEditText editText) {
        this.mContext = context;
        this.editText = editText;
    }

    public TimePickerUtils(Context context) {
        this.mContext = context;
    }

    public void setTextInputEditText(TextInputEditText editText) {
        this.editText = editText;
    }

    private void init() {

    }

    public TimePickerView showTimePicker() {
        //时间选择器
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
        endDate.set(2100, 11, 31);
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Toast.makeText(mContext, date.getTime() + "", Toast.LENGTH_SHORT).show();
            }
        })
                .setType(new boolean[]{false, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFf5f5f5)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel(null, "月", "日", "时", "分", null)//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
        return pvTime;
    }

    private ArrayList<TimeBean> options1Items = new ArrayList<>();
    private ArrayList<SendPersonBean> options4Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    private boolean initSelectFlag = false;

    public void showSelectDialog(List<MultipleItemEntity> list) {
        //  options1Items
        if (!initSelectFlag) {
            for (MultipleItemEntity entity : list) {
                options4Items.add(new SendPersonBean(entity.getField(MultipleFields.NAME)));
            }
            initSelectFlag = true;
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options4Items.get(options1).getPickerViewText();
                sendID = list.get(options1).getField(MultipleFields.ID);
                sendName = tx;
                editText.setText(tx);
            }
        })
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        //String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        //  Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        pvOptions.setPicker(options4Items);//一级选择器
        pvOptions.show();
    }

    public void showTimeDialog() {
        options1Items = getDayOfMonth();

        //22
        ArrayList<String> options2Items_02 = getTodayHourData();
        options2Items.add(options2Items_02);
        for (int i = 1; i < options1Items.size(); i++) {
            //32
            ArrayList<String> options2Items_03 = getHourData();
            options2Items.add(options2Items_03);
        }
        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        options3Items_01 = getmD2();
        options3Items.add(options3Items_01);
        for (int i = 1; i < options2Items.size(); i++) {
            options3Items_02 = getmD();
            options3Items.add(options3Items_02);

        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                int year = formatYear(options1Items.get(options1).getPickerViewText());
                String timeShow = year + "年" +
                        options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) + "点" +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText() + "分";
                selectTime = timeShow;
                editText.setText(timeShow);
                String time = year +
                        options1Items.get(options1).getPickerViewText().replace("月", "").replace("日", "") +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();

                SimpleDateFormat fomat = new SimpleDateFormat("yyyyMMddHHmm");
                Date date = null;
                try {
                    date = fomat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sendtime = date.getTime();
            }
        })
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1, 1)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "点", "分")
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        //String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        //  Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private int formatYear(String day) {
        int moth = Integer.valueOf(day.substring(0, 2));
        Calendar calendar = Calendar.getInstance();
        if (moth < (calendar.get(Calendar.MONTH) + 1)) {
            return calendar.get(Calendar.YEAR) + 1;
        }
        return calendar.get(Calendar.YEAR);
    }

    private long sendtime = 0;

    public long getSendTime() {
        return sendtime;
    }

    private String selectTime;

    public String getSelectTime() {
        return selectTime;
    }

    private String sendName;

    public String getSendName() {
        return sendName;
    }

    private String sendID;

    public String getSendID() {
        return sendID;
    }

    private ArrayList<TimeBean> getDayOfMonth() {
        ArrayList<TimeBean> listTime = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String start = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        String end = (calendar.get(Calendar.YEAR) + 1) + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fomat = new SimpleDateFormat("MM月dd日EEEE");
        SimpleDateFormat fomattoday = new SimpleDateFormat("MM月dd日");
        Date dBegin = null;
        Date dEnd = null;
        try {
            dBegin = sdf.parse(start);
            dEnd = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Date> listDate = getDatesBetweenTwoDate(dBegin, dEnd);
        for (int i = 0; i < listDate.size(); i++) {
            //   System.out.println(fomat.format(listDate.get(i)));
            if (i == 0) {
                listTime.add(new TimeBean(fomattoday.format(listDate.get(i)) + "今天"));
                fomattoday = null;
            } else {
                listTime.add(new TimeBean(fomat.format(listDate.get(i))));
            }
        }

        return listTime;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }

    /**
     * 今天 点
     */
    private ArrayList<String> getTodayHourData() {
        int max = currentHour();
        if (max < 23 && currentMin() > 45) {
            max = max + 1;
        }
        ArrayList<String> lists = new ArrayList<>();
        for (int i = max; i < 24; i++) {
            lists.add(i + "");
        }
        return lists;
    }

    /**
     * 明天 后天 点
     */
    private ArrayList<String> getHourData() {
        ArrayList<String> lists = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            lists.add(i + "");
        }
        return lists;
    }

    /**
     * 明天 后天  分
     */
    private ArrayList<IPickerViewData> getMinData() {
        ArrayList<IPickerViewData> dataArrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dataArrayList.add(new PickerViewData((i * 10) + ""));
        }
        return dataArrayList;
    }

    /**
     * 明天 后天
     */
    private ArrayList<ArrayList<IPickerViewData>> getmD() {
        ArrayList<ArrayList<IPickerViewData>> d = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            d.add(getMinData());
        }
        return d;
    }

    /**
     * 明天 后天  2222
     */
    private ArrayList<ArrayList<IPickerViewData>> getmD2() {
        //14
        int max = currentHour();
        if (currentMin() > 45) {
            max = max + 1;
        }
        int value = 24 - max;
        ArrayList<ArrayList<IPickerViewData>> d = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            if (i == 0) {
                d.add(getTodyMinData());
            } else {
                d.add(getMinData());
            }

        }
        return d;
    }

    /**
     * 明天 后天  分2222
     */
    private ArrayList<IPickerViewData> getTodyMinData() {

        int min = currentMin();
        int current = 0;
        if (min > 35 && min <= 45) {
            current = 0;
        } else if (min > 45 && min <= 55) {
            current = 1;
        } else if (min > 55) {
            current = 2;
        } else if (min <= 5) {
            current = 2;
        } else if (min > 5 && min <= 15) {
            current = 3;
        } else if (min > 15 && min <= 25) {
            current = 4;
        } else if (min > 25 && min <= 35) {
            current = 5;
        }
        int max = currentHour();
        if (max > 23 && min > 35) {
            current = 5;
        }

        ArrayList<IPickerViewData> dataArrayList = new ArrayList<>();
        for (int i = current; i < 6; i++) {
            dataArrayList.add(new PickerViewData((i * 10) + ""));
        }
        return dataArrayList;
    }

    private int currentMin() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }


    private int currentHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }
}
