package com.iot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.iot.constants.ResultConstants.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static Result error(String msg) {
        return new Result(ERROR_CODE, msg, null);
    }
}
