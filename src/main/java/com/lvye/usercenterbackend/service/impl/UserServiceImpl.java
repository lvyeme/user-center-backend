package com.lvye.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvye.usercenterbackend.model.domain.User;
import com.lvye.usercenterbackend.service.UserService;
import com.lvye.usercenterbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 *
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




