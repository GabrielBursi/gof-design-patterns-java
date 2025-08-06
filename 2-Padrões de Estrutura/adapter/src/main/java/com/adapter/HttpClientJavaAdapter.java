package com.adapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientJavaAdapter implements HttpClient {

    @Override
    public String get(String urlString) {
        try {
            var url = new URL(urlString);
            var conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            var reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            var response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException("GET request failed: " + e.getMessage());
        }
    }

    @Override
    public String post(String url, String body) {
        throw new UnsupportedOperationException("POST not implemented yet");
    }
}
