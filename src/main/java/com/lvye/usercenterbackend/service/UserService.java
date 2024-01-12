package com.lvye.usercenterbackend.service;

import com.lvye.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 *
*/
public interface UserService extends IService<User> {

    /***
     *
     * @param userAccount ⽤户账户
     * @param userPassword ⽤户密码
     * @param chackPassword 校验密码
     * @param planetCode 编号
     * @return 新⽤户 id
     */
    long userRegister(String userAccount, String userPassword,String chackPassword,String planetCode);

    /**
     * 用户登录
     * @param userAccount  ⽤户账户
     * @param userPassword ⽤户密码
     * @return 脱敏后用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /****
     * 用户注销
     * @param request
     * @return
     */
    int userLogout (HttpServletRequest request);
}
