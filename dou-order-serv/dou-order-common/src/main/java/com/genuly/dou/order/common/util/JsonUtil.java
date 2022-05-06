package com.genuly.dou.order.common.util;

import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class JsonUtil {

    /**
     * 根据json字符串获取相应的对象
     *
     * @param json {"promotionCont":6,"unionTrafficType":80,"spreadType":1,"actUnionId":10,"unionId":12}
     * @return
     */
    public static <T> T formatJson2Object(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        T t = null;
        try {
            t = JSON.parseObject(json, clazz);
        } catch (JSONException e) {
            log.error("json exception:{},json:{},e:{}", e.getMessage(), json, e);
        }
        return t;
    }

}