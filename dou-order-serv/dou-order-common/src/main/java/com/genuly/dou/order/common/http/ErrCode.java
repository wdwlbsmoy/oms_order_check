package com.genuly.dou.order.common.http;

/**
 * HttpClient ErrCode
 *
 * @author fanxiang 2017/05/16
 */
public enum ErrCode {
    TimeOutException("HttpClientException timeout after retry", 1),
    NullResponseException("HttpClientException response null", 2),
    ClientUnknownException("HttpClientException client known exception", 3);

    private String msg;
    private int code;

    ErrCode(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static String getMsg(int code) {
        for (ErrCode c : ErrCode.values()) {
            if (c.getCode() == code) {
                return c.msg;
            }
        }
        return null;
    }

    // get set 方法
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

}

