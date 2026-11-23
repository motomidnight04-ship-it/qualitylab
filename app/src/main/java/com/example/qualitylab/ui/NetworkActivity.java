package com.example.qualitylab.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qualitylab.R;
import com.example.qualitylab.net.ApiException;
import com.example.qualitylab.net.Post;
import com.example.qualitylab.net.PostsApi;
import com.example.qualitylab.util.AppExecutors;
import com.example.qualitylab.util.AppIdling;
import com.example.qualitylab.util.AppLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkActivity extends AppCompatActivity {

    private static final String TAG = "NetworkActivity";

    private TextView tvStatus;
    private ArrayAdapter<String> adapter;
    private PostsApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        tvStatus = findViewById(R.id.tv_status);
        ListView list = findViewById(R.id.list_posts);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        list.setAdapter(adapter);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        api = new PostsApi(PostsApi.DEFAULT_BASE_URL, client);

        findViewById(R.id.btn_load).setOnClickListener(v -> load());
    }

    private void load() {
        tvStatus.setText(R.string.status_loading);
        AppIdling.increment();
        AppExecutors.io().execute(() -> {
            try {
                List<Post> posts = api.fetchPosts(10);
                AppLog.d(TAG, "Получено постов: " + posts.size());
                runOnUiThread(() -> {
                    List<String> titles = new ArrayList<>();
                    for (Post p : posts) {
                        titles.add(p.id + ". " + p.title);
                    }
                    adapter.clear();
                    adapter.addAll(titles);
                    tvStatus.setText(getString(R.string.status_ok, posts.size()));
                });
            } catch (ApiException e) {
                AppLog.e(TAG, "Ошибка загрузки", e);
                runOnUiThread(() -> tvStatus.setText(getString(R.string.status_error, e.getMessage())));
            } finally {
                AppIdling.decrement();
            }
        });
    }
}
