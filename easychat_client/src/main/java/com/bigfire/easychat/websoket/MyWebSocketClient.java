package com.bigfire.easychat.websoket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.controller.ChatController;
import com.bigfire.easychat.controller.LoginController;
import com.bigfire.easychat.controller.TaskOk;
import com.bigfire.easychat.entity.Cmd;
import com.bigfire.easychat.entity.Msg;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.util.DialogUtil;
import com.bigfire.easychat.util.Storage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  20:40
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Slf4j
public class MyWebSocketClient extends WebSocketClient {
//    Logger logger = LoggerFactory.getLogger(MyWebSocketClient.class);
    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {//已经握手,建立连接成功
        log.info("onOpen()");
//        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
//            String key = it.next();
//            System.out.println(key+":"+shake.getFieldValue(key));
//        }
    }

    @Override
    public void onMessage(String resultStr) {
        log.debug("接收到消息：" + resultStr);
        JSONObject result = JSONObject.parseObject(resultStr);
        Integer code = result.getInteger("code");
        String note = result.getString("msg");

        ChatController chatController = (ChatController) Storage.controllers.get("chatController");
        if (code==100){//用户列表
//            String data = result.getJSONArray("data");
            JSONArray jsonArray = result.getJSONArray("data");
            log.info("【onlineUsers】",jsonArray);
            ArrayList<User> users = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                users.add(jsonArray.getJSONObject(i).toJavaObject(User.class));
            }
            Storage.onlineUsers = users;
            chatController.updateUserList(users);
        }else if (code==101){//消息
            JSONObject data = result.getJSONObject("data");
            log.debug("【msg】{}",data);
            Msg msg = data.toJavaObject(Msg.class);
            chatController.updateMsg(msg);
        }else if (code==102){//cmd
            JSONObject data = result.getJSONObject("data");
            log.debug("【cmd】{}",data);
            Cmd cmd = data.toJavaObject(Cmd.class);
            chatController.updateCmd(cmd);
        }else {//未知信息
            DialogUtil.error(note);
        }

    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        log.info("关闭...");
        running = false;
    }

    @Override
    public void onError(Exception e) {
        log.error("异常" + e);
        running = false;
    }
    public static Boolean running = true;
    public static void testConnect(String ip, String port) {
        try {
            running = true;
            MyWebSocketClient client = new MyWebSocketClient("ws://" + ip + ":" + port + "/websocket/test");
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN) && running) {
                Thread.sleep(1);
            }
            if (running){//连接成功

            }else {//连接失败

            }
            LoginController loginController = (LoginController) Storage.controllers.get("loginController");
            loginController.testConnect(running);
//            LoginController.loginController.testConnect(running);
            client.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void connect(String ip, String port,String username) {
        try {
//            running = true;
            MyWebSocketClient client = new MyWebSocketClient("ws://" + ip + ":" + port + "/websocket/"+username);
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN) && running) {
                Thread.sleep(1);
            }
//            LoginController.loginController.testConnect(running);
//            client.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
