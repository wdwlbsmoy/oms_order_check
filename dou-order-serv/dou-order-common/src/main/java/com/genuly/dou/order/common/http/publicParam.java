package com.genuly.dou.order.common.http;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class publicParam {

    public static RequestParams getParam(String appKey, String appSecret, String accessToken){
        //获取公共参数
        RequestParams param = new RequestParams();
        param.setAppKey(appKey);
        param.setAppSecret(appSecret);
        param.setAccessToken(accessToken);
        param.setV("2");
        return param;
    }
}
