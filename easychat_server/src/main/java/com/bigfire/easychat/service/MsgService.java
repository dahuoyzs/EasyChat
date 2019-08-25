package com.bigfire.easychat.service;

import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.entity.Msg;
import com.bigfire.easychat.entity.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/20  22:17
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Service
public class MsgService {
    @Autowired
    WebSocketServer webSocketServer;
    public void sendMsg(Msg msg){
        if (msg!=null){
            String username = msg.getToUsername();
            if (username!=null){
                webSocketServer.sendMessage(username, JSONObject.toJSONString(Result.getCodeResult(101,"msg",msg)));
            }else {
                webSocketServer.sendMessageAll(JSONObject.toJSONString(Result.getCodeResult(101,"msg",msg)));
            }
        }
    }
}
