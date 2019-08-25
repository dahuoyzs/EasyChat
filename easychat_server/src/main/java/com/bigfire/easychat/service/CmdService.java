package com.bigfire.easychat.service;

import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.entity.Cmd;
import com.bigfire.easychat.entity.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/20  23:19
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Service
public class CmdService {
    @Autowired
    WebSocketServer webSocketServer;
    public void sendCmd(Cmd cmd){
        if (cmd!=null){
            String username = cmd.getToUsername();
            if (username!=null){
                webSocketServer.sendMessage(username, JSONObject.toJSONString(Result.getCodeResult(102,"cmd",cmd)));
            }else {
                webSocketServer.sendMessageAll(JSONObject.toJSONString(Result.getCodeResult(102,"cmd",cmd)));
            }
        }
    }
}
