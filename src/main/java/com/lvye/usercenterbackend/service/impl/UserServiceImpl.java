package com.lvye.usercenterbackend.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvye.usercenterbackend.model.domain.User;
import com.lvye.usercenterbackend.service.UserService;
import com.lvye.usercenterbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lvye.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 *
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值
     * 为了加密后的密码更难破解
     */
    private static final String SALT = "lvye" ;


    @Override
    public long userRegister(String userAccount, String userPassword, String chackPassword) {

        //1. 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword, chackPassword)){
            return -1;
        }
        if (userAccount.length()< 4 ){
            return -1;
        }
        if (userPassword.length() < 8 || chackPassword.length() < 8){
            return -1;
        }
        //校验账户不能含有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return -1;
        }
        //校验密码和校验密码是否一致
        if (!userPassword.equals(chackPassword)){
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT +
                userPassword).getBytes());
        //3. 写入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if (userAccount.length()< 4 ){
            return null;
        }
        if (userPassword.length() < 8 ){
            return null;
        }
        //校验账户不能含有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return null;
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT +userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("user login failed, userAccount or userPassword is invalid");
            return null;
        }
        // 3.用户脱敏
        User saftyUser = new User();
        saftyUser.setId(user.getId());
        saftyUser.setUsername(user.getUsername());
        saftyUser.setUserAccount(user.getUserAccount());
        saftyUser.setAvatarUrl(user.getAvatarUrl());
        saftyUser.setGender(user.getGender());
        saftyUser.setEmail(user.getEmail());
        saftyUser.setUserStatus(user.getUserStatus());
        saftyUser.setUserRole(user.getUserRole());
        saftyUser.setPhone(user.getPhone());
        saftyUser.setCreateTime(user.getCreateTime());
        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,saftyUser);
        return user;

    }
}




