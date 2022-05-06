package com.genuly.dou.order.common.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public abstract class AbstractHttpRequest {


    protected String getResponse(String key, String url) {
        String response = StringUtils.EMPTY;
        try {
            response = HttpClient.getDefaultInstance().get(url);
        } catch (Exception e) {
            log.error("HTTP GET 接口异常 url:{}", url, e);
        }
        return parseResponse(key, response);
    }

    protected String postResponse(String key, String url, String body) {
        String response = StringUtils.EMPTY;
        try {
            response = HttpClient.getDefaultInstance().post(url, body);
        } catch (Exception e) {
            log.error("HTTP POST 接口异常 url:{}", url, e);
        }
        return parseResponse(key, response);
    }

    private static String parseResponse(String key, String response) {
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        int errorNo = jsonObject.getIntValue("err_no");
        if (errorNo != 0) {
            String msg = jsonObject.getString("message");
            log.error("method:{},response:{}", key, response);
            return null;
        }
        return jsonObject.getString("data");
    }
}

