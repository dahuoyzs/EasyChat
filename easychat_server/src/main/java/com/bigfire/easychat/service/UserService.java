package com.bigfire.easychat.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.common.util.JWTUtil;
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

    public User userAdd(User user){
        return userDao.save(user);
    }
    public List<User> list(){
        return userDao.findAll();
    };
    public User findByUsername(String username){
        return userDao.findByUsername(username);
    };
    //Dao层返回都是各种数据，或者List，Service就是用来处理业务的，结果拼装好了还不组装Result吗？
    public Result<User> userAddResult(User user){
        log.debug(JSONObject.toJSONString(user));
        User u = findByUsername(user.getUsername());
        if (u!=null){
            log.debug(JSONObject.toJSONString(u));
            return Result.getErrorResult("此用户已经存在，请更换用户名");
        }
        return Result.getSuccessResult(userDao.save(user));
    }
    public Result<List<User>> listResult(){
        Result<List<User>> result = Result.getSuccessResult(userDao.findAll());
        return result;
    };
    public Result<User> findByUsernameResult(String username){
        User u = findByUsername(username);
        return Result.getSuccessResult(u);
    };
    @Transactional
    public Result<User> loginResult(User user){
        log.debug(JSONObject.toJSONString(user));
        User u = findByUsername(user.getUsername());
        if (u==null){
            log.debug(JSONObject.toJSONString(u));
            return Result.getErrorResult("此用户不存在");
        }else {
            if (u.getPassword().equals(user.getPassword())){
                if (!u.getHeadUrl().equals(user.getHeadUrl())){
                    userDao.update(user.getHeadUrl(),user.getId());
                }
                Long userId = u.getId();
                String token = JWTUtil.createToken(userId+"");
                return Result.getMsgResult("登录成功",token);
            }else return Result.getErrorResult("密码错误");
        }
    };
//    public Result<User> logoutResult(User user){
//
//    }


}
