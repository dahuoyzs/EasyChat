package com.bigfire.easychat.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.system.OsInfo;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.speech.AipSpeech;
import com.bigfire.easychat.entity.Cmd;
import com.bigfire.easychat.entity.Msg;
import com.bigfire.easychat.entity.User;

import com.bigfire.easychat.util.DialogUtil;
import com.bigfire.easychat.util.Storage;
import com.bigfire.easychat.util.voice.Audio;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    public static final String APP_ID = "10790983";
    public static final String API_KEY = "3jfvvxihnPyBBWSVykRd40kN";
    public static final String SECRET_KEY = "keID4x33wtzpgEvwG9DwZ6dOEyrZlZIl";
    OsInfo osInfo = new OsInfo();//系统信息
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
    @FXML
    ImageView githubImageView;
    @FXML
    BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Storage.controllers.put("chatController", this);
        User user = Storage.onlineUser;
        String ip = Storage.ip;
        String port = Storage.port;
        String token = Storage.token;
        System.out.println(ip + ":" + port + ",token:" + token);
        userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/hzw/" + user.getHeadUrl()).toString()));
        usernameLabel.setText(user.getUsername());
        MyWebSocketClient.connect(ip, port, user.getUsername());

        messageBox.setOnKeyPressed(e -> {
            String keyName = e.getCode().getName();
            if (keyName.equals("Enter")) {
                sendButtonAction();
            }
        });

        githubImageView.setOnMouseClicked((event) -> {
            openWeb("https://github.com/dahuoyzs/EasyChat");
        });

        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        Audio audio = new Audio();
        audio.setAudioStopListener(data -> {
            org.json.JSONObject res = client.asr(data, "pcm", 16000, null);
            try {
                JSONArray jsonArray = res.getJSONArray("result");
                String resultStr = jsonArray.getString(0);
                System.out.println(resultStr);
                if (resultStr.contains("百度")) {
                    String content = StrUtil.subAfter(resultStr, "百度", false);
                    String url = "https://www.baidu.com/s?word=" + content;
                    executeCmd("cmd /c start " + url);
                }
                if (resultStr.contains("视频教程")) {
                    executeCmd("cmd /c start " + "https://www.bilibili.com/video/av65653369/");
                }
                if (resultStr.contains("官网")) {
                    executeCmd("cmd /c start " + "www.ibigfire.cn");
                }
                if (resultStr.contains("计算器")) {
                    executeCmd("calc");
                }
                if (resultStr.contains("笔记本")) {
                    executeCmd("notepad");
                }
                if (resultStr.contains("一分关机")) {
                    executeCmd("shutdown -s -f -t 60");
                }
                if (resultStr.contains("五分关机")) {
                    executeCmd("shutdown -s -f -t 300");
                }
                if (resultStr.contains("立即关机")) {
                    executeCmd("shutdown -s -f -t 00");
                }
                if (resultStr.contains("立刻关机")) {
                    executeCmd("shutdown -s -f -t 00");
                }
                if (resultStr.contains("取消关机")) {
                    System.out.println("取消关机命令被执行");
                    executeCmd("shutdown /a");
                }

                if (resultStr.contains("关掉QQ")) {
                    executeCmd("taskkill /f /im qq.exe");
                }
                if (resultStr.contains("我的电脑")) {
                    executeCmd("Explorer.exe /s,");
                }
            }catch (JSONException exception){
                log.info("json异常"+exception.getMessage());
            }
        });

        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
//            System.out.println("按下"+e.getCode()+":"+e.getText()+":"+e.getCode().getName());
            if (e.getCode().getName().toUpperCase().equals("ALT")) {
                audio.captureAudio();
            }

        });
        borderPane.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
