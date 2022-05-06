package com.genuly.dou.order.common.rpc;

public class DouTik {

    /**
     * @des 抖音接口
     */
    public static final String TIK_TOK_API_ERROR = "TikTokApiError";
    public static final String TOKEN_AUTH = "oauth2.access_token";
    public static final String TOKEN_CREATE = "token.create";
    public static final String TOKEN_REFRESH = "token.refresh";
    //订单详情查询
    public static final String ORDER_DETAIL = "order.orderDetail";
    //新版订单流水明细接口
    public static final String ORDER_SETTLE_BILL = "order.getSettleBillDetailV2";
    //资金流水明细接口
    public static final String ORDER_SETTLE_SHOP_ACCOUNT_ITEM = "order.getShopAccountItem";
    //根据订单ID查询运费险
    public static final String ORDER_INSURANCE = "order.insurance";
    //获取店铺商品的类目
    public static final String SHOP_SHOPCATEGORY = "shop.getShopCategory";
    //查询订单列表
    public static final String ORDER_SEARCHLISRT = "order.searchList";

}
