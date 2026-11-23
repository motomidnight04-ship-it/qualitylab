package com.example.qualitylab.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OrderCalculatorErrorsTest {

    @Test(expected = IllegalArgumentException.class)
    public void negativePrice_throws() {
        OrderCalculator.calculateTotal(-1, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeQuantity_throws() {
        OrderCalculator.calculateTotal(1, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void discountOver100_throws() {
        OrderCalculator.calculateTotal(1, 1, 100.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDiscount_throws() {
        OrderCalculator.calculateTotal(1, 1, -0.5);
    }

    @Test
    public void format_usesTwoDecimals() {
        assertEquals("270.00", OrderCalculator.format(270));
        assertEquals("0.10", OrderCalculator.format(0.1));
    }

    @Test
    public void round2_roundsToCents() {
        assertEquals(1.24, OrderCalculator.round2(1.235), 0.0001);
    }
}
