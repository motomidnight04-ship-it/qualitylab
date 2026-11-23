package com.example.qualitylab.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qualitylab.R;
import com.example.qualitylab.util.AppLog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Счётчик запусков
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int launches = prefs.getInt("launches", 0) + 1;
        prefs.edit().putInt("launches", launches).apply();
        AppLog.i(TAG, "Запуск №" + launches);

        TextView tvLaunches = findViewById(R.id.tv_launches);
        tvLaunches.setText(getString(R.string.launches_count, launches));

        open(R.id.btn_order, OrderActivity.class);
        open(R.id.btn_tasks, TasksActivity.class);
        open(R.id.btn_network, NetworkActivity.class);
        open(R.id.btn_crash, CrashActivity.class);
        open(R.id.btn_leak, LeakActivity.class);
    }

    private void open(int buttonId, Class<?> target) {
        findViewById(buttonId).setOnClickListener(v -> {
            AppLog.d(TAG, "Переход: " + target.getSimpleName());
            startActivity(new Intent(this, target));
        });
    }
}
