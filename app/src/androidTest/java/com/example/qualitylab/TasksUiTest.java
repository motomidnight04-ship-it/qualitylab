package com.example.qualitylab;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qualitylab.data.AppDatabase;
import com.example.qualitylab.ui.TasksActivity;
import com.example.qualitylab.util.AppIdling;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Задания 9–10: асинхронный экран (Room в фоне) + IdlingResource. */
@RunWith(AndroidJUnit4.class)
public class TasksUiTest {

    private ActivityScenario<TasksActivity> scenario;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        // Чистое состояние перед каждым тестом (борьба с flaky-тестами)
        AppDatabase.get(context).clearAllTables();
        IdlingRegistry.getInstance().register(AppIdling.get());
        scenario = ActivityScenario.launch(TasksActivity.class);
    }

    @After
    public void tearDown() {
        scenario.close();
        IdlingRegistry.getInstance().unregister(AppIdling.get());
    }

    @Test
    public void addTask_appearsInList() {
        onView(withId(R.id.et_task_title)).perform(replaceText("Купить молоко"), closeSoftKeyboard());
        onView(withId(R.id.btn_add)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_tasks)).atPosition(0)
                .check(matches(withText(containsString("Купить молоко"))));
    }

    @Test
    public void emptyTitle_showsError() {
        onView(withId(R.id.btn_add)).perform(click());

        onView(withId(R.id.et_task_title))
                .check(matches(androidx.test.espresso.matcher.ViewMatchers.hasErrorText(
                        ApplicationProvider.getApplicationContext().getString(R.string.err_task_empty))));
    }

    @Test
    public void tapOnTask_marksItDone() {
        onView(withId(R.id.et_task_title)).perform(replaceText("Сделать лабу"), closeSoftKeyboard());
        onView(withId(R.id.btn_add)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_tasks)).atPosition(0).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_tasks)).atPosition(0)
                .check(matches(withText(containsString("✔"))));
    }
}
