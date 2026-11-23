package com.example.qualitylab.ui;

import android.app.Activity;

/** Статическое поле живёт дольше экрана. */
final class LeakHolder {

    static Activity lastActivity;

    private LeakHolder() {
    }
}
