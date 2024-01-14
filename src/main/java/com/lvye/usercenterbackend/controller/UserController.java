package com.lvye.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lvye.usercenterbackend.common.BaseResponse;
import com.lvye.usercenterbackend.common.ResultUtils;
import com.lvye.usercenterbackend.model.domain.User;
import com.lvye.usercenterbackend.model.domain.request.userLoginRequest;
import com.lvye.usercenterbackend.model.domain.request.userRegisterRequest;
import com.lvye.usercenterbackend.service.UserService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
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


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody userRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword ,planetCode)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
/*
        return new BaseResponse<>(0,result,"ok");
*/
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody userLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return new  BaseResponse<>(0, user,"ok");
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return ResultUtils.success(userService.userLogout(request));
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null){
            return null;
        }
        Long userId = currentUser.getId();
        //TODO 判断用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String username ,HttpServletRequest request) {
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
        List<User> list = userList.stream().map(user -> userService.
                getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request) {
        //是否管理员
        if (!isAdmin(request)){
            return null;
        }
        if (id <= 0){
            return null;
        }
        boolean removeById = userService.removeById(id);
        return ResultUtils.success(removeById);
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


