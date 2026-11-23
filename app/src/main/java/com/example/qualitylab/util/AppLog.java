package com.example.qualitylab.util;

import android.util.Log;

import com.example.qualitylab.BuildConfig;

/**
 * Обёртка над Log (Задание 1).
 * d/i пишутся только в debug-сборке; в release их дополнительно вырезает R8
 * (см. proguard-rules.pro). w/e пишутся всегда.
 */
public final class AppLog {

    private AppLog() {
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        Log.e(tag, msg, t);
    }
}
