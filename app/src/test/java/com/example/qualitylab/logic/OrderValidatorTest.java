package com.example.qualitylab.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OrderValidatorTest {

    @Test
    public void validData_isOk() {
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("100", "3", "10"));
    }

    @Test
    public void emptyDiscount_isOk() {
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("100", "3", ""));
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("100", "3", null));
    }

    @Test
    public void commaAsDecimalSeparator_isOk() {
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("10,5", "2", "7,5"));
    }

    @Test
    public void emptyPrice() {
        assertEquals(OrderValidator.Error.PRICE_EMPTY, OrderValidator.validate("  ", "3", ""));
        assertEquals(OrderValidator.Error.PRICE_EMPTY, OrderValidator.validate(null, "3", ""));
    }

    @Test
    public void priceBoundaries() {
        assertEquals(OrderValidator.Error.PRICE_INVALID, OrderValidator.validate("0", "1", ""));
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("0.01", "1", ""));
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("1000000", "1", ""));
        assertEquals(OrderValidator.Error.PRICE_INVALID, OrderValidator.validate("1000000.01", "1", ""));
    }

    @Test
    public void priceNotANumber() {
        assertEquals(OrderValidator.Error.PRICE_INVALID, OrderValidator.validate("abc", "1", ""));
        assertEquals(OrderValidator.Error.PRICE_INVALID, OrderValidator.validate("-5", "1", ""));
        assertEquals(OrderValidator.Error.PRICE_INVALID, OrderValidator.validate("1e3", "1", ""));
    }

    @Test
    public void emptyQuantity() {
        assertEquals(OrderValidator.Error.QUANTITY_EMPTY, OrderValidator.validate("10", "", ""));
    }

    @Test
    public void quantityBoundaries() {
        assertEquals(OrderValidator.Error.QUANTITY_INVALID, OrderValidator.validate("10", "0", ""));
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("10", "1", ""));
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("10", "1000", ""));
        assertEquals(OrderValidator.Error.QUANTITY_INVALID, OrderValidator.validate("10", "1001", ""));
        assertEquals(OrderValidator.Error.QUANTITY_INVALID, OrderValidator.validate("10", "12345", ""));
        assertEquals(OrderValidator.Error.QUANTITY_INVALID, OrderValidator.validate("10", "1.5", ""));
    }

    @Test
    public void discountBoundaries() {
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("10", "1", "0"));
        assertEquals(OrderValidator.Error.NONE, OrderValidator.validate("10", "1", "100"));
        assertEquals(OrderValidator.Error.DISCOUNT_INVALID, OrderValidator.validate("10", "1", "100.5"));
        assertEquals(OrderValidator.Error.DISCOUNT_INVALID, OrderValidator.validate("10", "1", "-1"));
        assertEquals(OrderValidator.Error.DISCOUNT_INVALID, OrderValidator.validate("10", "1", "abc"));
    }

    @Test
    public void parseNumber() {
        assertEquals(12.5, OrderValidator.parseNumber(" 12,5 "), 0.0001);
        assertNull(OrderValidator.parseNumber("x"));
        assertNull(OrderValidator.parseNumber(null));
    }
}
