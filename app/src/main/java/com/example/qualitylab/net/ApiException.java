package com.example.qualitylab.net;

public class ApiException extends Exception {

    /** HTTP-код, либо -1 (сетевая ошибка), либо -2 (некорректный JSON). */
    private final int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
