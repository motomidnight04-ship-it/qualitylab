package com.example.qualitylab.logic;

/** Проверка полей формы заказа. */
public final class OrderValidator {

    public enum Error {
        NONE,
        PRICE_EMPTY,
        PRICE_INVALID,
        QUANTITY_EMPTY,
        QUANTITY_INVALID,
        DISCOUNT_INVALID
    }

    public static final double MIN_PRICE = 0.01;
    public static final double MAX_PRICE = 1_000_000;
    public static final int MAX_QUANTITY = 1000;

    private OrderValidator() {
    }

    public static Error validate(String price, String quantity, String discount) {
        if (isBlank(price)) {
            return Error.PRICE_EMPTY;
        }
        Double p = parseNumber(price);
        if (p == null || p < MIN_PRICE || p > MAX_PRICE) {
            return Error.PRICE_INVALID;
        }

        if (isBlank(quantity)) {
            return Error.QUANTITY_EMPTY;
        }
        String q = quantity.trim();
        if (!q.matches("\\d{1,4}")) {
            return Error.QUANTITY_INVALID;
        }
        int qty = Integer.parseInt(q);
        if (qty < 1 || qty > MAX_QUANTITY) {
            return Error.QUANTITY_INVALID;
        }

        if (!isBlank(discount)) {
            Double d = parseNumber(discount);
            if (d == null || d > 100) {
                return Error.DISCOUNT_INVALID;
            }
        }
        return Error.NONE;
    }

    /** Разбор числа; допускает запятую как разделитель. null — если это не число. */
    public static Double parseNumber(String text) {
        if (text == null) {
            return null;
        }
        String t = text.trim();
        if (!t.matches("\\d+([.,]\\d+)?")) {
            return null;
        }
        return Double.parseDouble(t.replace(',', '.'));
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
