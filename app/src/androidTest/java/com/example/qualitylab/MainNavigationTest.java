package com.example.qualitylab;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qualitylab.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainNavigationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void orderButton_opensOrderScreen() {
        onView(withId(R.id.btn_order)).perform(scrollTo(), click());
        onView(withId(R.id.btn_calculate)).check(matches(isDisplayed()));
    }

    @Test
    public void tasksButton_opensTasksScreen() {
        onView(withId(R.id.btn_tasks)).perform(scrollTo(), click());
        onView(withId(R.id.btn_add)).check(matches(isDisplayed()));
    }

    @Test
    public void networkButton_opensNetworkScreen() {
        onView(withId(R.id.btn_network)).perform(scrollTo(), click());
        onView(withId(R.id.btn_load)).check(matches(isDisplayed()));
    }
}
