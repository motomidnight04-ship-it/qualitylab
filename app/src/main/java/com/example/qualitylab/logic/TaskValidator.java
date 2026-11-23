package com.example.qualitylab.logic;

/** Проверка названия задачи. */
public final class TaskValidator {

    public enum TaskError {NONE, EMPTY, TOO_LONG}

    public static final int MAX_LENGTH = 100;

    private TaskValidator() {
    }

    public static TaskError validate(String title) {
        if (title == null || title.trim().isEmpty()) {
            return TaskError.EMPTY;
        }
        if (title.trim().length() > MAX_LENGTH) {
            return TaskError.TOO_LONG;
        }
        return TaskError.NONE;
    }
}
