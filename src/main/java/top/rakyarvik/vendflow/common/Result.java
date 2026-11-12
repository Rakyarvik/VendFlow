package top.rakyarvik.vendflow.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 0;
        r.message = "ok";
        r.data = data;
        return r;
    }
}
