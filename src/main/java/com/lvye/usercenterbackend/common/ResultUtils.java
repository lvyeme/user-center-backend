package com.lvye.usercenterbackend.common;

import lombok.Data;

/**
 * @author LVye
 * @version 1.0
 * 返回控制类
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data,"ok");
    }
}
