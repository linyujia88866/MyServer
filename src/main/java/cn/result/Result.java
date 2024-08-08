package cn.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> Result<E> success (E data) {
        return new Result<>(200,"操作成功",data);
    }

    public static <E> Result<E> success (E data, String msg) {
        return new Result<>(200,msg,data);
    }

    public static Result success() {
        return new Result(200,"操作成功",null);
    }

    public static Result error(int code, String message) {
        return new Result(code, message,null);
    }


}