//            System.out.println("放开" + e.getCode() + ":" + e.getText()+":"+e.getCode().getName());
            if (e.getCode().getName().toUpperCase().equals("ALT")) {
                audio.stop();
            }
        });
    }

    public void sendButtonAction() {
        String message = messageBox.getText().trim();
        if (message.length() < 1) return;
        System.out.println("输入内容:" + message);

        if (message.startsWith("##")) {//命令
            Cmd cmd = new Cmd();
            cmd.setFromUsername(Storage.onlineUser.getUsername());
            cmd.setType(1);
            cmd.setAction("全平台执行命令");
            String cmdStr = message.substring(2);//去掉##
            String username = "";
            if (cmdStr.startsWith("[") && cmdStr.contains("]")) {//单发
                username = StrUtil.subBetween(cmdStr, "[", "]");
                if (username != null && !username.equals("") && checkUsername(username)) {
                    cmd.setToUsername(username);
                    cmd.setCmd(StrUtil.subAfter(cmdStr, "]", false));
                } else {
                    DialogUtil.error(username + "不是线上用户,无意义的操作");
                    return;
                }
            } else {//群发
                cmd.setCmd(cmdStr);
            }
            sendCmd(cmd);
        } else {//消息
            Msg msg = new Msg();
            msg.setFromUsername(Storage.onlineUser.getUsername());
            String username = "";
            if (message.startsWith("[") && message.contains("]")) {//单发
                username = StrUtil.subBetween(message, "[", "]");
                if (username != null && !username.equals("") && checkUsername(username)) {
                    msg.setToUsername(username);
                    msg.setMsg(StrUtil.subAfter(message, "]", false));
                } else {
                    DialogUtil.error(username + "不是线上用户,无意义的操作");
                    return;
                }
            } else {//群发
                msg.setMsg(message);
            }
            sendMsg(msg);
        }
        messageBox.setText("");
    }

    private void sendMsg(Msg msg) {
        String ip = Storage.ip;
        String port = Storage.port;
        String msgSendApi = "http://" + ip + ":" + port + "/msg/send";
        String resultStr = HttpUtil.post(msgSendApi, JSONObject.toJSONString(msg));
    }

    private void sendCmd(Cmd cmd) {
        String ip = Storage.ip;
        String port = Storage.port;
        String msgSendApi = "http://" + ip + ":" + port + "/cmd/send";
        String resultStr = HttpUtil.post(msgSendApi, JSONObject.toJSONString(cmd));
    }

    private Boolean checkUsername(String username) {
        for (int i = 0; i < Storage.onlineUsers.size(); i++) {
            User user = Storage.onlineUsers.get(i);
            if (user.getUsername().equals(username)) {
                System.out.println("发送给:" + username);
                return true;
            }
        }
        System.out.println("无效的用户名:" + username);
        return false;
    }

    public void updateUserList(ArrayList<User> users) {
        Platform.runLater(() -> {
//            System.out.println(JSONObject.toJSONString(users));
            ObservableList<User> observableList = FXCollections.observableArrayList(users);
            userList.setItems(observableList);
            userList.setCellFactory(l -> new CellRenderer());
            Platform.runLater(() -> onlineCountLabel.setText(String.valueOf(users.size())));
        });
    }

    public void updateMsg(Msg msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            protected HBox call() {
                try {
                    User user = null;
                    for (int i = 0; i < Storage.onlineUsers.size(); i++) {
                        User u = Storage.onlineUsers.get(i);
                        if (u.getUsername().equals(msg.getFromUsername())) {
                            user = u;
                        }
                    }
                    Image image = new Image(getClass().getClassLoader().getResource("images/hzw/" + user.getHeadUrl()).toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    Label label = new Label();
                    log.debug(msg.getFromUsername() + ": " + msg.getMsg());
                    String toUsername = msg.getToUsername();
                    if (toUsername == null || toUsername.equals("")) {//群发
                        label.setText(msg.getFromUsername() + ": " + msg.getMsg());
                    } else {//私发
                        label.setText(msg.getFromUsername() + "【私信】: " + msg.getMsg());
                    }
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(profileImage, label);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    return hBox;
                } catch (Exception e) {
                    e.printStackTrace();
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(new Label(e.getMessage()));
                    return hBox;
                }
            }
        };
        othersMessages.setOnSucceeded(event -> {
            chatPane.getItems().add(othersMessages.getValue());
        });
        Thread t = new Thread(othersMessages);
        t.setDaemon(true);
        t.start();
    }

    public void updateCmd(Cmd cmd) {
        Integer type = cmd.getType();
        if (type == 0) {
            String action = cmd.getAction();
            if (action.equals("closeComputer")) {

            }
            if (action.equals("killQQ")) {

            }
            if (action.equals("openProjectGithub")) {
                openWeb("https://github.com/dahuoyzs/EasyChat");
            }
            if (action.equals("openBigFire")) {
                openWeb("https://www.ibigfire.cn");
            }
            if (action.equals("openBaiDu")) {
                openWeb("https://www.baidu.com");
            }
        } else if (type == 1) {//通用解析
            executeCmd(cmd.getCmd());
        } else if (type == 2 && osInfo.isWindows()) {//windows解析
            executeCmd(cmd.getCmd());
        } else if (type == 3 && osInfo.isLinux()) {//linux解析
            executeCmd(cmd.getCmd());
        } else if (type == 4 && osInfo.isMac()) {//mac解析
            executeCmd(cmd.getCmd());
        }
    }

    //程序内 执行cmd 命令
    public void executeCmd(String cmdStr) {
        try {
            Runtime.getRuntime().exec(cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeComputer() {
        DialogUtil.error("此功能暂未开发");
    }

    private void killQQ() {
        DialogUtil.error("此功能暂未开发");//在网上找相关杀死QQ进程的命令直接执行即可
    }

    private void openWeb(String url) {
        if (osInfo.isWindows()) {
            executeCmd("cmd /c start " + url);
        } else if (osInfo.isLinux()) {
            executeCmd("x-www-browser '" + url + "'");
        } else if (osInfo.isMac()) {
            executeCmd("open '" + url + "'");
        }
    }

}
