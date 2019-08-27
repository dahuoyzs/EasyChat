package com.bigfire.easychat.controller;

import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.common.util.Result;
import com.bigfire.easychat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/18  23:46
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/list")
    public Result<List<User>> userList(){
        return userService.list();
    }
    @PostMapping("/add")
    public Result<User> userAdd(@RequestBody User user){
        return userService.userRegister(user);
    }
    @PostMapping("/find/{username}")
    public Result<User> userAdd(@PathVariable String username){
        return userService.findByUsername(username);
    }
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user){
        return userService.login(user);
    }
    @PostMapping("/logout")
    public Result<User> logout(@RequestBody User user){
        return userService.logout(user);
    }
}
