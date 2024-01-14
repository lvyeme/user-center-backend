package com.lvye.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LVye
 * @version 1.0
 * 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
    public BaseResponse(int code, T data) {
        this(code,data,"");
    }
}
