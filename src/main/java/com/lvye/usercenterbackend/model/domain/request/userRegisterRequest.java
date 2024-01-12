package com.lvye.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 * @author LVye
 * @version 1.0
 */
@Data
public class userRegisterRequest implements Serializable {
    private static final long serialVersionUID = 9019647009923591588L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
    private String planetCode;
}
