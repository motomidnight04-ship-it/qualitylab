package com.example.qualitylab.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TaskValidatorTest {

    @Test
    public void normalTitle_isOk() {
        assertEquals(TaskValidator.TaskError.NONE, TaskValidator.validate("Купить хлеб"));
    }

    @Test
    public void blankOrNull_isEmpty() {
        assertEquals(TaskValidator.TaskError.EMPTY, TaskValidator.validate(""));
        assertEquals(TaskValidator.TaskError.EMPTY, TaskValidator.validate("   "));
        assertEquals(TaskValidator.TaskError.EMPTY, TaskValidator.validate(null));
    }

    @Test
    public void lengthBoundaries() {
        assertEquals(TaskValidator.TaskError.NONE, TaskValidator.validate(repeat('a', 100)));
        assertEquals(TaskValidator.TaskError.TOO_LONG, TaskValidator.validate(repeat('a', 101)));
        // пробелы по краям не считаются
        assertEquals(TaskValidator.TaskError.NONE, TaskValidator.validate("  " + repeat('a', 100) + "  "));
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
