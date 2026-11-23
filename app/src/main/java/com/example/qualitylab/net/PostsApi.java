package com.example.qualitylab.net;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/** Сетевой слой: загрузка постов (Network Inspector, MockWebServer). */
public class PostsApi {

    public static final String DEFAULT_BASE_URL = "https://jsonplaceholder.typicode.com/";

    private final HttpUrl baseUrl;
    private final OkHttpClient client;

    public PostsApi(String baseUrl, OkHttpClient client) {
        this.baseUrl = HttpUrl.get(baseUrl);
        this.client = client;
    }

    public List<Post> fetchPosts(int limit) throws ApiException {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("posts")
                .addQueryParameter("_limit", String.valueOf(limit))
                .build();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), "HTTP " + response.code());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new ApiException(-2, "Пустой ответ сервера");
            }
            return PostParser.parse(body.string());
        } catch (IOException e) {
            throw new ApiException(-1, "Сетевая ошибка: " + e.getMessage(), e);
        }
    }
}
