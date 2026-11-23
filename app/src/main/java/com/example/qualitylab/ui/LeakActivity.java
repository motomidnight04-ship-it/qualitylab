package com.example.qualitylab.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qualitylab.R;
import com.example.qualitylab.util.AppLog;

public class LeakActivity extends AppCompatActivity {

    private static final String TAG = "LeakActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);


        AppLog.d(TAG, "Экран открыт");

        findViewById(R.id.btn_leak_back).setOnClickListener(v -> finish());
    }
}
