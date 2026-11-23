package com.example.qualitylab.logic;

import java.util.Locale;

/** Расчёт суммы заказа (бизнес-логика). */
public final class OrderCalculator {

    /** С этого количества включается оптовая скидка. */
    public static final int BULK_THRESHOLD = 10;
    /** Размер оптовой скидки, в процентах. */
    public static final double BULK_DISCOUNT_PERCENT = 5.0;

    private OrderCalculator() {
    }

    /**
     * @param price           цена за единицу (>= 0)
     * @param quantity        количество (>= 0)
     * @param discountPercent скидка клиента, 0..100
     * @return итоговая сумма, округлённая до копеек
     */
    public static double calculateTotal(double price, int quantity, double discountPercent) {
        if (price < 0 || quantity < 0 || discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Недопустимые параметры заказа");
        }
        double subtotal = price * quantity;
        double total = subtotal - subtotal * discountPercent / 100.0;

        if (quantity >= BULK_THRESHOLD) {
            double bulkDiscount = total * BULK_DISCOUNT_PERCENT / 100.0;
            total = total - bulkDiscount;
        }
        return round2(total);
    }

    public static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static String format(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
