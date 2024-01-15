package com.lvye.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvye.usercenterbackend.common.ErrorCode;
import com.lvye.usercenterbackend.exception.BusinessException;
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
     * 加密后的密码更难破解
     */
    private static final String SALT = "lvye" ;


    @Override
    public long userRegister(String userAccount, String userPassword, String chackPassword,String planetCode) {

        //1. 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword, chackPassword)){
            throw new BusinessException(ErrorCode.PARMS_ERORR,"参数为空");
        }
        if (userAccount.length()< 4 ){
            throw new BusinessException(ErrorCode.PARMS_ERORR,"用户账号小于4位");
        }
        if (userPassword.length() < 8 || chackPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARMS_ERORR,"用户密码过短");
        }
        if (planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARMS_ERORR,"星球编号过长");
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
        //编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
        count = userMapper.selectCount(queryWrapper);
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
        user.setPlanetCode(planetCode);
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
        User saftyUser = getSafetyUser(user);
        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,saftyUser);
        return user;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User saftyUser = new User();
        saftyUser.setId(originUser.getId());
        saftyUser.setUsername(originUser.getUsername());
        saftyUser.setUserAccount(originUser.getUserAccount());
        saftyUser.setAvatarUrl(originUser.getAvatarUrl());
        saftyUser.setGender(originUser.getGender());
        saftyUser.setPlanetCode(originUser.getPlanetCode());
        saftyUser.setEmail(originUser.getEmail());
        saftyUser.setUserStatus(originUser.getUserStatus());
        saftyUser.setUserRole(originUser.getUserRole());
        saftyUser.setPhone(originUser.getPhone());
        saftyUser.setCreateTime(originUser.getCreateTime());
        return saftyUser;
    }

    /*****
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

}




