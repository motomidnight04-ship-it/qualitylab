package com.example.qualitylab;

import android.app.Application;
import android.os.StrictMode;

import com.example.qualitylab.util.AppLog;

public class QualityApp extends Application {

    private static final String TAG = "QualityApp";

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            enableStrictMode();
        }
        AppLog.i(TAG, "Приложение запущено, версия " + BuildConfig.VERSION_NAME);
    }

    /** Задание 13: StrictMode ищет дисковые/сетевые операции в UI-потоке и утечки. */
    private void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeathOnNetwork() // чтобы воспроизводился NetworkOnMainThreadException (Задание 3)
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .detectLeakedSqlLiteObjects()
                .detectActivityLeaks()
                .penaltyLog()
                .build());
    }
}
