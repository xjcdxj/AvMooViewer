package com.xujiacheng.avmooviewer.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetRequest {
    private static OkHttpClient okHttpClient;

    public InternetRequest() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .build();
        }
    }

    public String getHTML(String url) {
        String html = null;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200) {
                html = new String(response.body().bytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            return html;
        }
        return html;
    }
}
