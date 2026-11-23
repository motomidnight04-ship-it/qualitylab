package com.example.qualitylab.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qualitylab.R;
import com.example.qualitylab.util.AppExecutors;
import com.example.qualitylab.util.AppLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Задание 3: намеренные крэши. Галочка «Исправленная версия» включает безопасный код. */
public class CrashActivity extends AppCompatActivity {

    private static final String TAG = "CrashActivity";

    private CheckBox cbFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        cbFixed = findViewById(R.id.cb_fixed);
        findViewById(R.id.btn_npe).setOnClickListener(v -> nullPointer());
        findViewById(R.id.btn_ioobe).setOnClickListener(v -> indexOutOfBounds());
        findViewById(R.id.btn_network_main).setOnClickListener(v -> networkOnMainThread());
    }

    private String getNullableText() {
        return null;
    }

    private void nullPointer() {
        String text = getNullableText();
        if (cbFixed.isChecked()) {
            int length = text == null ? 0 : text.length();
            AppLog.d(TAG, "Длина строки: " + length);
            toastOk();
        } else {
            AppLog.d(TAG, "Длина строки: " + text.length()); // NullPointerException
        }
    }

    private void indexOutOfBounds() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        if (cbFixed.isChecked()) {
            if (numbers.size() > 5) {
                AppLog.d(TAG, "Элемент: " + numbers.get(5));
            }
            toastOk();
        } else {
            AppLog.d(TAG, "Элемент: " + numbers.get(5)); // IndexOutOfBoundsException
        }
    }

    private void networkOnMainThread() {
        if (cbFixed.isChecked()) {
            AppExecutors.io().execute(() -> {
                try {
                    int code = openConnection();
                    AppLog.d(TAG, "HTTP код: " + code);
                } catch (IOException e) {
                    AppLog.e(TAG, "Нет сети", e);
                }
            });
            toastOk();
        } else {
            try {
                openConnection(); // NetworkOnMainThreadException
            } catch (IOException e) {
                AppLog.e(TAG, "Нет сети", e);
            }
        }
    }

    private int openConnection() throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL("https://jsonplaceholder.typicode.com/posts/1").openConnection();
        try {
            return connection.getResponseCode();
        } finally {
            connection.disconnect();
        }
    }

    private void toastOk() {
        Toast.makeText(this, R.string.toast_ok, Toast.LENGTH_SHORT).show();
    }
}
