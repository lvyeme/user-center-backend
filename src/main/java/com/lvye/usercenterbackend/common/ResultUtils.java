package com.lvye.usercenterbackend.common;

import lombok.Data;

/**
 * @author LVye
 * @version 1.0
 * 返回控制类
 */

public class ResultUtils {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data,"ok");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error (ErrorCode errorCode){
        //return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),errorCode.getDescription());
    return new BaseResponse<>(errorCode);
    }
}
