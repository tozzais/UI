package com.gc.ui.http;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lizetong on 15/8/27.
 */
public class OkHttpUtils {

    private final static long HTTP_CONNECT_TIMEOUT = 10;
    private final static long HTTP_READ_TIMEOUT = 30;

    private static OkHttpClient singleton;



    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    //定义logging
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(interceptor)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request.Builder builder = chain.request().newBuilder();
                                    Request request;
//                                    String userToken = GlobalParam.getUserToken();
//                                    if (!TextUtils.isEmpty(userToken)){
//                                        builder.addHeader("token",userToken);
//                                    }
                                    request = builder.build();
                                    return chain.proceed(request);
                                }
                            });
                    singleton = builder.build();
                }
            }
        }
        return singleton;
    }
}
