package com.genuly.dou.order.common.http;

import java.io.IOException;

public class HttpClientException extends IOException {

    private static final long serialVersionUID = 1L;

    public HttpClientException(ErrCode code) {
        super(code.getMsg());
    }

    public HttpClientException(String msg) {
        super(msg);
    }

    public HttpClientException(ErrCode code, IOException e) {
        super(code.getMsg(), e);
    }
}
