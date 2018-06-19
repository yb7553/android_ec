package com.shoppingselect;


import java.util.List;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe bean
 * @Notice
 * @Author Administrator.
 * @Date 2018/6/19 0019.
 */
public class BigClassification {
    //大分类
    private String name;
    public List<SmallClassification> atts;
    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SmallClassification> getList() {
        return atts;
    }

    public void setList(List<SmallClassification> atts) {
        this.atts = atts;
    }

    //小分类
    public static class SmallClassification {
        private String attr_name;
        private int id;
        private String attr_price;
        private boolean select=false;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getAttr_price() {
            return attr_price;
        }

        public void setAttr_price(String attr_price) {
            this.attr_price = attr_price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return attr_name;
        }

        public void setName(String attr_name) {
            this.attr_name = attr_name;
        }
    }
}
