package com.bank.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private String result;
    /**
     * 响应业务状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应中的数据
     */
    private Object data;

    public Result(String result, Integer code, String message, Object data) {
        this.result = result;
        this.code = code;
        this.message = message;
        if (null == data) {
            this.data = "";
        } else {
            this.data = data;
        }

    }

    public static Result success(Object data) {
        return new Result("成功", 0, "成功", data);
    }

    public static Result success() {
        return new Result("成功", 0, "成功", null);
    }

}
