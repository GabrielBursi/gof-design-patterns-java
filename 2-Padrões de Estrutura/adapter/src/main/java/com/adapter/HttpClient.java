package com.adapter;

public interface HttpClient {
    String get(String url);
    String post(String url, String body);
}
