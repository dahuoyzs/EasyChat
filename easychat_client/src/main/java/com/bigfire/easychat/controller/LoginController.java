package com.bigfire.easychat.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.App;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.util.DialogUtil;
import com.bigfire.easychat.util.IPUtil;
import com.bigfire.easychat.util.Storage;
import com.bigfire.easychat.websoket.MyWebSocketClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.java_websocket.client.WebSocketClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  20:36
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */

public class LoginController implements Initializable,TaskOk{
    @FXML Label noteLabel;
    @FXML TextField usernameTextfield;
    @FXML PasswordField passwordField;
    @FXML TextField ipTextfield;
    @FXML TextField portTextfield;
    @FXML ImageView imageViewHead;
    @FXML ImageView imageViewTest;
    //ip相关
    private String ip;
    private String port;
    private Boolean isCanConnect;
    //登录
    public void login() {
        User user = check();
        if (user!=null) {
            String loginApi = "http://" + ip + ":" + port+"/user/login";
            String loginResult = HttpUtil.post(loginApi, JSONObject.toJSONString(user));
            JSONObject result = JSONObject.parseObject(loginResult);
            Integer code = result.getInteger("code");
            String msg = result.getString("msg");
            if (code == 200) {

//                MyWebSocketClient.connect(ip, port, user.getUsername());
            } else if (code == 500) {
                DialogUtil.error(msg);
            }
        }
    }

    //注册
    public void register() {
        User user = check();
        if (user!=null) {
            String registerApi = "http://" + ip + ":" + port+"/user/add";
            String registerResultStr = HttpUtil.post(registerApi, JSONObject.toJSONString(user));
            JSONObject result = JSONObject.parseObject(registerResultStr);
            Integer code = result.getInteger("code");
            String msg = result.getString("msg");
            User u = JSONObject.parseObject(result.getJSONObject("data").toJSONString(), User.class);
            if (code == 200) {
                DialogUtil.info("注册成功,可以登录了");
                String notes = "您申请的账号:" + u.getUsername() + "密码:" + u.getPassword();
                noteLabel.setText(notes);
            } else if (code == 500) {
                DialogUtil.error(msg);
            }
        }
    }

    //网络检测
    public void testConnect() throws IOException {
        String notes = noteLabel.getText().trim();
        String username = usernameTextfield.getText().trim();
        String password = passwordField.getText().trim();
        String ip = ipTextfield.getText().trim();
        String port = portTextfield.getText().trim();
        StringBuilder sb = new StringBuilder().append("notes:").append(notes).append("，").append("username:").append(username).append("，").append("password:").append(password).append("，").append("ip:").append(ip).append("，").append("port:").append(port).append("\n");
        System.out.println(sb.toString());
        Boolean isIp = IPUtil.isIP(ip);
        Boolean isPort = IPUtil.isPort(port);
        if ((isIp && isPort) || (isPort && ip.equals("localhost"))) {//合法得ip和端口
            noteLabel.setText("网络测试中");
            Platform.runLater(() -> MyWebSocketClient.testConnect(ip, port));
        } else if (!isIp) {
            DialogUtil.info("错误的IP地址");
        } else if (!isPort) {
            DialogUtil.info("错误的端口");
        }
        this.ip = ip;
        this.port = port;
    }

    //测试
    public void test() {
        String fileName = StrUtil.subAfter(imageViewHead.getImage().impl_getUrl(),"images/hzw/",true);
        System.out.println(fileName);
        System.out.println(getClass().getClassLoader().getResource("images/hzw/"));
    }
    @Override
    public void changeHead(Image image) {

        imageViewHead.setImage(image);
    }


    //WebSocket检测
    @Override
    public void testConnect(Boolean isCanConnect) {
        this.isCanConnect = isCanConnect;
        noteLabel.setText(isCanConnect ? "连接成功" : "连接失败");
        if (isCanConnect) {
            DialogUtil.info("连接成功");
        } else {
            DialogUtil.error("网络不可用");
        }
    }

    //参数检测
    private User check() {
        if (isCanConnect == null) {
            DialogUtil.info("请先检测网络状况");
            return null;
        } else if (!isCanConnect) {
            DialogUtil.error("网络不可用");
            return null;
        } else {
            String username = usernameTextfield.getText().trim();
            String password = passwordField.getText().trim();
            if (username.trim().length() < 2) {
                DialogUtil.info("用户名最短2位,密码最短4位");
                return null;
            } else if (password.trim().length() < 4) {
                DialogUtil.info("用户名最短2位,密码最短4位");
                return null;
            } else {
                String fileName = StrUtil.subAfter(imageViewHead.getImage().impl_getUrl(),"images/hzw/",true);
                String ip = IPUtil.getIp();
                return new User()
                        .setUsername(username)
                        .setPassword(password)
                        .setHeadUrl(fileName)
                        .setIp(ip)
                        .setAddress(IPUtil.getAddress(ip))
                        .setCreateDate(new Date());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        loginController = this;
        Storage.controllers.put("loginController",this);
        imageViewHead.setOnMouseClicked((event) -> {
            try {
                Stage secondStage = new Stage();//创建舞台
                secondStage.setTitle("选择头像");
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/HeadList.fxml"));

                Scene scene = new Scene(root, 300, 300);//创建场景

                secondStage.setResizable(false);
                secondStage.setScene(scene);
                secondStage.initModality(Modality.APPLICATION_MODAL);
                Stage loginStage = (Stage) Storage.stageViews.get("loginStage");
                secondStage.setX(loginStage.getX()+loginStage.getWidth()-5);
                secondStage.setY(loginStage.getY());
                secondStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
