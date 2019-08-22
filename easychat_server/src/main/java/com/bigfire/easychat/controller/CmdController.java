package com.bigfire.easychat.controller;

import com.bigfire.easychat.entity.Msg;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.entity.response.Result;
import com.bigfire.easychat.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/20  22:09
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@RestController
@RequestMapping("/cmd")
public class CmdController {
    @Autowired
    MsgService msgService;
    @PostMapping("/send")
    public Result<User> msgSend(@RequestBody Msg msg){
        msgService.sendMsg(msg);//发信
        return Result.getMsgResult("发送成功");
    }
}
