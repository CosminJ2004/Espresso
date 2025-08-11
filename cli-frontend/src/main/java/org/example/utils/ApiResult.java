package org.example.utils;

public final class ApiResult<T> {
    private final boolean ok;
    private final T data;
    private final String message;
    private final Integer statusCode;

    private ApiResult(boolean ok, T data, String message, Integer statusCode) {
        this.ok = ok;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(true, data, null, null);
    }

    public static <T> ApiResult<T> err(String message, Integer statusCode) {
        return new ApiResult<>(false, null, message, statusCode);
    }

    public boolean isOk() { return ok; }
    public T get() { return data; }
    public String message() { return message; }
    public Integer statusCode() { return statusCode; }
}
