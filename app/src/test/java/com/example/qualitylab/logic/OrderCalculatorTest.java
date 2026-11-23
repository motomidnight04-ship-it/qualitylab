package com.example.qualitylab.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/** Параметризованный тест расчёта суммы (Задание 5). */
@RunWith(Parameterized.class)
public class OrderCalculatorTest {

    @Parameterized.Parameters(name = "цена={0}, кол-во={1}, скидка={2}% -> {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {100.0, 3, 0.0, 300.00},
                {100.0, 3, 10.0, 270.00},
                {50.0, 1, 100.0, 0.00},
                {19.99, 2, 0.0, 39.98},
                {0.01, 1, 0.0, 0.01},
                {100.0, 0, 0.0, 0.00},
                {10.0, 9, 0.0, 90.00},        // граница: оптовой скидки ещё нет
                {10.0, 10, 0.0, 95.00},       // граница: оптовая скидка 5%
                {200.0, 10, 10.0, 1710.00},   // скидка клиента + оптовая
                {1.0, 1000, 0.0, 950.00}
        });
    }

    private final double price;
    private final int quantity;
    private final double discount;
    private final double expected;

    public OrderCalculatorTest(double price, int quantity, double discount, double expected) {
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.expected = expected;
    }

    @Test
    public void calculatesTotal() {
        // Arrange — данные пришли из параметров
        // Act
        double actual = OrderCalculator.calculateTotal(price, quantity, discount);
        // Assert
        assertEquals(expected, actual, 0.001);
    }
}
