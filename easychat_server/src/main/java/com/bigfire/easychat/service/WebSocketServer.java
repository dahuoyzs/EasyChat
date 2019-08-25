package com.bigfire.easychat.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.common.util.StrUtil;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.entity.response.Result;
import com.bigfire.easychat.repository.UserDao;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  15:08
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
//@Slf4j
@ServerEndpoint(value = "/websocket/{username}")
@Service
public class WebSocketServer {

    private static UserDao userDao;
//    public WebSocketServer(){}
    @Autowired
    public WebSocketServer(UserDao userDao){
        this.userDao = userDao;
    }
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    public static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String username;
    private final static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 连接建立成功调用的方法
     */
    public WebSocketServer() {
//        log.info("WebSocketServer  init ！");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        if (username != null) {
            this.session = session;
            this.username = username;
            if (sessionMap.containsKey(username)) {
                log.error("重复登录");
                return;
            }
            webSocketSet.add(this);     //加入set中
            addOnlineCount();           //在线数加1
            sessionMap.put(username, session);
            log.info("新连接加入！在线人数为:{},sessionMapSize:{}",getOnlineCount(),sessionMap.size());
            List<String> names = mapKeys(sessionMap);
            List<User> onlineUserList = new ArrayList<>();
            StrUtil.print("当前在线的用户名如下:");
            names.forEach(name->{
                System.out.println(name);
                User user = userDao.findByUsername(name);
                System.out.println(JSONObject.toJSONString(user));
                user.setPassword("******");
                onlineUserList.add(user);
            });//输出列表
            System.out.println("在线人数:"+onlineUserList.size());
            Result<User> result = Result.getCodeResult(100,"onlineUserList",onlineUserList);
            sendMessageAll(JSONObject.toJSONString(result));//更新用户列表
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1

        sessionMap.remove(this.username);
        log.info(this.username);
        log.info("有一连接关闭！在线人数为:{},sessionMapSize:{}",getOnlineCount(),sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            item.sendMessage(message);
//        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误:{}", error.getMessage());
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //制定某个用户发消息
    public void sendMessage(String username, String message) {
        log.info("给制定用户发消息:{} MSG:", username, message);
        Session session = sessionMap.get(username);
        log.info(session.toString());
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 群发消息
     */
    public static void sendMessageAll(String message) {
        log.info("群发消息Set的长度为{}", webSocketSet.size());
        log.info(message);
        for (WebSocketServer item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
    private List<String> mapKeys(ConcurrentHashMap<String,Session> map){
        ArrayList<String> list = new ArrayList<>();
        map.keySet().forEach(e->list.add(e));
        return list;
    }

}
