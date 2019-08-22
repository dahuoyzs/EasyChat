package com.bigfire.easychat.controller;

import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.entity.response.Result;
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
        return userService.listResult();
    }
    @PostMapping("/add")
    public Result<User> userAdd(@RequestBody User user){
        return userService.userAddResult(user);
    }
    @GetMapping("/find/{username}")
    public Result<User> userAdd(@PathVariable String username){
        return userService.findByUsernameResult(username);
    }
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user){
        return userService.loginResult(user);
    }
}
