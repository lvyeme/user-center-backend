package com.lvye.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lvye.usercenterbackend.model.domain.User;
import com.lvye.usercenterbackend.model.domain.request.userLoginRequest;
import com.lvye.usercenterbackend.model.domain.request.userRegisterRequest;
import com.lvye.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lvye.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static com.lvye.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author LVye
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @RequestMapping("/register")
    public Long userRegister(@RequestBody userRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody userLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> userSearch(String username ,HttpServletRequest request) {
        //是否管理员
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        /*return userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());*/
        //简写为
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request) {
        //是否管理员
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     *  判断是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}


