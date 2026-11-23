package com.example.qualitylab.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qualitylab.R;
import com.example.qualitylab.ui.ResultActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

/** Задание 7: тесты с Android-контекстом на JVM, без эмулятора (Robolectric). */
@RunWith(AndroidJUnit4.class)
@Config(sdk = 34)
public class AndroidContextRobolectricTest {

    private final Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void sharedPreferences_saveAndRead() {
        SharedPreferences prefs = context.getSharedPreferences("test_prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("launches", 7).commit();

        assertEquals(7, prefs.getInt("launches", 0));
    }

    @Test
    public void resources_appNameIsAvailable() {
        assertEquals("QualityLab", context.getString(R.string.app_name));
    }

    @Test
    public void intent_carriesExtras() {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_TOTAL, 270.0);
        intent.putExtra(ResultActivity.EXTRA_QUANTITY, 3);

        assertEquals(270.0, intent.getDoubleExtra(ResultActivity.EXTRA_TOTAL, 0), 0.0001);
        assertEquals(3, intent.getIntExtra(ResultActivity.EXTRA_QUANTITY, 0));
        assertNotNull(intent.getComponent());
    }

    @Test
    public void resultActivity_showsTotalFromIntent() {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_TOTAL, 270.0);
        intent.putExtra(ResultActivity.EXTRA_QUANTITY, 3);

        try (ActivityScenario<ResultActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                TextView total = activity.findViewById(R.id.tv_result_total);
                assertEquals(context.getString(R.string.result_total, "270.00"), total.getText().toString());
            });
        }
    }
}
