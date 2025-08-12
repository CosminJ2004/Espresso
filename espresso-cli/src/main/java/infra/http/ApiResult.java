package infra.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ApiResult<T> {
    @JsonProperty("success") public final boolean success;
    @JsonProperty("data") public final T data;
    @JsonProperty("error") public final String error;
    @JsonProperty("statusCode") public final Integer statusCode;

    public ApiResult() {
        this.success = false;
        this.data = null;
        this.error = null;
        this.statusCode = null;
    }

    public ApiResult(boolean success, T data, String error, Integer statusCode) {
        this.success = success;
        this.data = data;
        this.error = error;//message
        this.statusCode = statusCode;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null, null);
    }

    public static <T> ApiResult<T> error(String error, Integer statusCode) {
        return new ApiResult<>(false, null, error, statusCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
