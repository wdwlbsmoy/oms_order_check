package com.genuly.dou.order.common.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class HttpClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    private volatile static HttpClient instance;

    private HttpClient(long connectTimeOut, long readTimeOut, int maxRetryTimes, int poolSize, int maxRequest, int maxRequestPerHost) {
        ConnectionPool connectionPool = new ConnectionPool(poolSize, 1, TimeUnit.MINUTES);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequest);
        dispatcher.setMaxRequestsPerHost(maxRequestPerHost);
        client = new OkHttpClient.Builder()
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(true)
                .addInterceptor(new RetryInterceptor(maxRetryTimes))
                .dispatcher(dispatcher)
                .build();
    }

    public static HttpClient getInstance(long connectTimeOut, long readTimeOut, int maxRetryTimes, int poolSize, int maxRequest, int maxRequestPerHost) {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient(connectTimeOut, readTimeOut, maxRetryTimes, poolSize, maxRequest, maxRequestPerHost);
                }
            }
        }
        return instance;
    }

    public static HttpClient getDefaultInstance() {
        return getInstance(500, 500, 3, 128, 512, 128);
    }

    public String get(String url) throws HttpClientException {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    public String post(String url, String json) throws HttpClientException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return execute(request);

    }

    private String execute(Request request) throws HttpClientException {
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
            return null;
        } catch (HttpClientException e) {
            log.error("HttpClient HttpClientException", e);
            throw new HttpClientException(ErrCode.NullResponseException, e);
        } catch (IOException e) {
            log.error("HttpClient IOException", e);
            throw new HttpClientException(ErrCode.ClientUnknownException, e);
        }
    }

    /**
     * RetryInterceptor
     */
    private static class RetryInterceptor implements Interceptor {
        public int maxRetry;

        public RetryInterceptor(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @SuppressWarnings("resource")
        @Override
        public Response intercept(Chain chain) throws IOException {
            int retryNum = 0;
            Request request = chain.request();
            Response response = process(chain, request);
            while (retryNum < maxRetry && (response == null || !response.isSuccessful())) {
                response = process(chain, request);
                retryNum++;
            }
            if (response == null) {
                throw new HttpClientException(ErrCode.NullResponseException);
            }
            return response;
        }

        private Response process(Chain chain, Request request) throws IOException {
            Response response = null;
            try {
                response = chain.proceed(request);
            } catch (java.net.SocketTimeoutException e) { // catch timeout exception
                log.error("HttpClient IOException", e);
            }
            return response;
        }
    }
}

