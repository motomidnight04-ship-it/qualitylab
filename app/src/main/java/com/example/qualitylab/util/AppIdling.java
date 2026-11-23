package com.example.qualitylab.util;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

/**
 * Счётчик фоновых операций для Espresso IdlingResource (Задания 9–10).
 * Пока есть хотя бы одна незавершённая фоновая операция, Espresso ждёт.
 */
public final class AppIdling {

    private static final CountingIdlingResource RESOURCE = new CountingIdlingResource("QualityLabIO");

    private AppIdling() {
    }

    public static void increment() {
        RESOURCE.increment();
    }

    public static void decrement() {
        if (!RESOURCE.isIdleNow()) {
            RESOURCE.decrement();
        }
    }

    public static IdlingResource get() {
        return RESOURCE;
    }
}
