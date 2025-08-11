package infra.http;

public final class ApiResult<T> {
    private final boolean success;
    private final T data;
    private final String message;
    private final Integer statusCode;

    private ApiResult(boolean success, T data, String message, Integer statusCode) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null, null);
    }

    public static <T> ApiResult<T> error(String message, Integer statusCode) {
        return new ApiResult<>(false, null, message, statusCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public T get() {
        return data;
    }

    public String message() {
        return message;
    }

    public Integer statusCode() {
        return statusCode;
    }
}
