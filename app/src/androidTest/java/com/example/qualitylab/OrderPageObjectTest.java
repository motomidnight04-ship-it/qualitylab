package com.example.qualitylab;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.hamcrest.Matchers.allOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qualitylab.pages.OrderPage;
import com.example.qualitylab.ui.OrderActivity;
import com.example.qualitylab.ui.ResultActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Задания 9–10: сценарии формы и навигации на Page Object + Espresso Intents. */
@RunWith(AndroidJUnit4.class)
public class OrderPageObjectTest {

    private ActivityScenario<OrderActivity> scenario;
    private final OrderPage orderPage = new OrderPage();

    @Before
    public void setUp() {
        Intents.init();
        scenario = ActivityScenario.launch(OrderActivity.class);
    }

    @After
    public void tearDown() {
        scenario.close();
        Intents.release();
    }

    @Test
    public void total_withCustomerDiscount() {
        orderPage.enterPrice("100").enterQuantity("3").enterDiscount("10").calculate().assertTotal("270.00");
    }

    @Test
    public void total_withoutDiscount() {
        orderPage.enterPrice("19.99").enterQuantity("2").calculate().assertTotal("39.98");
    }

    @Test
    public void invalidQuantity_showsError() {
        orderPage.enterPrice("100").enterQuantity("0").calculate().assertError(R.string.err_qty_invalid);
    }

    @Test
    public void details_opensResultScreenWithTotal() {
        orderPage.enterPrice("100").enterQuantity("3").enterDiscount("10").calculate().openDetails();

        intended(allOf(hasComponent(ResultActivity.class.getName()),
                hasExtra(ResultActivity.EXTRA_TOTAL, 270.0),
                hasExtra(ResultActivity.EXTRA_QUANTITY, 3)));
    }
}
