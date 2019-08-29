package com.bigfire.easychat.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.util.DialogUtil;
import com.bigfire.easychat.util.IPUtil;
import com.bigfire.easychat.util.Storage;
import com.bigfire.easychat.websoket.MyWebSocketClient;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  20:36
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Slf4j
public class LoginController implements Initializable, TaskOk {
    @FXML
    Label noteLabel;
    @FXML
    TextField usernameTextfield;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField ipTextfield;
    @FXML
    TextField portTextfield;
    @FXML
    ImageView imageViewHead;
    @FXML
    ImageView imageViewTest;
    //ip相关
    private String ip;
    private String port;
    private Boolean isCanConnect;

    //登录
    public void login() {
        ip = ipTextfield.getText().trim();
        port = portTextfield.getText().trim();
        String loginApi = "http://" + ip + ":" + port + "/user/login";
        JSONObject loginResult = JSONObject.parseObject(action(loginApi));
        System.out.println(loginResult);
        Integer loginCode = loginResult.getInteger("code");
        String loginMsg = loginResult.getString("msg");

        if (loginCode == 200) {

            String token = loginResult.getString("data");
            Storage.token = token;
            Storage.ip = ip;
            Storage.port = port;
//            System.out.println(Storage.token);
            Parent chatParent = null;
            try {
                chatParent = FXMLLoader.load(getClass().getClassLoader().getResource("views/ChatLayout.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage chatStage = new Stage();
            chatStage.setWidth(1000);
            chatStage.setHeight(650);
            Scene scene = new Scene(chatParent);
            chatStage.setScene(scene);
            chatStage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon/wx.png").toString()));
            chatStage.setTitle("EasyChat0.0.3");
            chatStage.setOnCloseRequest(e -> {
                try{
                    User user = Storage.onlineUser;
                    String logoutApi = "http://" + ip + ":" + port + "/user/logout";
                    String resultStr = HttpUtil.post(logoutApi,JSONObject.toJSONString(user));
                    JSONObject logoutResult = JSONObject.parseObject(resultStr);
                    Integer logoutCode = logoutResult.getInteger("code");
                    String logoutMsg = logoutResult.getString("msg");
                    if (loginCode==200){//退出成功
                        log.info("安全退出");
                        System.exit(0);
                    }else DialogUtil.error(loginMsg);
                }catch (Exception exception){
                    log.error("不正常的退出{}",exception.getMessage());
                }
            });
            Storage.stageViews.put("chatStage", chatStage);
            Stage loginStage = Storage.stageViews.get("loginStage");
            loginStage.close();
            chatStage.show();


        }else if (loginCode == 500){
            DialogUtil.error(loginMsg);
        }

    }

    //注册
    public void register() {
        ip = ipTextfield.getText().trim();
        port = portTextfield.getText().trim();
        String loginApi = "http://" + ip + ":" + port + "/user/add";
        JSONObject result = JSONObject.parseObject(action(loginApi));
        log.info(result.toJSONString());
        Integer code = result.getInteger("code");
        String msg = result.getString("msg");
        if (code == 200) {
            User u = JSONObject.parseObject(result.getJSONObject("data").toJSONString(), User.class);
            DialogUtil.info("注册成功,可以登录了");
            String notes = "您申请的账号:" + u.getUsername() + "密码:" + u.getPassword();
            noteLabel.setText(notes);
        }else if (code == 500){
            DialogUtil.error(msg);
        }

    }

    //网络检测
    public void testConnect() {
        String notes = noteLabel.getText().trim();
        String username = usernameTextfield.getText().trim();
        String password = passwordField.getText().trim();
        String ip = ipTextfield.getText().trim();
        String port = portTextfield.getText().trim();
        StringBuilder sb = new StringBuilder().append("notes:").append(notes).append("，").append("username:").append(username).append("，").append("password:").append(password).append("，").append("ip:").append(ip).append("，").append("port:").append(port).append("\n");
        log.info(sb.toString());
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
        log.info("测试按钮被点击了");
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
//        if (isCanConnect) {
//            DialogUtil.info("连接成功");
//        } else {
//            DialogUtil.error("网络不可用");
//        }
    }

    //参数检测
    private String action(String api) {
        String username = usernameTextfield.getText().trim();
        String password = passwordField.getText().trim();
        if (username.trim().length() < 2 || password.trim().length() < 4) {
            noteLabel.setText("用户名最短2位,密码最短4位");
            return "{\"code\":500,\"msg\":\"用户名最短2位,密码最短4位\"}";
        }else {
            String fileName = StrUtil.subAfter(imageViewHead.getImage().impl_getUrl(), "images/hzw/", true);
            User user = new User().setUsername(username).setPassword(password).setHeadUrl(fileName).setCreateDate(new Date());
            Storage.onlineUser = user;//
            String resultStr = "";
            try {
                resultStr = HttpUtil.post(api, JSONObject.toJSONString(user));//post请求
            } catch (Exception e) {
                log.error(e.getMessage());
                noteLabel.setText("网络异常,请确认IP和端口");
                return "{\"code\":500,\"msg\":\"网络异常,请确认IP和端口\"}";
            }
            return resultStr;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Storage.controllers.put("loginController", this);
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
                secondStage.setX(loginStage.getX() + loginStage.getWidth() - 5);
                secondStage.setY(loginStage.getY());
                secondStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        passwordField.setOnKeyPressed(e->{
            String keyName = e.getCode().getName();
            if (keyName.equals("Enter")){
                login();
            }
        });
    }


}
