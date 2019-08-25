package com.bigfire.easychat.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.entity.Cmd;
import com.bigfire.easychat.entity.Msg;
import com.bigfire.easychat.entity.User;

import com.bigfire.easychat.util.Storage;
import com.bigfire.easychat.util.StrUtil;
import com.bigfire.easychat.websoket.MyWebSocketClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/22  0:20
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Slf4j
public class ChatController implements Initializable {

    @FXML
    ImageView userImageView;
    @FXML
    Label usernameLabel;
    @FXML
    ListView userList;
    @FXML
    Label onlineCountLabel;
    @FXML
    TextField messageBox;

    @FXML
    ListView chatPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Storage.controllers.put("chatController", this);
        User user =  Storage.onlineUser;
        String ip =  Storage.ip;
        String port =  Storage.port;
        String token = Storage.token;
        System.out.println(ip+":"+port+",token:"+token);
        userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/hzw/"+user.getHeadUrl()).toString()));
        usernameLabel.setText(user.getUsername());
        MyWebSocketClient.connect(ip, port, user.getUsername());
        messageBox.setOnKeyPressed(e->{
            String keyName = e.getCode().getName();
            if (keyName.equals("Enter")){
                sendButtonAction();
            }
        });
    }
    public void sendButtonAction(){
        String message  = messageBox.getText().trim();
        if (message.length()<1)return;
        System.out.println("输入内容:"+message);
        String username = "";
        if (message.startsWith("[") && message.contains("]")){
            username = message.substring(1,message.indexOf("]"));
            message = StrUtil.getRight(message,"]");
        }
        Msg msg = new Msg();
        msg.setFromUsername(Storage.onlineUser.getUsername());
        msg.setMsg(message);
        if (username!=null&&!username.equals("")&&checkUsername(username)){
            msg.setToUsername(username);
        }
        String ip = Storage.ip;
        String port = Storage.port;
        String msgSendApi = "http://" + ip + ":" + port + "/msg/send";
        String resultStr = HttpUtil.post(msgSendApi,JSONObject.toJSONString(msg));
        messageBox.setText("");
//        log.debug("sendMsgResult:{}",resultStr);
//        System.out.println("sendButtonAction被点击");
    }
    private Boolean checkUsername(String username){
        for (int i = 0; i < Storage.onlineUsers.size(); i++) {
            User user = Storage.onlineUsers.get(i);
            if (user.getUsername().equals(username)){
                System.out.println("发送给:"+username);
                return true;
            }
        }
        System.out.println("无效的用户名:"+username);
        return false;
    }
    public void updateUserList(ArrayList<User> users) {
        Platform.runLater(() -> {
//            System.out.println(JSONObject.toJSONString(users));
            ObservableList<User> observableList = FXCollections.observableArrayList(users);
            userList.setItems(observableList);
            userList.setCellFactory(l-> new CellRenderer());
            Platform.runLater(() -> onlineCountLabel.setText(String.valueOf(users.size())));
        });
    }
    public void updateMsg(Msg msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            protected HBox call(){
                try{
//                    StrUtil.print(JSONObject.toJSONString(msg));
                    User user = null;
                    for (int i = 0; i < Storage.onlineUsers.size(); i++) {
                        User u = Storage.onlineUsers.get(i);
                        if (u.getUsername().equals(msg.getFromUsername())){
                            user = u;
                        }
                    }
//                    StrUtil.print(JSONObject.toJSONString(user));
                    Image image = new Image(getClass().getClassLoader().getResource("images/hzw/" +  user.getHeadUrl()).toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    Label label = new Label();
                    log.debug(msg.getFromUsername() + ": "+msg.getMsg());
                    label.setText(msg.getFromUsername() + ": "+msg.getMsg());
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(profileImage,label);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    return hBox;
                }catch (Exception e){
                    e.printStackTrace();
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(new Label(e.getMessage()));
                    return hBox;
                }
            }
        };
//        StrUtil.print(JSONObject.toJSONString(msg));
        othersMessages.setOnSucceeded(event -> {
            chatPane.getItems().add(othersMessages.getValue());
        });
        Thread t = new Thread(othersMessages);
        t.setDaemon(true);
        t.start();
    }
    public void updateCmd(Cmd cmd) {

    }
}
