package com.lvye.usercenterbackend.service;

import java.util.Date;

import com.lvye.usercenterbackend.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author LVye
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("dogyupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://avatars.githubusercontent.com/u/62923489?v=4");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setEmail("123");
        user.setPhone("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

@Test
void userRegister() {
    String userAccount = "yupi";
    String userPassword = "";
    String checkPassword = "123456";
    long result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertEquals(-1, result);

    userAccount = "yu";
    result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertEquals(-1, result);
    userAccount = "yupi";
    userPassword = "123456";
    result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertEquals(-1, result);

    userAccount = "yu pi";
    userPassword = "12345678";
    result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertEquals(-1, result);
    checkPassword = "123456789";
    result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertEquals(-1, result);
    userAccount = "dogyupi";
    userPassword = "12345678";
    result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertEquals(-1, result);
    userAccount = "yupi";
    result = userService.userRegister(userAccount, userPassword,
            checkPassword);
    Assertions.assertTrue(result > 0);
}
    @Test
    void testuserRegister() {
        //非空
        String userAccount = "dog";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        //账户长度不小于与4位
        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        //密码长度不小于与8位
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword);
        Assertions.assertEquals(-1, result);
        //两次输入的密码不一致
        userAccount = "dogyupi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword);
        Assertions.assertEquals(-1, result);
        //账户已存在
        userAccount = "asd";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword);
        /*Assertions.assertTrue(result > 0);*/

        Assertions.assertEquals(-1, result);

    }

    @Test
    void registerSuccess() {
        String useraccount = "yupi";
        String userpassword = "";
        String checkpassword = "123456";
        long result = userService.userRegister(useraccount, userpassword, checkpassword);
        Assertions.assertEquals(-1,result);

        useraccount = "yu";
        result = userService.userRegister(useraccount, userpassword, checkpassword);
        Assertions.assertEquals(-1,result);

        useraccount = "yupi";
        userpassword = "123456";
        result = userService.userRegister(useraccount, userpassword, checkpassword);
        Assertions.assertEquals(-1,result);

        useraccount = "yu pi";
        userpassword = "12345678";
        result = userService.userRegister(useraccount, userpassword, checkpassword);
        Assertions.assertEquals(-1,result);

        checkpassword = "123456789";
        result = userService.userRegister(useraccount, userpassword, checkpassword);
        Assertions.assertEquals(-1,result);

        useraccount = "dogyupi";
        checkpassword = "12345678";
        result = userService.userRegister(useraccount, userpassword, checkpassword);
        Assertions.assertEquals(-1,result);

        useraccount = "yupi";
        result =  userService.userRegister(useraccount, userpassword, checkpassword);
       // Assertions.assertEquals(-1,result);
        Assertions.assertEquals(-1,result);

    }
}