package com.flj.latte.ec.common.http.api;

/**
 * Created by liuguangli on 17/4/29.
 */

public class API {


    /**
     * TEST信息
     */
    public static final String TEST_GET = "/get?uid=${uid}";
    public static final String TEST_POST = "/post";
    // 下发短信验证码
    public static final String GET_SMS_CODE = "/v2/ecapi.auth.mobile.send";
    //      "/f34e28da5816433d/getMsgCode?phone=${phone}";
    // 校验验证码
    public static final String CHECK_SMS_CODE =
            "/f34e28da5816433d/checkMsgCode?phone=${phone}&code=${code}";
    // 注册
    public static final String REGISTER = "/f34e28da5816433d/register";
    // 登录
    public static final String LOGIN = "/f34e28da5816433d/auth";
    // token 登录
    public static final String LOGIN_BY_TOKEN = "/f34e28da5816433d/login";

    // 检查用户是否存在
    public static String CHECK_USER_EXIST =
            "/f34e28da5816433d/isUserExist?phone=${phone}";
    /**
     * 登录信息
     */
    // 下发短信验证码
    public static final String AUTH_MOBILE_SEND = "/v2/ecapi.auth.mobile.send";
    //验证短信验证码
    public static final String AUTH_MOBILE_VERIFY = "/v2/ecapi.auth.mobile.verify";
    //用户是否已注册
    public static final String AUTH_MOBILE_ISSIGNUP = "/v2/ecapi.auth.mobile.issignup";
    //用户快速注册
    public static final String AUTH_MOBILE_SIGNUP = "/v2/ecapi.auth.mobile.signup";
    //用户登录
    public static final String AUTH_SIGNIN = "/v2/ecapi.auth.signin";
    //token自动登录
    public static final String AUTH_TOKEN = "/v2/ecapi.auth.token";

    /**
     * 地址信息
     */
    //省市区
    public static final String CONFIGNEE_LIST = "/v2/ecapi.consignee.list";
    //选择地址
    public static final String CONFIGNEE_CHOOSE = "/v2/ecapi.consignee.choose";
    //添加
    public static final String CONFIGNEE_ADD = "/v2/ecapi.consignee.add";
    //修改
    public static final String CONFIGNEE_UPDATE = "/v2/ecapi.consignee.update";
    //删除
    public static final String CONFIGNEE_DELETE = "/v2/ecapi.consignee.delete";
    //配送人员列表
    public static final String CONFIGNEE_SEND_LIST = "/v2/ecapi.send.staff.list";
    /**
     * 个人信息
     */
    //用户修改密码
    public static final String USER_PASSWORD_UPDATE = "/v2/ecapi.user.password.update";
    //用户修改昵称
    public static final String USER_NICKNAME_UPDATE = "/v2/ecapi.user.nickname.update";
    //用户修改头像
    public static final String USER_AVATAR_UPDATE = "/v2/ecapi.user.avatar.update";
    //用户个人信息
    public static final String USER_PERSON_LIST = "/v2/ecapi.user.person.list";
    //用户忘记密码修改
    public static final String USER_FORGET_UDPATE = "/v2/ecapi.user.forget.update";
    //用户修改性别
    public static final String USER_SEX_UDPATE = "/v2/ecapi.user.sex.update";

    /**
     * 订单信息
     */
    //订订单-全部,订单-待付款0,订单-待发货1,订单-待收货2,订单-待评价3
    public static final String ORDER_LIST = "/v2/ecapi.order.list";

    //我的-排行榜
    public static final String CHARTS_LIST = "/v2/ecapi.charts.list";


    /**
     * 首页信息
     */
    //精品推荐2，新品上架5，销量排行4
    public static final String PRODUCT_LIST = "/v2/ecapi.product.list";
    //商品详情
    public static final String PRODUCT_DETAIL = "/v2/ecapi.product.detail";
    //分类
    public static final String CATEGORY_LIST = "/v2/ecapi.category.list";
    /**
     * 购物车
     */
    //加入购物车
    public static final String CART_INSERT = "/v2/ecapi.cart.insert";
    //购物清单
    public static final String CART_GET = "/v2/ecapi.cart.get";
    //创建购物车订单
    public static final String CART_CHECKOUT = "/v2/ecapi.cart.checkout";
    //删除购物车商品记录
    public static final String CART_DELETE = "/v2/ecapi.cart.delete";
    //修改购物车商品数量
    public static final String CART_UPDATE = "/v2/ecapi.cart.update";
    //指定配送员
    public static final String SEND_STAFF_LIST = "/v2/ecapi.send.staff.list";
    //保存配送方式
    public static final String SEND_TYPE_UPDATE = "/v2/ecapi.send.type.upate";

    /*评论信息接口*/

    //获取评论信息内容
    public static final String GET_COMMENT_DETAIL = "/v2/ecapi.comment.detail";
    //删除评论
    public static final String DELET_COMMENT = "/v2/ecapi.comment.del";
    //上传评论图片
    public static final String UPLOAD_COMMENT_IMG = "/v2/ecapi.comment.imgUpload";
    //用户评论新增
    public static final String INSERT_COMMENT = "/v2/ecapi.comment.insert";

    /**
     * 支付信息
     */
    //一网通支付/签约数据校验
    public static final String PAY_OR_SIGN = "/payOrSign";
    //一网通支付/签约接口
    public static final String PAY_OR_SIGNURL = "/payOrSignUrl";
    //支付宝接口
    public static final String ALIPAY = "/alipay";
    //微信支付接口
    public static final String wxpay = "/wxpay";


    /**
     * 文章信息
     */
    //发现-文章清单
    public static final String ARTICLE_LIST = "/v2/ecapi.article.list";


    /**
     * 配置域名信息
     */
    public static class Config {
        private static final String TEST_DOMAIN = "http://120.79.230.229/bfwl-mall/calmdown";
        private static final String RElEASE_DOMAIN = "http://120.79.230.229/bfwl-mall/calmdown";
        private static final String TEST_APP_ID = "e90928398db0130b0d6d21da7bde357e";
        private static final String RELEASE_APP_ID = "e90928398db0130b0d6d21da7bde357e";
        private static final String TEST_APP_KEY = "514d8f8a2371bdf1566033f6664a24d2";
        private static final String RELEASE_APP_KEY = "514d8f8a2371bdf1566033f6664a24d2";
        private static String appId = TEST_APP_ID;
        private static String appKey = TEST_APP_KEY;
        private static String domain = TEST_DOMAIN;

        public static void setDebug(boolean debug) {
            domain = debug ? TEST_DOMAIN : RElEASE_DOMAIN;
            appId = debug ? TEST_APP_ID : RELEASE_APP_ID;
            appKey = debug ? TEST_APP_KEY : RELEASE_APP_KEY;
        }

        public static String getDomain() {
            return domain;
        }

        public static String getAppId() {
            return appId;
        }

        public static String getAppKey() {
            return appKey;
        }
    }
}
