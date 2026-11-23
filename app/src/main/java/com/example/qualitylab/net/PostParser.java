package com.example.qualitylab.net;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.Arrays;
import java.util.List;

public final class PostParser {

    private static final Gson GSON = new Gson();

    private PostParser() {
    }

    public static List<Post> parse(String json) throws ApiException {
        try {
            Post[] posts = GSON.fromJson(json, Post[].class);
            if (posts == null) {
                throw new ApiException(-2, "Пустой ответ сервера");
            }
            return Arrays.asList(posts);
        } catch (JsonParseException e) {
            throw new ApiException(-2, "Некорректный JSON: " + e.getMessage(), e);
        }
    }
}
