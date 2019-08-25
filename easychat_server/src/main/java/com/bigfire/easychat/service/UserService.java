package com.bigfire.easychat.service;


import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.common.util.JWTUtil;
import com.bigfire.easychat.common.util.Storage;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.entity.response.Result;
import com.bigfire.easychat.repository.UserDao;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  15:14
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    WebSocketServer webSocketServer;
    //注册
    public Result<User> userRegister(User user){
        log.debug(JSONObject.toJSONString(user));
        User u = userDao.findByUsername(user.getUsername());
        if (u!=null){
            log.debug(JSONObject.toJSONString(u));
            return Result.getErrorResult("此用户已经存在，请更换用户名");
        }
        return Result.getSuccessResult(userDao.save(user));
    }
    //查询所有用户
    public Result<List<User>> list(){
        Result<List<User>> result = Result.getSuccessResult(userDao.findAll());
        return result;
    };
    public Result<User> findByUsername(String username){
        User u = userDao.findByUsername(username);
        return Result.getSuccessResult(u);
    };
    //登录
    @Transactional
    public Result<User> login(User user){
        log.debug(JSONObject.toJSONString(user));
        User u = userDao.findByUsername(user.getUsername());
        if (u==null){
            log.debug(JSONObject.toJSONString(u));
            return Result.getErrorResult("此用户不存在");
        }else {
            if (u.getPassword().equals(user.getPassword())){
                if (!u.getHeadUrl().equals(user.getHeadUrl())){
                    userDao.update(user.getHeadUrl(),user.getId());
                }
                Long userId = u.getId();
                String username = u.getUsername();
                String token = JWTUtil.createToken(username);
                log.info("登录的用户名为:{}",username);//打印用户名
                Boolean isExist = Storage.onlineTokens.containsKey(username);
                if (isExist)return Result.getErrorResult("该用户已经登录过了，请勿重复登录");
                Storage.onlineTokens.put(username,token);
                log.info("当前token数:{}",Storage.onlineTokens.size());
                return Result.getMsgResult("登录成功",token);
            }else return Result.getErrorResult("密码错误");
        }
    };

    //登出
    public Result<User> logout(User user){
        log.debug(JSONObject.toJSONString(user));
        String username = user.getUsername();
        if (username==null){
            return Result.getErrorResult("要退出的用户名不能为空");
        }
        Storage.onlineTokens.remove(username);
        return Result.getMsgResult("退出成功");
    };

}
