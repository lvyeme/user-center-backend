package com.lvye.usercenterbackend.common;

/**
 * @author LVye
 * @version 1.0
 * 错误信息
 */
public enum ErrorCode {

    SUCCESS(0,"ok",""),
    PARMS_ERORR(40000,"请求参数错误",""),
    NULL_ERORR(40001,"请求数据为空",""),
    NO_AUTH(40101,"无权限",""),
    NO_LOGIN(40100,"未登录",""),

    ;
    private final int code;
    /**
     * 状态信息
     */
    private final String message;
    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
