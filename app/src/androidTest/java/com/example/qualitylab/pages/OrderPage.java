package com.example.qualitylab.pages;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.qualitylab.R;

/** Задание 10: Page Object — вся работа с экраном «Заказ» в одном месте. */
public class OrderPage {

    public OrderPage enterPrice(String value) {
        onView(withId(R.id.et_price)).perform(scrollTo(), replaceText(value));
        return this;
    }

    public OrderPage enterQuantity(String value) {
        onView(withId(R.id.et_quantity)).perform(scrollTo(), replaceText(value));
        return this;
    }

    public OrderPage enterDiscount(String value) {
        onView(withId(R.id.et_discount)).perform(scrollTo(), replaceText(value));
        return this;
    }

    public OrderPage calculate() {
        closeSoftKeyboard();
        onView(withId(R.id.btn_calculate)).perform(scrollTo(), click());
        return this;
    }

    public OrderPage assertTotal(String formattedTotal) {
        String expected = string(R.string.result_total, formattedTotal);
        onView(withId(R.id.tv_result)).perform(scrollTo()).check(matches(withText(expected)));
        return this;
    }

    public OrderPage assertError(int stringRes) {
        onView(withId(R.id.tv_error)).perform(scrollTo()).check(matches(withText(string(stringRes))));
        return this;
    }

    public OrderPage openDetails() {
        onView(withId(R.id.btn_details)).perform(scrollTo(), click());
        return this;
    }

    public OrderPage assertDisplayed() {
        onView(withId(R.id.btn_calculate)).check(matches(isDisplayed()));
        return this;
    }

    private static String string(int res, Object... args) {
        return InstrumentationRegistry.getInstrumentation().getTargetContext().getString(res, args);
    }
}
