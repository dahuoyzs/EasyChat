package com.bigfire.easychat.controller;

import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.entity.response.Result;
import com.bigfire.easychat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  15:03
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */

@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
