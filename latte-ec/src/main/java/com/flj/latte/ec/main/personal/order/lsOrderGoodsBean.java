package com.flj.latte.ec.main.personal.order;

/**
 * Copyright (c) 2018. cq Inc. All rights reserved.
 * Down, kageyuki anchor. Though not to, the heart yearning.
 *
 * @Describe com.flj.latte.ec.main.personal.order.
 * @Notice
 * @Author Administrator.
 * @Date 2018/7/14 0014.
 */
public class lsOrderGoodsBean {
    private String goodsAttr;
    private int goodsId;
    private String goodsName;
    private int goodsNum;
    private String goodsPic;
    private double goodsPrice;
    public void setGoodsattr(String goodsattr) {
        this.goodsAttr = goodsattr;
    }
    public String getGoodsattr() {
        return goodsAttr;
    }

    public void setGoodsid(int goodsid) {
        this.goodsId = goodsid;
    }
    public int getGoodsid() {
        return goodsId;
    }

    public void setGoodsname(String goodsname) {
        this.goodsName = goodsname;
    }
    public String getGoodsname() {
        return goodsName;
    }

    public void setGoodsnum(int goodsnum) {
        this.goodsNum = goodsnum;
    }
    public int getGoodsnum() {
        return goodsNum;
    }

    public void setGoodspic(String goodspic) {
        this.goodsPic = goodspic;
    }
    public String getGoodspic() {
        return goodsPic;
    }

    public void setGoodsprice(double goodsprice) {
        this.goodsPrice = goodsprice;
    }
    public double getGoodsprice() {
        return goodsPrice;
    }



}
