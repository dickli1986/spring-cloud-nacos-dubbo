package com.dickli.controller.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Result<T> implements Serializable {
    private boolean success;
    private T data;
    private String msg;

    public static Result success(Object data) {
        return new Result(Boolean.TRUE, data, "交易成功");
    }

    public static Result fail(String msg) {
        return new Result(Boolean.FALSE, null, msg);
    }
}
