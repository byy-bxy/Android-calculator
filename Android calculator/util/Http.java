package com.example.a111.util; // 必须与文件夹路径一致

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;

public class Http {
    private static final OkHttpClient client = new OkHttpClient();

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}