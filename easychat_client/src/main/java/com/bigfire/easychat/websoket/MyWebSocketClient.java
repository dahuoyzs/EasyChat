package com.bigfire.easychat.websoket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.bigfire.easychat.controller.LoginController;
import com.bigfire.easychat.controller.TaskOk;
import com.bigfire.easychat.util.Storage;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  20:40
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {//已经握手,建立连接成功
        System.out.println("onOpen()");
//        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
//            String key = it.next();
//            System.out.println(key+":"+shake.getFieldValue(key));
//        }
    }

    @Override
    public void onMessage(String paramString) {
        System.out.println("接收到消息：" + paramString);

    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
        running = false;
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常" + e);
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

    //    public static MyWebSocketClient listener(String ip,String port){
//        try {
//            MyWebSocketClient client = icon MyWebSocketClient("ws://"+ip+":"+port+"/websocket");
//            client.connect();
//
//            single = client;
//            return client;
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public static void main(String[] args) {
        testConnect("localhost", "8080");
//        System.out.println(listener("localhost", "8080"));
    }
//    public static void main(String[] args) {
//        try {
//            MyWebSocketClient client = icon MyWebSocketClient("ws://localhost:8080/websocket");
//            client.connect();
//            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//                System.out.println("还没有打开");
//            }
//            System.out.println("建立websocket连接");
//            client.send("asd");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

}
