package com.genuly.dou.order.common.util;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckInstanceUtil {

    /**
     * 校验实例
     */
    public static <T> T checkInstance(Object object, Class<T> clazz){
        if (object == null){
            return null;
        }
        T retObject = null;
        try {
            retObject = clazz.cast(object);
        }catch (ClassCastException e){
            log.error("checkInstance exception:" + e.getMessage() + ",object:{}" + object);
        }
        return retObject;
    }
}
