package com.xujiacheng.avmooviewer.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetRequest {
    private static OkHttpClient okHttpClient;
    private static InternetRequest INSTANCE;

    private InternetRequest() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .build();
        }
    }

    public static InternetRequest getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InternetRequest();
        }
        return INSTANCE;
    }

    /*
    user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Mobile Safari/537.36
     */
    public String getHTML(String url) {
        String html = null;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Mobile Safari/537.36")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200) {
                html = new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException ignored) {

        }
        return html;
    }
}
