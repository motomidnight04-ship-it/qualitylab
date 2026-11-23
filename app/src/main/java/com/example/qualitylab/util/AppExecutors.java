package com.example.qualitylab.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** Один общий фоновый поток для базы данных и сети. */
public final class AppExecutors {

    private static final Executor IO = Executors.newSingleThreadExecutor();

    private AppExecutors() {
    }

    public static Executor io() {
        return IO;
    }
}
