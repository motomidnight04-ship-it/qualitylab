package com.example.qualitylab;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.qualitylab.ui.OrderActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Задание 9: базовый Espresso-тест (до рефакторинга на Page Object). */
@RunWith(AndroidJUnit4.class)
public class OrderEspressoTest {

    @Rule
    public ActivityScenarioRule<OrderActivity> rule = new ActivityScenarioRule<>(OrderActivity.class);

    private String string(int res, Object... args) {
        return InstrumentationRegistry.getInstrumentation().getTargetContext().getString(res, args);
    }

    @Test
    public void validForm_showsTotal() {
        onView(withId(R.id.et_price)).perform(scrollTo(), replaceText("100"));
        onView(withId(R.id.et_quantity)).perform(scrollTo(), replaceText("3"));
        onView(withId(R.id.et_discount)).perform(scrollTo(), replaceText("10"));
        closeSoftKeyboard();

        onView(withId(R.id.btn_calculate)).perform(scrollTo(), click());

        onView(withId(R.id.tv_result)).perform(scrollTo())
                .check(matches(withText(string(R.string.result_total, "270.00"))));
    }

    @Test
    public void emptyPrice_showsError() {
        onView(withId(R.id.btn_calculate)).perform(scrollTo(), click());

        onView(withId(R.id.tv_error)).perform(scrollTo())
                .check(matches(withText(string(R.string.err_price_empty))));
    }
}
