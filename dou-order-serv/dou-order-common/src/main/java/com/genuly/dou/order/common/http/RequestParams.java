package com.genuly.dou.order.common.http;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @des 参见文档 https://op.jinritemai.com/docs/guide-docs/10/23
 **/
@Slf4j
@Data
public class RequestParams {

    /**
     * 线上正式环境URL
     */
    private static final String TIK_TOK_URL = "https://openapi-fxg.jinritemai.com";

    private String accessToken;
    private String appSecret;
    private String method;
    private String appKey;
    private Map<String, Object> paramJson;
    private String v;


    /**
     * 获取paramJson的字符串
     *
     * @return
     */
    public String getParamBody() {
        return Objects.isNull(this.paramJson) ? "" : "" + JSON.toJSONString(paramJson);
    }

    private String getBaseUrl() {
        String timestamp = getTimestamp();
        return TIK_TOK_URL + getPath()
                + "?app_key=" + appKey
                + "&method=" + method
                + "&timestamp=" + timestamp
                + "&v=" + v
                + "&sign_method=hmac-sha256"
                + "&sign=" + getSign(timestamp);
    }

    public String getAccessTokenUrl() {
        return getBaseUrl() + "&param_json=" + getParamBody();
    }

    public String getUrl() {
        return postUrl() + "&param_json=" + getParamBody();
    }

    public String postUrl() {
        return getBaseUrl() + "&access_token=" + this.accessToken;
    }


    /**
     * 计算签名
     *
     * @param timestamp 时间戳
     * @return 签名
     */
    private String getSign(String timestamp) {

        Map<String, String> map = new TreeMap<>();
        map.put("app_key", appKey);
        map.put("method", method);
        map.put("param_json", JSON.toJSONString(paramJson));
        map.put("timestamp", timestamp);
        map.put("v",v);

        StringBuilder paramPattern = new StringBuilder();
        map.entrySet().iterator().forEachRemaining(
                item -> paramPattern.append(item.getKey()).append(item.getValue()));
        if (log.isDebugEnabled()) {
            log.debug("paramPattern : {}", paramPattern);
        }

        String signPattern = appSecret + paramPattern + appSecret;
        if (log.isDebugEnabled()) {
            log.debug("appSecret : {} , signPattern", appSecret, signPattern);
        }

        return hmac(signPattern, appSecret);
    }


    /**
     * 计算hmac
     *
     * @param plainText 加密内容
     * @param appSecret 密钥
     * @return 使用hmac加密后的字符串
     */
    private String hmac(String plainText, String appSecret) {
        Mac mac;
        try {
            byte[] secret = appSecret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(secret, "HmacSHA256");

            mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return "";
        }

        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] digest = mac.doFinal(plainBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b: digest) {
            sb.append(String.format("%02x", b));
        }

        if (log.isDebugEnabled()) {
            log.debug("sign: >>> {}", sb);
        }
        return String.valueOf(sb);
    }

    private String getTimestamp() {
        return DateUtil.formatDateTime(new Date());
    }

    private String getPath() {
        String path = method;
        if (method.contains(".")) {
            path = method.replaceAll("\\.", "/");
        }
        return "/" + path;
    }


    /**
     * 测试店铺授权接口与正式环境不一样
     *
     * @return
     */
    public String getSandBoxTokenUrl() {

        return TIK_TOK_URL + getPath()
                + "?app_id=" + appKey
                + "&app_secret=" + appSecret
                + "&grant_type=authorization_self&test_shop=1";
    }

}

